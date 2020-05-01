package Engine.Pieces;

import Engine.Chessboard;
import javafx.util.Pair;

public class Bishop extends Piece{
    public Bishop(String color) {
        super(color, "file:src/main/resources/Chess_blt60.png", "file:src/main/resources/Chess_bdt60.png");
    }

    @Override
    public boolean isMoveLegal(Pair<Integer, Integer> start, Pair<Integer, Integer> end, Chessboard myChessboard) {
        return isMoveable(start, end, myChessboard) && !myEnginge.checkCheck(this.pieceColor, new Chessboard(myChessboard, start, end));
    }

    @Override
    public boolean isMoveable(Pair<Integer, Integer> start, Pair<Integer, Integer> end, Chessboard myChessboard) {
        //Check if the move is a diagonal
        if(Math.abs(start.getKey() - end.getKey()) != Math.abs(start.getValue() - end.getValue())) return false;

        //Checking that the target square is either empty or enemy piece
        if(myChessboard.getPiece(end) != null && sameColor(myChessboard.getPiece(end))) return false;

        //Checking if the squares in between are empty
        int changeInX;
        int changeInY;

        if((end.getKey() - start.getKey()) > 0) changeInX = 1;
        else changeInX = -1;

        if((end.getValue() - start.getValue()) > 0) changeInY = 1;
        else changeInY = -1;

        for(int i = start.getKey() + changeInX, j = start.getValue() + changeInY; changeInX == 1? i < end.getKey(): i > end.getKey(); i+=changeInX, j+=changeInY){
            if (myChessboard.getPiece(new Pair<>(i, j)) != null) return false;
        }


        return true;
    }
}
