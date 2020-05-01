package Engine.Pieces;

import Engine.Chessboard;
import javafx.util.Pair;

public class Queen extends Piece {
    public Queen(String color) {
        super(color, "file:src/main/resources/Chess_qlt60.png", "file:src/main/resources/Chess_qdt60.png");
    }

    @Override
    public boolean isMoveLegal(Pair<Integer, Integer> start, Pair<Integer, Integer> end, Chessboard myChessboard) {
        return isMoveable(start, end, myChessboard) && !myEnginge.checkCheck(this.pieceColor, new Chessboard(myChessboard, start, end));
    }

    @Override
    public boolean isMoveable(Pair<Integer, Integer> start, Pair<Integer, Integer> end, Chessboard myChessboard) {

        //Checking that the target square is either empty or enemy piece
        if (myChessboard.getPiece(end) != null && sameColor(myChessboard.getPiece(end))) return false;

        boolean movingDiagonal = true;

        //Check if the move is a diagonal. Otherwise set movingDiagonal to false
        if (Math.abs(start.getKey() - end.getKey()) != Math.abs(start.getValue() - end.getValue()))
            movingDiagonal = false;

        //Check that the Movement is in horizontal/vertical
        if (!movingDiagonal && !start.getKey().equals(end.getKey()) && !start.getValue().equals(end.getValue())) return false;

        if (movingDiagonal) {
            //Checking if the squares in between are empty
            int changeInX;
            int changeInY;

            if ((end.getKey() - start.getKey()) > 0) changeInX = 1;
            else changeInX = -1;

            if ((end.getValue() - start.getValue()) > 0) changeInY = 1;
            else changeInY = -1;

            for (int i = start.getKey() + changeInX, j = start.getValue() + changeInY; changeInX == 1 ? i < end.getKey() : i > end.getKey(); i += changeInX, j += changeInY) {
                if (myChessboard.getPiece(new Pair<>(i, j)) != null) return false;
            }
        } else {
            //Check that path is free. Only one loop will be used as for the other the condition is never met
            for (int i = Math.min(start.getKey(), end.getKey()) + 1; i < Math.max(start.getKey(), end.getKey()); i++) {
                if (myChessboard.getPiece(new Pair<>(i, start.getValue())) != null) return false;
            }
            for (int i = Math.min(start.getValue(), end.getValue()) + 1; i < Math.max(start.getValue(), end.getValue()); i++) {
                if (myChessboard.getPiece(new Pair<>(start.getKey(), i)) != null) return false;
            }
        }
        return true;
    }
}
