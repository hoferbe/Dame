import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.Scene;
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
        myScenes.put("Chessboard", new Scene(new ChessboardPane()));
    }

    @Override
    public void start(Stage primaryStage){
        GUIController.myWindow = this;

        fillMap();
        myStage = primaryStage;

        activeScene = "Menu";
        myStage.setTitle("Chess");
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

    public void setHighlightSquares(String[] highlightSquares){
        if(activeScene.compareTo("Chessboard") == 0) {
            ((ChessboardPane)myScenes.get(activeScene).getRoot()).changeActive(highlightSquares);
        }
    }

    public void setBoardState(String[][] boardState){
        if(activeScene.compareTo("Chessboard") == 0) {
            ((ChessboardPane)myScenes.get(activeScene).getRoot()).updateBoardPieces(boardState);
        }
    }

    public void closeWindow(){
        Platform.exit();
    }
}
