package sudoku.problemdomain.Sudoku;

import sudoku.problemdomain.Menu.MainMenu;
import sudoku.problemdomain.constants.GameState;
import sudoku.problemdomain.constants.Mode;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Random;
import java.awt.event.WindowEvent;

import static sudoku.problemdomain.Sudoku.SudokuUtils.*;
import static sudoku.problemdomain.constants.Fonts.SUDOKU_CLUES_FONT;
import static sudoku.problemdomain.constants.Fonts.SUDOKU_FONT;
import static sudoku.problemdomain.constants.GameState.ACTIVE;
import static sudoku.problemdomain.constants.GameState.COMPLETED;
import static sudoku.problemdomain.constants.Messages.*;
import static sudoku.problemdomain.constants.SaveDirectories.PENDING_GAME_PATH;

public class SudokuGame extends JPanel implements Serializable {

    private GameState gameState;
    private static CustomBorderTextField[][] sudokuSquares;
    private static final int SUDOKU_SQUARE_SIZE = 50;
    private static final int SUDOKU_GAME_SCREEN_SIZE = SUDOKU_SQUARE_SIZE * 9;

    private static final Dimension SUDOKU_SCREEN_SIZE = new Dimension(
            SUDOKU_GAME_SCREEN_SIZE,
            SUDOKU_GAME_SCREEN_SIZE
    );
    private static final File PENDING_GAME = new File(PENDING_GAME_PATH);
    public static final int GRID_BOUNDARY = 9;
    JMenuBar jMenuBar;
    JFrame parent;
    private static final NumberFormat INT_FORMAT;
    static {
        INT_FORMAT = NumberFormat.getIntegerInstance();
        INT_FORMAT.setGroupingUsed(false);
    }
    private static final NumberFormatter NUMBER_FORMATTER;
    static{
        NUMBER_FORMATTER = new NumberFormatter(INT_FORMAT){
            @Override
            public Object stringToValue(String text) throws ParseException {
                if (text.length() == 0)
                    return null;
                return super.stringToValue(text);
            }
        };
        NUMBER_FORMATTER.setValueClass(Integer.class);
        NUMBER_FORMATTER.setAllowsInvalid(false);
        NUMBER_FORMATTER.setMinimum(1);
        NUMBER_FORMATTER.setMaximum(9);
    }
    public SudokuGame(JFrame parent, Mode mode) {
        gameState = ACTIVE;
        this.parent = parent;
        this.setPreferredSize(SUDOKU_SCREEN_SIZE);
        this.setLayout(null);
        parent.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveIntoPending("Pending.txt",sudokuSquares);
            }
        });

        try {
            if (sudokuSquares == null || sudokuSquares[0] == null)
                generateSudokuSquares();


            if(mode == Mode.NEW_GAME){
                generateRandomSudoku();
            }else if(mode == Mode.CONTINUE){
                displayPendingSudoku(sudokuSquares,PENDING_GAME);
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
                showConfirmationDialog(RESET_WARNING, () -> {
                    resetCurrentSudoku(sudokuSquares);
                    deletePendingGame();
                });
            }else {
                resetCurrentSudoku(sudokuSquares);
                deletePendingGame();
            }
        });

        change.addActionListener(e -> {
            if (gameState == ACTIVE)
                showConfirmationDialog(NEW_GAME_WARNING, this::generateRandomSudoku);
            else
                generateRandomSudoku();
        });

        exit.addActionListener(e -> {
            if(gameState == ACTIVE){
                showConfirmationDialog(EXIT_WARNING, () -> {
                    parent.dispose();
                    saveIntoPending("Pending.txt",sudokuSquares);
                    sudokuSquares = null;
                    SwingUtilities.invokeLater(MainMenu::new);
                });
            }else {
                parent.dispose();
                deletePendingGame();
                sudokuSquares = null;
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
                "No");
        if (option == JOptionPane.YES_OPTION) {
            actionOnConfirmation.run();
        }
    }

    private void generateSudokuSquares() throws ParseException {
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
                        Color.gray, // Right
                        NUMBER_FORMATTER
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
                this.add(textField);
            }
        }

    }




    private void generateRandomSudoku(){
        deletePendingGame();
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
    public static File getPendingGame(){
        return PENDING_GAME;
    }
    public static void deletePendingGame(){
        PENDING_GAME.delete();
    }
}
