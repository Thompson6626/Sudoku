package sudoku.problemdomain.Sudoku;

import sudoku.problemdomain.Menu.MainMenu;
import sudoku.problemdomain.Menu.MainMenuPanel;
import sudoku.problemdomain.constants.GameState;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

import static sudoku.problemdomain.Sudoku.SudokuUtils.*;
import static sudoku.problemdomain.constants.GameState.ACTIVE;
import static sudoku.problemdomain.constants.GameState.COMPLETED;
import static sudoku.problemdomain.constants.Messages.*;

public class SudokuGame extends JPanel implements Serializable {

    private GameState gameState;
    private static CustomBorderTextField[][] sudokuSquares;
    private static final int SUDOKU_SQUARE_SIZE = 50;
    private static final int SUDOKU_GAME_SCREEN_SIZE = SUDOKU_SQUARE_SIZE * 9;
    private static final Font SUDOKU_FONT = new Font("Work Sans",Font.PLAIN,25);
    private static final Font SUDOKU_CLUES_FONT = new Font("Work Sans",Font.BOLD,30);
    private static final Dimension SUDOKU_SCREEN_SIZE = new Dimension(
            SUDOKU_GAME_SCREEN_SIZE,
            SUDOKU_GAME_SCREEN_SIZE
    );
    private final Map<Long,int[][]> sudokus = new HashMap<>();
    private long currentBaseSudoku;
    public static final int GRID_BOUNDARY = 9;
    JMenuBar jMenuBar;
    JFrame parent;

    public SudokuGame(JFrame parent) {
        this.parent = parent;
        this.setPreferredSize(SUDOKU_SCREEN_SIZE);
        this.setLayout(null);

        try {
            if(sudokuSquares == null){
                sudokuSquares = new CustomBorderTextField[GRID_BOUNDARY][GRID_BOUNDARY];
                generateSudokuSquares();
                generateRandomSudoku();
            }else{
                displayUnfinishedSudoku();
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        jMenuBar = new JMenuBar();

        JMenu game = new JMenu("Help");
        JMenuItem reset = new JMenuItem("Reset");
        JMenuItem change = new JMenuItem("New Game");
        JMenuItem exit = new JMenuItem("Exit");

        reset.addActionListener(e -> {
            if (gameState == ACTIVE){
                showConfirmationDialog(RESET_WARNING, this::resetCurrentSudoku);
            }else {
                resetCurrentSudoku();
            }
        });

        change.addActionListener(e -> {
            if (gameState == ACTIVE){
                showConfirmationDialog(NEW_GAME_WARNING, this::generateRandomSudoku);
            }else {
                generateRandomSudoku();
            }
        });

        exit.addActionListener(e -> {
            if(gameState == ACTIVE){
                showConfirmationDialog(EXIT_WARNING, () -> {
                    parent.dispose();
                    MainMenuPanel.setOnGoingGame(sudokuSquares);
                    SwingUtilities.invokeLater(MainMenu::new);
                });
            }else {
                parent.dispose();
                sudokuSquares = null;
                MainMenuPanel.setOnGoingGame(sudokuSquares);
                SwingUtilities.invokeLater(MainMenu::new);
            }
        });

        game.add(reset);
        game.add(change);
        game.addSeparator();
        game.add(exit);


        jMenuBar.add(game);
        parent.setJMenuBar(jMenuBar);

    }
    private void showConfirmationDialog(String message, Runnable actionOnConfirmation) {
        int option = JOptionPane.showOptionDialog(parent,
                message,
                "Warning",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Yes", "No"},
                null);
        if (option == JOptionPane.YES_OPTION) {
            actionOnConfirmation.run();
        }
    }


    private void displayUnfinishedSudoku() {
        for (CustomBorderTextField[] sudokuSquare : sudokuSquares) {
            for (CustomBorderTextField textField : sudokuSquare){
                this.add(textField);
            }
        }
    }

    private void resetCurrentSudoku() {
        for (CustomBorderTextField[] arr:sudokuSquares){
            for (CustomBorderTextField textField:arr){
                if (textField.isEditable())
                    textField.setText("");
            }
        }
    }




    private void generateSudokuSquares() throws ParseException {
        NumberFormat intFormat = NumberFormat.getIntegerInstance();
        intFormat.setGroupingUsed(false);
        NumberFormatter numberFormatter = new NumberFormatter(intFormat){
            @Override
            public Object stringToValue(String text) throws ParseException {
                if (text.length() == 0)
                    return null;
                return super.stringToValue(text);
            }
        };
        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setMinimum(1);
        numberFormatter.setMaximum(9);

        sudokuSquares = new CustomBorderTextField[GRID_BOUNDARY][GRID_BOUNDARY];
        final int squareWidth = 50;
        final int squareHeight = 50;
        for (int i = 0; i < GRID_BOUNDARY; i++) {
            for (int j = 0; j < GRID_BOUNDARY; j++) {
                CustomBorderTextField textField = new CustomBorderTextField(
                        1,
                        Color.gray, // Top
                        Color.gray, // Bottom
                        Color.gray, // Left
                        Color.gray,
                        numberFormatter// Right
                );
                textField.setColumns(1);
                textField.setVisible(true);
                textField.setBounds(
                        squareWidth * j,
                        squareHeight * i,
                        squareWidth,
                        squareHeight
                );
                textField.addKeyListener(new SudokuSquareListener());
                textField.setFont(SUDOKU_FONT);
                textField.setHorizontalAlignment(JTextField.CENTER);

                sudokuSquares[i][j] = textField;
                this.add(sudokuSquares[i][j]);
            }
        }

    }




    private void generateRandomSudoku(){
        int clues = 17 + new Random().nextInt(14);

        for (CustomBorderTextField[] arr:sudokuSquares){
            for (CustomBorderTextField customBorderTextField : arr) {
                customBorderTextField.setText("");
                customBorderTextField.setEditable(true);
                customBorderTextField.setFont(SUDOKU_FONT);
            }
        }

        gameState = ACTIVE;

        Sudoku sudoku = new Sudoku(GRID_BOUNDARY,clues);
        sudoku.fillValues();

        int[][] mat = sudoku.mat;
        for (int i = 0; i < GRID_BOUNDARY; i++) {
            for (int j = 0; j < GRID_BOUNDARY; j++) {
                if (mat[i][j] != 0){
                    sudokuSquares[i][j].setText(String.valueOf(mat[i][j]));
                    sudokuSquares[i][j].setFont(SUDOKU_CLUES_FONT);
                    sudokuSquares[i][j].setEditable(false);
                }
            }
        }
        // [row,col,number]
        currentBaseSudoku = getHashCode(sudokuSquares);
        sudokus.put(currentBaseSudoku,mat);
    }

    private void gameCompleted(){
        gameState = COMPLETED;
        showConfirmationDialog(GAME_COMPLETE, this::generateRandomSudoku);
    }
    private class SudokuSquareListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {}
        @Override
        public void keyPressed(KeyEvent e) {}
        @Override
        public void keyReleased(KeyEvent e) {
            if (
                    sudokuGridIsFull(sudokuSquares) &&
                    isValidSudoku(sudokuSquares) &&
                    gameState == ACTIVE
            ){
                gameCompleted();
            }
        }
    }

    public static CustomBorderTextField[][] getSudokuSquares() {
        return sudokuSquares;
    }

    public static void setSudokuSquares(CustomBorderTextField[][] sudokuSquares) {
        SudokuGame.sudokuSquares = sudokuSquares;
    }
}
