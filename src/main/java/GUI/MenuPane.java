package GUI;

import GUI.EventHandler.ChessActionEventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class MenuPane extends BorderPane {

    public MenuPane(){
        FlowPane topPane = new FlowPane();
        Label topLabel = new Label("Checkers");
        topPane.getChildren().addAll(topLabel);
        this.setTop(topPane);

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

        //fill the center Pane
        centerPane.getChildren().addAll(chooseGameModePane);
        this.setCenter(centerPane);

        //creating Pane to fill the bottom of the scene
        FlowPane bottomPane = new FlowPane();

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

        bottomPane.getChildren().addAll(playButton);
        this.setBottom(bottomPane);


        this.setBackground(new Background(new BackgroundFill(Color.rgb(179, 77, 77), CornerRadii.EMPTY, Insets.EMPTY)));

        topLabel.setPrefHeight(30);
        topLabel.setStyle("-fx-font-size: 20pt; -fx-text-fill: yellow");

        playButton.setBackground(new Background(new BackgroundFill(Color.rgb(255, 234, 106), CornerRadii.EMPTY, Insets.EMPTY)));

    }
}
