package DameEngine;

import DameEngine.Pieces.Piece;

import java.util.Arrays;

public class Board {
    private final Piece[][] BoardState;

    public Board(){
        BoardState = new Piece[8][8];
    }

    //Method to place a piece on the board
    public void placePiece(Coordinates coordinates, Piece piece){
        BoardState[coordinates.getX()][coordinates.getY()] = piece;
    }

    //method to move a piece
    public void movePiece(Coordinates start, Coordinates end){
        BoardState[end.getX()][end.getY()] = BoardState[start.getX()][start.getY()];
        BoardState[start.getX()][start.getY()] = null;
    }

    //method to remove a piece
    public void removePiece(Coordinates coordinates){
        BoardState[coordinates.getX()][coordinates.getY()] = null;
    }

    //method to get a piece
    public Piece getPiece(Coordinates coordinates){
        return BoardState[coordinates.getX()][coordinates.getY()];
    }

    @Override
    public String toString() {
        return "Board{" +
                "BoardState=" + Arrays.toString(BoardState) +
                '}';
    }

    public String[][] getStringBoard(){
        String[][] stringBoard = new String[8][8];

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if(BoardState[i][j] != null) stringBoard[i][j] = BoardState[i][j].getPieceIdentifierName();
            }
        }

        return  stringBoard;
    }
}
