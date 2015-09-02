package game2d;

import javafx.application.Application;
import javafx.stage.Stage;

public class GameWindow extends Application {

    @Override
    public void start(Stage stage) {
        Settings config;

        config = new Settings("config.properties");
        int show_menu = Integer.parseInt(config.get("show_menu", "1"));

        SoundController.init();

        if (show_menu > 0) {
            GameMenu menu = new GameMenu(this, stage);
            stage.setScene(menu.scene);
        } else {
            startGame(stage);
        }

        stage.setTitle(config.get("title", "The amazing adventures of BOX"));
        stage.show();
    }

    public void startGame(Stage stage) {
        Game2d game = new Game2d();
        stage.setScene(game.scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
