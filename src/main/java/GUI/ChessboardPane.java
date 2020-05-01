package GUI;

import GUI.EventHandler.ChessBoardClickedEventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.util.HashMap;
import java.util.Map;

public class ChessboardPane extends AnchorPane {

    double chessboardWidth;
    double chessboardHeight;
    double squareWidth;
    double squareHeight;

    public static final Map<String, String> imagePaths = new HashMap<>();
    String[][] boardState = new String[8][8];

    AnchorPane squarePane;
    AnchorPane piecesPane;


    ChessboardPane() {
        Image chessboardImage = new Image("file:src/main/resources/chessboard.png", 800, 800, false, false);
        ImageView chessboardView = new ImageView(chessboardImage);
        chessboardWidth = chessboardImage.getWidth();
        chessboardHeight = chessboardImage.getHeight();
        squareWidth = chessboardWidth / 8;
        squareHeight = chessboardHeight / 8;

        this.setOnMouseClicked(new ChessBoardClickedEventHandler() {
            @Override
            public void handle(MouseEvent event) {
                if (chessboardView.contains(event.getX(), event.getY())) {
                    int x = (int) Math.floor(event.getX() / chessboardWidth * 8);
                    int y = (int) Math.floor(event.getY() / chessboardHeight * 8);
                    System.out.println("x: " + x + "\ny: " + y);
                    send("Chessboard_Clicked_" + x + "_" + y);
                }
            }
        });
        AnchorPane.setTopAnchor(chessboardView, 0.0);
        AnchorPane.setLeftAnchor(chessboardView, 0.0);

        squarePane = new AnchorPane();
        AnchorPane.setLeftAnchor(squarePane, 0.0);
        AnchorPane.setTopAnchor(squarePane, 0.0);

        piecesPane = new AnchorPane();
        AnchorPane.setLeftAnchor(piecesPane, 0.0);
        AnchorPane.setTopAnchor(piecesPane, 0.0);


        this.getChildren().addAll(chessboardView, squarePane, piecesPane);
    }

    public void changeActive(String[] activeSquares) {
        resetActive();

        if (activeSquares != null) {
            for (int i = 0; i < activeSquares.length; i += 3) {
                String color = activeSquares[i];
                int x = Integer.parseInt(activeSquares[i + 1]);
                int y = Integer.parseInt(activeSquares[i + 2]);

                Rectangle rect = new Rectangle(squareWidth, squareHeight);
                rect.setFill(Color.rgb(0, 0, 0, 0));
                if (color.compareTo("green") == 0) {
                    rect.setStroke(Color.rgb(0, 255, 0, 0.8));
                } else if (color.compareTo("red") == 0) {
                    rect.setStroke(Color.rgb(255, 0, 0, 0.8));
                } else {
                    rect.setStroke(Color.rgb(100, 100, 100, 0.5));
                }
                rect.setStrokeType(StrokeType.INSIDE);
                rect.setStrokeWidth(5);

                AnchorPane.setLeftAnchor(rect, x * squareWidth);
                AnchorPane.setTopAnchor(rect, y * squareHeight);

                squarePane.getChildren().addAll(rect);
            }
        }
    }

    public void resetActive() {
        squarePane.getChildren().clear();
    }


    public void updateBoardPieces(String[][] newBoardState) {
        boardState = newBoardState;

        resetBoardPieces();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (boardState[i][j] != null) {
                    ImageView pieceView = new ImageView(new Image(imagePaths.get(boardState[i][j]), 100, 100, false, false));

                    AnchorPane.setLeftAnchor(pieceView, i * squareWidth);
                    AnchorPane.setTopAnchor(pieceView, j * squareHeight);

                    piecesPane.getChildren().addAll(pieceView);
                }
            }
        }
    }

    private void resetBoardPieces() {
        piecesPane.getChildren().clear();
    }
}
