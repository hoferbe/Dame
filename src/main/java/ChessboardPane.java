import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class ChessboardPane extends BorderPane {


    ChessboardPane(){
        Image chessboardImage = new Image("file:src/main/resources/Black-board-4.jpg", 800, 0, true, false);
        ImageView chessboardView = new ImageView(chessboardImage);
        chessboardView.setOnMouseClicked(new ChessBoardClickedEventHandler() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(event);
            }
        });
        this.setCenter(chessboardView);
    }
}
