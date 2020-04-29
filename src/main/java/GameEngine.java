import javafx.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GameEngine {

    Chessboard myChessboard;

    private Set<Class<? extends Piece>> availablePieces;

    public GameEngine() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Set<String> availablePiecesNames = new HashSet<>(Arrays.asList(
                "TestPiece"
        ));
            myChessboard = new Chessboard();
            registerPieces(availablePiecesNames);
            Piece test = new TestPiece(new Pair<>(5, 5), "white");
            myChessboard.placePiece(new Pair<>(5, 5), test);
    }

    private void registerPieces(Set<String> availablePiecesNames) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        for (String name : availablePiecesNames){
            Class<? extends Piece> pieceClass = (Class<? extends Piece>) Class.forName(name);
            Piece placeHolder = pieceClass.getDeclaredConstructor(new Class[]{Pair.class, String.class}).newInstance(new Pair<>(0, 0), "white");
            ChessboardPane.imagePaths.put(name + "_white", placeHolder.imagePathWhite);
            ChessboardPane.imagePaths.put(name + "_black", placeHolder.imagePathBlack);
        }
    }

    public String[] squareClicked(Pair<Integer, Integer> square){
        Set<Pair<Integer, Integer>> highlights;
        Piece clickedPiece = myChessboard.getPiece(square);
        String[] stringArray = null;
        if(clickedPiece != null){
            highlights = clickedPiece.getLegalMoves(myChessboard);
            ArrayList<String> highlights2 = new ArrayList<String>();
            highlights2.add("red");
            highlights2.add(square.getKey().toString());
            highlights2.add(square.getValue().toString());
            for (Pair<Integer, Integer> current : highlights){
                highlights2.add("green");
                highlights2.add(current.getKey().toString());
                highlights2.add((current.getValue().toString()));
            }
            stringArray = new String[highlights2.size()];
            highlights2.toArray(stringArray);
        }

        return stringArray;
    }

    public String[][] getStringBoard(){
        return myChessboard.getStringBoard();
    }

}
