import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.util.Queue;

public class ChessWindow extends Application implements Runnable {

    private final Queue<String> events;
    public static Queue<String> eventTemp;


    public ChessWindow() {
        events = eventTemp;
    }

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();

        root.setTop(new Label("Chess Test Window"));

        //creating Pane to fill the center of the scene
        FlowPane centerPane = new FlowPane();

        //creating and filling the Pane for the game Mode
        FlowPane chooseGameModePane = new FlowPane();
        Label gameModeLabel = new Label("Game against: ");
        final ToggleGroup playerOrBot = new ToggleGroup();
        RadioButton gamePVP = new RadioButton("Player");
        gamePVP.setToggleGroup(playerOrBot);
        gamePVP.setSelected(true);
        RadioButton gamePVE = new RadioButton("Computer");
        gamePVE.setToggleGroup(playerOrBot);
        chooseGameModePane.getChildren().addAll(gameModeLabel, gamePVP, gamePVE);

        //creating and filling the Pane for the difficulty of the Bot
        FlowPane chooseDifficultyPane = new FlowPane();
        Label difficultySettingLabel = new Label("Bot Strength: ");
        Slider botDifficulty = new Slider();
        botDifficulty.setMin(0);
        botDifficulty.setMax(100);
        botDifficulty.setValue(40);
        botDifficulty.setShowTickLabels(true);
        botDifficulty.setShowTickMarks(false);
        botDifficulty.setMajorTickUnit(50);
        botDifficulty.setMinorTickCount(5);
        botDifficulty.setBlockIncrement(10);
        chooseDifficultyPane.getChildren().addAll(difficultySettingLabel, botDifficulty);

        //creating and filling the Pane for the player Color
        FlowPane chooseColorPane = new FlowPane();
        Label ColorLabel = new Label("Color: ");
        final ToggleGroup playerColor = new ToggleGroup();
        RadioButton colorWhite = new RadioButton("White");
        colorWhite.setToggleGroup(playerColor);
        colorWhite.setSelected(true);
        RadioButton colorBlack = new RadioButton("Black");
        colorBlack.setToggleGroup(playerColor);
        RadioButton colorRandom = new RadioButton("Random");
        colorRandom.setToggleGroup(playerColor);
        chooseColorPane.getChildren().addAll(ColorLabel, colorWhite, colorBlack, colorRandom);

        //creating the Pane for the play button
        FlowPane playButtonPane = new FlowPane();
        Button playButton = new Button("Play");
        playButton.setOnAction(new ChessActionEventHandler(events){
            @Override
            public void handle(ActionEvent event) {
                send("Chessmenu_StartGame");
            }
        });

        playButtonPane.getChildren().addAll(playButton);

        //fill the center Pane
        centerPane.getChildren().addAll(chooseGameModePane, chooseDifficultyPane, chooseColorPane, playButtonPane);
        root.setCenter(centerPane);

        root.setBottom(new Label("Testing!"));

        primaryStage.setTitle("Chess");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setOnCloseRequest(new ChessWindowEventHandler(events));
        primaryStage.show();
    }

    @Override
    public void run() {
        launch();
    }
}
