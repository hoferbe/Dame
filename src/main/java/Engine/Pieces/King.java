package Engine.Pieces;

import Engine.Chessboard;
import javafx.util.Pair;

public class King extends Piece{

    public King(String color) {
        super(color, "file:src/main/resources/Chess_klt60.png", "file:src/main/resources/Chess_kdt60.png");
    }

    @Override
    public boolean isMoveLegal(Pair<Integer, Integer> start, Pair<Integer, Integer> end, Chessboard myChessboard) {
        return isMoveable(start, end, myChessboard) && !myEnginge.checkCheck(this.pieceColor, new Chessboard(myChessboard, start, end));
    }

    @Override
    public boolean isMoveable(Pair<Integer, Integer> start, Pair<Integer, Integer> end, Chessboard myChessboard) {
        if(myChessboard.getPiece(end) != null && sameColor(myChessboard.getPiece(end))) return false;
        return Math.abs(start.getKey() - end.getKey()) <= 1
                && Math.abs(start.getValue() - end.getValue()) <= 1
                && Math.abs(start.getKey() - end.getKey()) + Math.abs(start.getValue() - end.getValue()) != 0;
    }
}
