package sudoku.problemdomain.Menu;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {


    MainMenuPanel panel;
    public MainMenu(){
        this.setTitle("Sudoku");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new MainMenuPanel();
        this.add(panel);
        this.setBackground(Color.WHITE);
        this.setResizable(false);
        this.setVisible(true);
        this.pack();
        this.setLocationRelativeTo(null);
    }




}
