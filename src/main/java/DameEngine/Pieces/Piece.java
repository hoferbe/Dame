package DameEngine.Pieces;

import DameEngine.Board;
import DameEngine.Coordinates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class Piece {
    protected final String pieceName;
    protected final String pieceColor;

    protected final String imagePathWhite;
    protected final String imagePathBlack;

    Piece(String pieceColor, String imagePathWhite, String imagePathBlack){
        pieceName = this.getClass().getName();
        this.pieceColor = pieceColor;
        this.imagePathWhite = imagePathWhite;
        this.imagePathBlack = imagePathBlack;
    }

    public String getPieceColor() {
        return pieceColor;
    }

    public Set<Coordinates> getLegalMoves(Coordinates coordinates, Board board){
        Set<Coordinates> legalMoves = new HashSet<>();
        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                Coordinates target = new Coordinates(x, y);
                if(isMoveLegal(coordinates, target, board)) legalMoves.add(target);
            }
        }
        return legalMoves;
    }

    public Set<Coordinates> getLegalTakes(Coordinates coordinates, Board board){
        Set<Coordinates> legalMoves = new HashSet<>();
        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                Coordinates target = new Coordinates(x, y);
                if(isTakeLegal(coordinates, target, board)) legalMoves.add(target);
            }
        }
        return legalMoves;
    }

    public abstract boolean isMoveLegal(Coordinates start, Coordinates end, Board board);
    public abstract boolean isTakeLegal(Coordinates start, Coordinates end, Board board);

    public boolean canMove(Coordinates coordinates, Board board){
        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                Coordinates target = new Coordinates(x, y);
                if(isMoveLegal(coordinates, target, board)) return true;
            }
        }
        return false;
    }

    public boolean canTake(Coordinates coordinates, Board board){
        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                Coordinates target = new Coordinates(x, y);
                if(isMoveLegal(coordinates, target, board) && moveType(coordinates, new Coordinates(x, y)).split("_")[0].equals("taking")) return true;
            }
        }
        return false;
    }

    public abstract String moveType(Coordinates start, Coordinates end);

    public String getPieceIdentifierName(){
        return pieceName + "_" + pieceColor;
    }

    public String getImagePathBlack() {
        return imagePathBlack;
    }

    public String getImagePathWhite() {
        return imagePathWhite;
    }
}
