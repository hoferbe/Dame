package Engine.Pieces;

import Engine.Chessboard;
import javafx.util.Pair;

public class Pawn extends Piece {

    public Pawn(String color) {
        super(color, "file:src/main/resources/Chess_plt60.png", "file:src/main/resources/Chess_pdt60.png");
    }

    @Override
    public boolean isMoveLegal(Pair<Integer, Integer> start, Pair<Integer, Integer> end, Chessboard myChessboard) {
        return isMoveable(start, end, myChessboard) && !myEnginge.checkCheck(this.pieceColor, new Chessboard(myChessboard, start, end));
    }

    @Override
    public boolean isMoveable(Pair<Integer, Integer> start, Pair<Integer, Integer> end, Chessboard myChessboard) {
        if (start.getKey().equals(end.getKey())) {
            if (this.pieceColor.equals("white")) {
                if ((start.getValue() != 6 || start.getValue() != end.getValue() + 2) && start.getValue() != end.getValue() + 1)
                    return false;
            } else {
                if ((start.getValue() != 1 || start.getValue() != end.getValue() - 2) && start.getValue() != end.getValue() - 1)
                    return false;
            }
            if (myChessboard.getPiece(end) != null) return false;
            return true;
        }
        else if(Math.abs(start.getKey() - end.getKey()) == 1){
            if (this.pieceColor.equals("white")) {
                if (start.getValue() != end.getValue() + 1)
                    return false;
            } else {
                if (start.getValue() != end.getValue() - 1)
                    return false;
            }
            if (myChessboard.getPiece(end) == null || myChessboard.getPiece(end).getPieceColor().equals(this.pieceColor)) return false;
            return true;
        }
        return false;
    }
}
