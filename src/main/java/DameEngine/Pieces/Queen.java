package DameEngine.Pieces;

import DameEngine.Board;
import DameEngine.Coordinates;

public class Queen extends Piece{
    public Queen(String color) {
        super(color, "file:src/main/resources/circle.png", "file:src/main/resources/BlackCircle.png");
    }

    @Override
    public boolean isMoveLegal(Coordinates start, Coordinates end, Board board) {
        if (board.getPiece(start) == null || board.getPiece(end) != null) return false;

        if (pieceColor.equals("white")) {
            if (Math.abs(end.getX() - start.getX()) == 1 && end.getY() - start.getY() == -1) return true;
            else if (Math.abs(end.getX() - start.getX()) == 2 && end.getY() - start.getY() == -2
                    && board.getPiece(new Coordinates((start.getX() + end.getX()) / 2, (start.getY() + end.getY()) / 2)) != null
                    && !board.getPiece(new Coordinates((start.getX() + end.getX()) / 2, (start.getY() + end.getY()) / 2)).getPieceColor().equals(pieceColor)){
                return true;
            }
        } else {
            if (Math.abs(end.getX() - start.getX()) == 1 && end.getY() - start.getY() == 1) return true;
            else if (Math.abs(end.getX() - start.getX()) == 2 && end.getY() - start.getY() == 2
                    && board.getPiece(new Coordinates((start.getX() + end.getX()) / 2, (start.getY() + end.getY()) / 2)) != null
                    && !board.getPiece(new Coordinates((start.getX() + end.getX()) / 2, (start.getY() + end.getY()) / 2)).getPieceColor().equals(pieceColor)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isTakeLegal(Coordinates start, Coordinates end, Board board) {
        if (board.getPiece(start) == null || board.getPiece(end) != null) return false;

        if (pieceColor.equals("white")) {
            if (Math.abs(end.getX() - start.getX()) == 2 && end.getY() - start.getY() == -2
                    && board.getPiece(new Coordinates((start.getX() + end.getX()) / 2, (start.getY() + end.getY()) / 2)) != null
                    && !board.getPiece(new Coordinates((start.getX() + end.getX()) / 2, (start.getY() + end.getY()) / 2)).getPieceColor().equals(pieceColor)){
                return true;
            }
        } else {
            if (Math.abs(end.getX() - start.getX()) == 2 && end.getY() - start.getY() == 2
                    && board.getPiece(new Coordinates((start.getX() + end.getX()) / 2, (start.getY() + end.getY()) / 2)) != null
                    && !board.getPiece(new Coordinates((start.getX() + end.getX()) / 2, (start.getY() + end.getY()) / 2)).getPieceColor().equals(pieceColor)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String moveType(Coordinates start, Coordinates end) {
        String moveType = "";
        if (Math.abs(end.getY()-start.getY()) == 2) moveType += "taking";
        moveType += "_";
        if(pieceColor.equals("white") && end.getY() == 0
                || pieceColor.equals("black") && end.getY() == 7) moveType += "promotion";

        return moveType;
    }
}
