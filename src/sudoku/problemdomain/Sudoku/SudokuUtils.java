package sudoku.problemdomain.Sudoku;

import sudoku.problemdomain.constants.SaveDirectories;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static sudoku.problemdomain.constants.Fonts.SUDOKU_CLUES_FONT;
import static sudoku.problemdomain.constants.Fonts.SUDOKU_FONT;

public class SudokuUtils {
    public static boolean sudokuGridIsFull(CustomBorderTextField[][] board) {
        return Arrays.stream(board)
                .flatMap(Arrays::stream)
                .noneMatch(textField -> textField.getText().isBlank());
    }
    public static boolean isValidSudoku(CustomBorderTextField[][] board) {
        Set<String> rowSet;
        Set<String> colSet;
        for (int i = 0; i < 9; i++) {
            rowSet = new HashSet<>();
            colSet = new HashSet<>();
            for (int j = 0; j < 9; j++) {
                String r = board[i][j].getText();
                String c = board[j][i].getText();
                if (!r.isBlank()){
                    if (rowSet.contains(r))return false;
                    else rowSet.add(r);
                }
                if (!c.isBlank()){
                    if (colSet.contains(c)) return false;
                    else colSet.add(c);
                }
            }
        }
        for (int i = 0; i < 9; i = i + 3) {
            for (int j = 0; j < 9; j = j + 3) {
                if (!checkBlock(i, j, board)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkBlock(int idxI, int idxJ, CustomBorderTextField[][] board) {
        Set<String> blockSet = new HashSet<>();

        int rows = idxI + 3;
        int cols = idxJ + 3;
        for (int i = idxI; i < rows; i++) {
            for (int j = idxJ; j < cols; j++) {
                if (board[i][j].getText().isBlank()) {
                    continue;
                }
                if (blockSet.contains(board[i][j].getText())) {
                    return false;
                }
                blockSet.add(board[i][j].getText());
            }
        }
        return true;
    }



    public static void resetCurrentSudoku(CustomBorderTextField[][] mat) {
        for (CustomBorderTextField[] arr:mat){
            for (CustomBorderTextField textField:arr){
                if (textField.isEditable())
                    textField.setText("");
            }
        }
    }


    public static void saveIntoPending(
            String name,
            CustomBorderTextField[][] mat
    ) {
        File pendingFolder = new File(SaveDirectories.PENDING_GAME_FOLDER);
        if (!pendingFolder.exists()){
            pendingFolder.mkdirs();
        }

        File file = new File(pendingFolder,name);
        try {
            FileWriter writer = new FileWriter(file, false);

            writer.write("");
            writer.close();
            writer = new FileWriter(file, true);
            writeSudokuGame(
                    writer,
                    mat
            );
            writer.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public static void writeSudokuGame(
            FileWriter writer,
            CustomBorderTextField[][] mat
    ) throws IOException {
        try (writer) {
            for (CustomBorderTextField[] txtField : mat) {
                StringBuilder line = new StringBuilder();
                for (int j = 0; j < mat[0].length; j++) {
                    String num = txtField[j].getText();
                    line.append(num.isBlank() ? " " : txtField[j].isEditable() ? num : num + "!");
                    if (j < mat[0].length - 1) {
                        line.append("|");
                    }
                }
                writer.write(line + "\n");
            }
        }
    }

    public static void displayPendingSudoku(
            CustomBorderTextField[][] mat,
            File pending
    ){
        try {
            int row = 0;
            Scanner sc = new Scanner(pending);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] nums = line.split("\\|");

                for (int i = 0; i < nums.length;i++){
                    CustomBorderTextField txtField = mat[row][i];
                    if (!nums[i].isBlank() && nums[i].length() > 1) {
                        txtField.setEditable(false);
                        txtField.setFont(SUDOKU_CLUES_FONT);
                    }else {
                        txtField.setFont(SUDOKU_FONT);
                    }

                    txtField.setText(nums[i].isBlank() ? "" : nums[i].substring(0,1));
                }
                ++row;
            }
            sc.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
