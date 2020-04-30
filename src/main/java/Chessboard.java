import javafx.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class Chessboard {

    Piece[][] chessboardState = new Piece[8][8];

    public Chessboard(){

    }

    public void placePiece(Pair<Integer, Integer> coordinates, Piece piece){
        chessboardState[coordinates.getKey()][coordinates.getValue()] = piece;
    }

    public void movePiece(Pair<Integer, Integer> start, Pair<Integer, Integer> end){
        chessboardState[end.getKey()][end.getValue()] = chessboardState[start.getKey()][start.getValue()];
        chessboardState[start.getKey()][start.getValue()] = null;
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
