package DameEngine;

import DameEngine.Pieces.Piece;

public class Board {
    private final Piece[][] BoardState;

    public Board(){
        BoardState = new Piece[8][8];
    }

    public void placePiece(Coordinates coordinates, Piece piece){
        BoardState[coordinates.getX()][coordinates.getY()] = piece;
    }

    public void movePiece(Coordinates start, Coordinates end){
        BoardState[end.getX()][end.getY()] = BoardState[start.getX()][start.getY()];
        BoardState[start.getX()][start.getY()] = null;
    }

    public Piece getPiece(Coordinates coordinates){
        return BoardState[coordinates.getX()][coordinates.getY()];
    }
}
