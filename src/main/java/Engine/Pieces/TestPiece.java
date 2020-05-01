package Engine.Pieces;

import Engine.Chessboard;
import Engine.Pieces.Piece;
import javafx.util.Pair;

public class TestPiece extends Piece {


    public TestPiece(String color) {
        super(color, "file:src/main/resources/circle.png", "file:src/main/resources/circle.png");
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
