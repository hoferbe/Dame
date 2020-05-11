package DameEngine.Pieces;

import DameEngine.Coordinates;
import Engine.Chessboard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class Piece {
    private final String color;

    Piece (String color){
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public ArrayList<Coordinates> getLegalMoves(Coordinates position, Chessboard chessboard){
        ArrayList<Coordinates> legalMoves = new ArrayList<>();
        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                Coordinates target = new Coordinates(x, y);
                if(isMoveLegal(position, target, chessboard)) legalMoves.add(target);
            }
        }
        return legalMoves;
    }

    abstract boolean isMoveLegal(Coordinates start, Coordinates end, Chessboard chessboard);
}
