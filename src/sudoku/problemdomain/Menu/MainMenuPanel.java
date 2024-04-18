package sudoku.problemdomain.Menu;


import sudoku.problemdomain.Sudoku.CustomBorderTextField;
import sudoku.problemdomain.Sudoku.SudokuFrame;
import sudoku.problemdomain.Sudoku.SudokuGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class MainMenuPanel extends JPanel implements ActionListener {
    private static final int MENU_SCREEN_WIDTH = 500;
    private static final int MENU_SCREEN_HEIGHT = 500;
    private static final Dimension MENU_SCREEN_SIZE = new Dimension(MENU_SCREEN_WIDTH, MENU_SCREEN_HEIGHT);
    private static CustomBorderTextField[][] onGoingGame = null;

    JButton continueButton;
    JButton playButton;
    JButton historyButton;
    JLabel titleLabel;

    public MainMenuPanel( ){
        this.setPreferredSize(MENU_SCREEN_SIZE);
        this.setLayout(null);

        generateMenu();
    }
    private void generateMenu(){
        generateMenuSudokuTextField();
        if(onGoingGame != null) generateContinueGame();
        generateMenuPlayButton();
        generateMenuHistoryButton();
    }
    private void generateMenuSudokuTextField(){
        final int labelWidth = 200;
        final int labelHeight = 80;
        titleLabel = new JLabel("Sudoku");
        titleLabel.setFont(new Font("Work Sans",Font.BOLD,40));
        titleLabel.setBounds(
                (MENU_SCREEN_WIDTH >> 1) - (labelWidth >> 1) + 20,
                labelHeight,
                labelWidth,
                labelHeight
        );
        this.add(titleLabel);
    }
    private void generateContinueGame(){
        final int buttonWidth = 120;
        final int buttonHeight = 60;
        continueButton = new JButton("Continue");
        continueButton.setFont(new Font(
                "Work Sans",
                Font.PLAIN,
                18
        ));
        continueButton.setVisible(true);
        continueButton.setFocusable(false);
        continueButton.addActionListener(this);
        continueButton.setBounds(
                (MENU_SCREEN_WIDTH >> 1) - (buttonWidth >> 1),
                titleLabel.getY() + buttonHeight + (buttonHeight >> 1),
                buttonWidth,
                buttonHeight
        );
        this.add(continueButton);
    }

    private void generateMenuPlayButton(){
        final int buttonWidth = onGoingGame == null ? 170 : 120 ;
        final int buttonHeight = onGoingGame == null ? 100 : 60;
        playButton = new JButton(onGoingGame == null ? "Play" : "New Game");
        playButton.setFont(new Font(
                "Optimus Princeps",
                Font.PLAIN,
                onGoingGame == null ? 30:15)
        );

        playButton.setBounds(
                (MENU_SCREEN_WIDTH >> 1) - (buttonWidth >> 1),
                onGoingGame == null ? (MENU_SCREEN_HEIGHT >> 1) - (buttonHeight >> 1) : continueButton.getY() + buttonHeight + (buttonHeight / 3),
                buttonWidth,
                buttonHeight
        );
        playButton.addActionListener(this);
        playButton.setVisible(true);
        playButton.setFocusable(false);
        this.add(playButton);
    }
    private void generateMenuHistoryButton(){
        final int buttonWidth = onGoingGame == null ? 170 : 120;
        final int buttonHeight = onGoingGame == null ? 100 : 60;
        historyButton = new JButton("History");
        historyButton.setFocusable(false);
        historyButton.setFont(new Font(
                "Work Sans",
                Font.PLAIN,
                onGoingGame == null ? 30 : 20
        ));
        historyButton.setBounds(
                (MENU_SCREEN_WIDTH >> 1) - (buttonWidth >> 1),
                playButton.getY() + buttonHeight + (buttonHeight / 3),
                buttonWidth,
                buttonHeight
        );
        historyButton.addActionListener(this);
        this.add(historyButton);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == playButton || e.getSource() == continueButton){
            Window ancestor = (Window) this.getTopLevelAncestor();
            if (ancestor instanceof Frame) {
                ancestor.dispose();
            }
            if(e.getSource() == playButton) SudokuGame.setSudokuSquares(null);
            SwingUtilities.invokeLater(SudokuFrame::new);
        }
        if (e.getSource() == historyButton) {
            System.out.println();
        }

    }
    public static void setOnGoingGame(CustomBorderTextField[][] onGoingGame) {
        MainMenuPanel.onGoingGame = onGoingGame;
    }

}
