import javafx.util.Pair;

public class Coordinates {

    public static Pair<Integer, Integer> getGUICoordinates(String chessCoordinates){
        chessCoordinates = chessCoordinates.toLowerCase();
        char xChar = chessCoordinates.charAt(0);
        int xCoordinate = xChar - 'a';
        System.out.println(xCoordinate);

        return new Pair<>(xCoordinate, 0);
    }
}
