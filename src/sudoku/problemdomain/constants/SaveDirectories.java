package sudoku.problemdomain.constants;

import java.io.File;

public class SaveDirectories {
    private static final String BASE_FOLDER = "src";
    private static final String GAME_FOLDER = "sudoku";
    private static final String DOMAIN_FOLDER = "problemdomain";
    private static final String SAVED_GAMES_FOLDER = "SavedGames";
    private static final String TXT_NAME = "Pending.txt";
    public static final String PENDING_GAME_FOLDER =
            System.getProperty("user.dir") + File.separator +
            BASE_FOLDER + File.separator + GAME_FOLDER + File.separator +
            DOMAIN_FOLDER + File.separator + SAVED_GAMES_FOLDER;
    public static final String PENDING_GAME_PATH =
            System.getProperty("user.dir") + File.separator +
            BASE_FOLDER + File.separator + GAME_FOLDER + File.separator +
            DOMAIN_FOLDER + File.separator + SAVED_GAMES_FOLDER + File.separator + TXT_NAME;

}
