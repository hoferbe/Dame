package Engine.Pieces;

import Engine.Chessboard;
import javafx.util.Pair;

public class Knight extends Piece{

    public Knight(String color) {
        super(color, "file:src/main/resources/Chess_nlt60.png", "file:src/main/resources/Chess_ndt60.png");
    }


    @Override
    public boolean isMoveLegal(Pair<Integer, Integer> start, Pair<Integer, Integer> end, Chessboard myChessboard) {
        return isMoveable(start, end, myChessboard) && !myEnginge.checkCheck(this.pieceColor, new Chessboard(myChessboard, start, end));
    }

    @Override
    public boolean isMoveable(Pair<Integer, Integer> start, Pair<Integer, Integer> end, Chessboard myChessboard) {
        if(Math.abs(start.getKey() - end.getKey()) * Math.abs(start.getValue() - end.getValue()) != 2) return false;

        //Checking that the target square is either empty or enemy piece
        if(myChessboard.getPiece(end) != null && sameColor(myChessboard.getPiece(end))) return false;

        return true;
    }
}
