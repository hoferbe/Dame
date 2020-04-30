package Engine;

import javafx.util.Pair;

public class Coordinates {

    public static Pair<Integer, Integer> getGUICoordinates(String chessCoordinates){
        chessCoordinates = chessCoordinates.toLowerCase();
        char xChar = chessCoordinates.charAt(0);
        int xCoordinate = xChar - 'a';

        int yCoordinate = 8 - Character.getNumericValue(chessCoordinates.charAt(1));

        return new Pair<>(xCoordinate, yCoordinate);
    }
}
