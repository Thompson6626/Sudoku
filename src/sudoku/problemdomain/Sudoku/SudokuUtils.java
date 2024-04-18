package sudoku.problemdomain.Sudoku;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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


    private static final int PRIME = 37;
    public static long getHashCode(CustomBorderTextField[][] grid) {
        if (grid == null || grid.length != 9 || grid[0].length != 9) {
            throw new IllegalArgumentException("Input grid must be a 9x9 String array");
        }

        long hash = 0;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String value = grid[i][j].getText();
                if (value.isBlank()) {
                    value = "-";
                }
                hash = Math.abs((hash * PRIME + value.charAt(0)) % Long.MAX_VALUE);
            }
        }
        return hash;
    }
}
