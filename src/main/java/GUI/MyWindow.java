package GUI;

import Controller.GUIController;

import GUI.EventHandler.ChessWindowEventHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.HashMap;
import java.util.Map;

public class MyWindow extends Application {

    Stage myStage;

    private Map<String, Scene> myScenes;
    ChessboardPane chessPane;
    String activeScene;

    public void fillMap(){
        myScenes = new HashMap<>();
        myScenes.put("Menu", new Scene(new MenuPane(), 300, 275));
        chessPane = new ChessboardPane();
        myScenes.put("Engine.Chessboard", new Scene(new ChessboardPane(), 840, 880));
    }

    @Override
    public void start(Stage primaryStage){
        GUIController.myWindow = this;

        fillMap();
        myStage = primaryStage;

        activeScene = "Menu";
        myStage.setTitle("Chekers");
        myStage.setScene(myScenes.get("Menu"));

        myStage.setOnCloseRequest(new ChessWindowEventHandler(){
            @Override
            public void handle(WindowEvent event) {
                send("Chesswindow_Closeprogram");
            }
        });
        myStage.show();
    }

    @Override
    public void stop(){
        System.out.println("Application stopped");
    }

    public void changeScene(String sceneName){
        if(myScenes.containsKey(sceneName)){
            activeScene = sceneName;
            myStage.setScene(myScenes.get(sceneName));
            myStage.show();
        }
    }

    public void winnerFound(String color){
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(myStage);
        StackPane dialogVbox = new StackPane();
        Text text = new Text(color + " has won!");
        dialogVbox.getChildren().add(text);
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();

        dialogVbox.setBackground(new Background(new BackgroundFill(Color.rgb(179, 77, 77), CornerRadii.EMPTY, Insets.EMPTY)));
        text.setFill(Color.YELLOW);
    }

    public void setHighlightSquares(String[] highlightSquares){
        if(activeScene.compareTo("Engine.Chessboard") == 0) {
            ((ChessboardPane)myScenes.get(activeScene).getRoot()).changeActive(highlightSquares);
        }
    }

    public void setBoardState(String[][] boardState){
        if(activeScene.compareTo("Engine.Chessboard") == 0) {
            ((ChessboardPane)myScenes.get(activeScene).getRoot()).updateBoardPieces(boardState);
        }
    }

    public void closeWindow(){
        Platform.exit();
    }
}
