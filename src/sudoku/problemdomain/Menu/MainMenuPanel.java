package sudoku.problemdomain.Menu;


import sudoku.problemdomain.Sudoku.SudokuFrame;
import sudoku.problemdomain.Sudoku.SudokuGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static sudoku.problemdomain.constants.Mode.CONTINUE;
import static sudoku.problemdomain.constants.Mode.NEW_GAME;

public class MainMenuPanel extends JPanel implements ActionListener {
    private static final int MENU_SCREEN_WIDTH = 500;
    private static final int MENU_SCREEN_HEIGHT = 500;
    private static final Dimension MENU_SCREEN_SIZE = new Dimension(MENU_SCREEN_WIDTH, MENU_SCREEN_HEIGHT);
    private static boolean PENDING;
    JButton continueButton;
    JButton playButton;
    JLabel titleLabel;

    public MainMenuPanel( ){
        PENDING = SudokuGame.getPendingGame().exists();
        this.setPreferredSize(MENU_SCREEN_SIZE);
        this.setLayout(null);

        generateMenu();
    }
    private void generateMenu(){
        generateMenuSudokuTextField();
        if(PENDING) generateContinueGame();
        generateMenuPlayButton();
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
        final int buttonWidth = 150;
        final int buttonHeight = 80;
        continueButton = new JButton("Continue");
        continueButton.setFont(new Font(
                "Work Sans",
                Font.PLAIN,
                23
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
        final int buttonWidth = PENDING ? 150 : 170;
        final int buttonHeight = PENDING ? 80 : 100;
        playButton = new JButton(PENDING ? "New Game" : "Play");
        playButton.setFont(new Font(
                "Optimus Princeps",
                Font.PLAIN,
                PENDING ? 20 : 30)
        );

        playButton.setBounds(
                (MENU_SCREEN_WIDTH >> 1) - (buttonWidth >> 1),
                PENDING ?  continueButton.getY() + buttonHeight + (buttonHeight / 3) : (MENU_SCREEN_HEIGHT >> 1) - (buttonHeight >> 1) ,
                buttonWidth,
                buttonHeight
        );
        playButton.addActionListener(this);
        playButton.setVisible(true);
        playButton.setFocusable(false);
        this.add(playButton);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == playButton || e.getSource() == continueButton){
            Window ancestor = (Window) this.getTopLevelAncestor();
            if (ancestor instanceof Frame) {
                ancestor.dispose();
            }
            if(e.getSource() == playButton) {
                SudokuGame.deletePendingGame();
                SwingUtilities.invokeLater(() -> new SudokuFrame(NEW_GAME));
            }else if(e.getSource() == continueButton){
                SwingUtilities.invokeLater(() -> new SudokuFrame(CONTINUE));
            }
        }
    }


}
