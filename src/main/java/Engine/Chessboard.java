package Engine;

import Engine.Pieces.Piece;
import groovy.transform.stc.PickAnyArgumentHint;
import javafx.scene.control.CheckBox;
import javafx.util.Pair;

public class Chessboard {

    private Piece[][] chessboardState = new Piece[8][8];


    private Pair<Integer, Integer> whiteKingSquare;
    private Pair<Integer, Integer> blackKingSquare;

    public Chessboard(){
    }

    public Chessboard(Chessboard copyBoard){
        this.chessboardState = copyBoard.chessboardState.clone();
        this.whiteKingSquare = copyBoard.whiteKingSquare;
        this.blackKingSquare = copyBoard.blackKingSquare;
    }

    public Chessboard(Chessboard copyBoard, Pair<Integer, Integer> start, Pair<Integer, Integer> end){
        //Need to iterate over the first array layers because otherwise the arrays would be referenced and changes would affect both.
        for (int i = 0; i < 8; i++){
            this.chessboardState[i] = copyBoard.chessboardState[i].clone();
        }
        this.whiteKingSquare = new Pair<>(copyBoard.whiteKingSquare.getKey(), copyBoard.whiteKingSquare.getValue());
        this.blackKingSquare = new Pair<>(copyBoard.blackKingSquare.getKey(), copyBoard.blackKingSquare.getValue());
        movePiece(start, end);
    }

    public void placePiece(Pair<Integer, Integer> coordinates, Piece piece){
        if (piece.getPieceIdentifierName().split("_")[0].compareTo("Engine.Pieces.King") == 0){
            if (piece.getPieceIdentifierName().split("_")[1].compareTo("white") == 0){
                whiteKingSquare = coordinates;
            }
            else blackKingSquare = coordinates;
        }
        chessboardState[coordinates.getKey()][coordinates.getValue()] = piece;
    }

    public void movePiece(Pair<Integer, Integer> start, Pair<Integer, Integer> end){
        if(getPiece(start).getPieceIdentifierName().split("_")[0].compareTo("Engine.Pieces.King")==0){
            getPiece(start);
            if (getPiece(start).getPieceIdentifierName().split("_")[1].compareTo("white") == 0) {
            whiteKingSquare = end;
            }
            else{
                blackKingSquare = end;
            }
        }
        chessboardState[end.getKey()][end.getValue()] = chessboardState[start.getKey()][start.getValue()];
        chessboardState[start.getKey()][start.getValue()] = null;
    }

    public void removePiece(Pair<Integer, Integer> coordinates){
        chessboardState[coordinates.getKey()][coordinates.getValue()] = null;
    }

    public Pair<Integer, Integer> getKingSquare(String color){
        if(color.compareTo("white") == 0) return whiteKingSquare;
        else return blackKingSquare;
    }

    public Piece getPiece(Pair<Integer, Integer> coordinates) {
        return chessboardState[coordinates.getKey()][coordinates.getValue()];
    }

    public String[][] getStringBoard(){
        String[][] stringBoard = new String[8][8];

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if(chessboardState[i][j] != null) stringBoard[i][j] = chessboardState[i][j].getPieceIdentifierName();
            }
        }

        return  stringBoard;
    }
}
