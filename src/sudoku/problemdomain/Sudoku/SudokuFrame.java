package sudoku.problemdomain.Sudoku;

import sudoku.problemdomain.constants.Mode;

import javax.swing.*;
import java.awt.*;

public class SudokuFrame extends JFrame {


    SudokuGame sudokuGame;
    public SudokuFrame(Mode mode)  {
        this.setTitle("Sudoku");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sudokuGame = new SudokuGame(this,mode);
        this.add(sudokuGame);
        this.setBackground(Color.WHITE);
        this.setResizable(false);
        this.setVisible(true);
        this.pack();
        this.setLocationRelativeTo(null);
    }
}
