package Engine.Pieces;

import Engine.Chessboard;
import javafx.util.Pair;

public class Rook extends Piece{
    public Rook(String color) {
        super(color, "file:src/main/resources/Chess_rlt60.png", "file:src/main/resources/Chess_rdt60.png");
    }

    @Override
    public boolean isMoveLegal(Pair<Integer, Integer> start, Pair<Integer, Integer> end, Chessboard myChessboard) {
        return isMoveable(start, end, myChessboard) && !myEnginge.checkCheck(this.pieceColor, new Chessboard(myChessboard, start, end));
    }

    @Override
    public boolean isMoveable(Pair<Integer, Integer> start, Pair<Integer, Integer> end, Chessboard myChessboard) {
        //Check that rook is moving and only moving in one direction
        if(!start.getKey().equals(end.getKey()) && !start.getValue().equals(end.getValue())) return false;

        //Checking that the target square is either empty or enemy piece
        if(myChessboard.getPiece(end) != null && sameColor(myChessboard.getPiece(end))) return false;

        //Check that path is free. Only one loop will be used as for the other the condition is never met
        for(int i = Math.min(start.getKey(), end.getKey()) + 1; i < Math.max(start.getKey(), end.getKey()); i++){
            if (myChessboard.getPiece(new Pair<>(i, start.getValue())) != null) return false;
        }
        for(int i = Math.min(start.getValue(), end.getValue()) + 1; i < Math.max(start.getValue(), end.getValue()); i++){
            if (myChessboard.getPiece(new Pair<>(start.getKey(), i)) != null) return false;
        }
        return true;
    }
}
