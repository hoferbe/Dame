package GUI;

import GUI.EventHandler.ChessActionEventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class MenuPane extends AnchorPane {

    public MenuPane(){
        FlowPane topPane = new FlowPane();
        Label topLabel = new Label("Checkers");
        topPane.getChildren().addAll(topLabel);
        AnchorPane.setTopAnchor(topPane, 0.0);
        AnchorPane.setLeftAnchor(topPane, 95.0);

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
        AnchorPane.setTopAnchor(centerPane, 100.0);
        AnchorPane.setLeftAnchor(centerPane, 50.0);

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
        AnchorPane.setTopAnchor(bottomPane, 200.0);
        AnchorPane.setLeftAnchor(bottomPane, 130.0);

        this.getChildren().addAll(topPane, centerPane, bottomPane);


        this.setBackground(new Background(new BackgroundFill(Color.rgb(179, 77, 77), CornerRadii.EMPTY, Insets.EMPTY)));

        topLabel.setPrefHeight(30);
        topLabel.setStyle("-fx-font-size: 20pt; -fx-text-fill: yellow");

        playButton.setBackground(new Background(new BackgroundFill(Color.rgb(255, 234, 106), CornerRadii.EMPTY, Insets.EMPTY)));

    }
}
