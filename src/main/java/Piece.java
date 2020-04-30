import javafx.util.Pair;

import java.util.HashSet;
import java.util.Set;

abstract class Piece {

    Pair<Integer, Integer> square;

    public static String pieceName;
    String pieceColor;

    public final String imagePathWhite;
    public final String imagePathBlack;

    public Piece(Pair<Integer, Integer> startingSquare, String color, String whitePath, String blackPath) {
        square = startingSquare;
        pieceColor = color;
        pieceName = this.getClass().getName();
        imagePathBlack = blackPath;
        imagePathWhite = whitePath;
    }

    public Pair<Integer, Integer> getSquare(){
        return square;
    }

    public Set<Pair<Integer, Integer>> getLegalMoves(Chessboard myChessboard){
        Set<Pair<Integer, Integer>> legalMoves = new HashSet<>();
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                Pair<Integer, Integer> tempCoord = new Pair<>(i, j);
                if(isMoveLegal(square, tempCoord, myChessboard)){
                    legalMoves.add(tempCoord);
                }
            }
        }
        return legalMoves;
    }

    public void updateSquare(Pair<Integer, Integer> newSquare){
        square = newSquare;
    }

    public String getPieceIdentifierName(){
        return pieceName + "_" + pieceColor;
    }

    abstract public boolean isMoveLegal(Pair<Integer, Integer> start, Pair<Integer, Integer> end, Chessboard myChessboard);
}
