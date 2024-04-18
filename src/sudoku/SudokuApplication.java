package sudoku;

import sudoku.problemdomain.Menu.MainMenu;

import javax.swing.*;

public class SudokuApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenu::new);
    }
}