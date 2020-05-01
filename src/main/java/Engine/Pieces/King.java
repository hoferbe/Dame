package Engine.Pieces;

import Engine.Chessboard;
import javafx.util.Pair;

public class King extends Piece {

    public King(String color) {
        super(color, "file:src/main/resources/Chess_klt60.png", "file:src/main/resources/Chess_kdt60.png");
        canCastle = true;
    }

    @Override
    public boolean isMoveLegal(Pair<Integer, Integer> start, Pair<Integer, Integer> end, Chessboard myChessboard) {
        return isMoveable(start, end, myChessboard) && !myEnginge.checkCheck(this.pieceColor, new Chessboard(myChessboard, start, end));
    }

    @Override
    public boolean isMoveable(Pair<Integer, Integer> start, Pair<Integer, Integer> end, Chessboard myChessboard) {
        if (myChessboard.getPiece(end) != null && sameColor(myChessboard.getPiece(end))) return false;
        if (Math.abs(start.getKey() - end.getKey()) <= 1
                && Math.abs(start.getValue() - end.getValue()) <= 1
                && Math.abs(start.getKey() - end.getKey()) + Math.abs(start.getValue() - end.getValue()) != 0)
            return true;
        else return castelCheck(start, end, myChessboard);
    }

    private boolean castelCheck(Pair<Integer, Integer> start, Pair<Integer, Integer> end, Chessboard myChessboard) {
        int startX = start.getKey();
        int startY = start.getValue();
        int endX = end.getKey();
        int endY = end.getValue();

        if (!canCastle) return false;
        if (startY != endY) return false;
        if (Math.abs(startX - endX) != 2) return false;

        //Check if castling to left
        if (endX - startX == -2) {
            //Check if rook is still castable
            if (myChessboard.getPiece(new Pair<>(0, startY)) == null || !myChessboard.getPiece(new Pair<>(0, startY)).isCanCastle()) return false;

            //Check that there is no piece in between
            if (
                    myChessboard.getPiece(new Pair<>(1, startY)) != null
                            || myChessboard.getPiece(new Pair<>(2, startY)) != null
                            || myChessboard.getPiece(new Pair<>(3, startY)) != null
            ) return false;

            //King can't castle out of, into, or through check
            if (
                    myEnginge.checkCheck(this.pieceColor, myChessboard)
                            || myEnginge.checkCheck(this.pieceColor, new Chessboard(myChessboard, start, new Pair<>(2, startY)))
                            || myEnginge.checkCheck(this.pieceColor, new Chessboard(myChessboard, start, new Pair<>(3, startY)))
            ) return false;
        } else {
            //Castling to right
            //Check if rook is still castable
            if (myChessboard.getPiece(new Pair<>(7, startY)) == null || !myChessboard.getPiece(new Pair<>(7, startY)).isCanCastle()) return false;

            //Check that there is no piece in between
            if (
                    myChessboard.getPiece(new Pair<>(6, startY)) != null
                            || myChessboard.getPiece(new Pair<>(5, startY)) != null
            ) return false;

            //King can't castle out of, into, or through check
            if (
                    myEnginge.checkCheck(this.pieceColor, myChessboard)
                            || myEnginge.checkCheck(this.pieceColor, new Chessboard(myChessboard, start, new Pair<>(5, startY)))
                            || myEnginge.checkCheck(this.pieceColor, new Chessboard(myChessboard, start, new Pair<>(6, startY)))
            ) return false;
        }

        tempSpecialMove = "castle_" + (end.getKey()==2?0:7) + "_" + (end.getKey()==2?3:5);
        return true;
    }
}
