package GUI;

import GUI.ChessActionEventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class MenuPane extends BorderPane {

    public MenuPane(){
        this.setTop(new Label("Chess"));

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
        playButton.setOnAction(new ChessActionEventHandler(){
            @Override
            public void handle(ActionEvent event) {
                send("Chessmenuwindow_StartGame");
            }
        });

        playButtonPane.getChildren().addAll(playButton);

        //fill the center Pane
        centerPane.getChildren().addAll(chooseGameModePane, chooseDifficultyPane, chooseColorPane, playButtonPane);
        this.setCenter(centerPane);

        this.setBottom(new Label("Testing!"));

    }
}
