package Engine.Pieces;

import Engine.Chessboard;
import Engine.GameEngine;
import javafx.util.Pair;

import java.util.HashSet;
import java.util.Set;

public abstract class Piece {

    public String pieceName;
    protected final String pieceColor;

    protected boolean canCastle;
    protected boolean canBeEnPassantTaken;

    public final String imagePathWhite;
    public final String imagePathBlack;

    protected String tempSpecialMove;
    protected String specialMove;

    static public GameEngine myEnginge;

    public Piece(String color, String whitePath, String blackPath) {
        pieceColor = color;
        pieceName = this.getClass().getName();
        imagePathBlack = blackPath;
        imagePathWhite = whitePath;

        canCastle = false;
        canBeEnPassantTaken = false;
        tempSpecialMove = "none";
        specialMove = "none";
    }

    public Set<Pair<Integer, Integer>> getLegalMoves(Pair<Integer, Integer> position, Chessboard myChessboard){
        Set<Pair<Integer, Integer>> legalMoves = new HashSet<>();
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                Pair<Integer, Integer> tempCoord = new Pair<>(i, j);
                if(isMoveLegal(position, tempCoord, myChessboard)){
                    legalMoves.add(tempCoord);
                }
            }
        }
        return legalMoves;
    }

    public String getPieceIdentifierName(){
        return pieceName + "_" + pieceColor;
    }

    public String getPieceColor(){
        return pieceColor;
    }

    abstract public boolean isMoveLegal(Pair<Integer, Integer> start, Pair<Integer, Integer> end, Chessboard myChessboard);

    abstract public boolean isMoveable(Pair<Integer, Integer> start, Pair<Integer, Integer> end, Chessboard myChessboard);

    protected boolean sameColor(Piece target){
        return this.pieceColor.compareTo(target.pieceColor) == 0;
    }

    public boolean isCanCastle(){
        return canCastle;
    }

    public void canCastleFalse(){canCastle = false;}

    public void resetEnPassent(){canBeEnPassantTaken = false;}

    public String getSpecialMove(){
        String save = specialMove;
        specialMove = "none";
        tempSpecialMove = "none";
        return save;
    }

    public void writeSpecialMove(){
        specialMove = tempSpecialMove;
    }
}
