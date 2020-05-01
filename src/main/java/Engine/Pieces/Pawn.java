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
        tempSpecialMove = "none";
        int startX = start.getKey();
        int startY = start.getValue();

        int endX = end.getKey();
        int endY = end.getValue();

        //Check for a straight move, can't take then
        if (startX == endX) {
            if (this.pieceColor.equals("white")) {
                //Check if it's only one square move or 2 in case of starting line
                if ((startY != 6 || startY != endY + 2) && startY != endY + 1)
                    return false;
            } else {
                //Check if it's only one square move or 2 in case of starting line
                if ((startY != 1 || startY != endY - 2) && startY != endY - 1)
                    return false;
            }
            //Can't take a piece moving that way
            if (myChessboard.getPiece(end) != null) return false;
            //turn on that it can be taken en passen, if it is a double move
            if (Math.abs(startY - endY) == 2) this.canBeEnPassantTaken = true;
            if(pieceColor.equals("white") && endY == 0 || pieceColor.equals("black") && endY == 7) tempSpecialMove = "promotion";
            return true;
        }
        //Check if diagonal move is only one square to the side
        else if(Math.abs(startX - endX) == 1){
            //Check if diagonal move is only one square forward
            if (this.pieceColor.equals("white")) {
                if (startY != endY + 1)
                    return false;
            } else {
                //Check if diagonal move is only one square forward
                if (startY != endY - 1)
                    return false;
            }

            if ((myChessboard.getPiece(end) == null || myChessboard.getPiece(end).getPieceColor().equals(this.pieceColor))
                    && (myChessboard.getPiece(new Pair<>(endX, startY)) == null || myChessboard.getPiece(new Pair<>(endX, startY)).getPieceColor().equals(this.pieceColor) || !myChessboard.getPiece(new Pair<>(endX, startY)).canBeEnPassantTaken)) return false;
            if(myChessboard.getPiece(end) == null) tempSpecialMove = "enpassant_" + endX + "_" + startY;
            else if(pieceColor.equals("white") && endY == 0 || pieceColor.equals("black") && endY == 7) tempSpecialMove = "promotion";
            return true;
        }
        return false;
    }
}
