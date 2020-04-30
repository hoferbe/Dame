import javafx.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class GameEngine {

    Chessboard myChessboard;

    private Set<Class<? extends Piece>> availablePieces;

    public final Queue<String> engineEvents = new LinkedList<>();
    private static volatile boolean running;

    private final Queue<String> controllerEvents;
    public static Queue<String> eventTemp;

    private Piece activePiece = null;

    public GameEngine() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Set<String> availablePiecesNames = new HashSet<>(Arrays.asList(
                "TestPiece"
        ));
        myChessboard = new Chessboard();
        registerPieces(availablePiecesNames);
        Piece test = new TestPiece(new Pair<>(5, 5), "white");
        myChessboard.placePiece(new Pair<>(5, 5), test);

        GameController.myEngine = this;

        controllerEvents = eventTemp;

        running = true;
        run();
    }

    private void registerPieces(Set<String> availablePiecesNames) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        for (String name : availablePiecesNames) {
            Class<? extends Piece> pieceClass = (Class<? extends Piece>) Class.forName(name);
            Piece placeHolder = pieceClass.getDeclaredConstructor(new Class[]{Pair.class, String.class}).newInstance(new Pair<>(0, 0), "white");
            ChessboardPane.imagePaths.put(name + "_white", placeHolder.imagePathWhite);
            ChessboardPane.imagePaths.put(name + "_black", placeHolder.imagePathBlack);
        }
    }

    public void run() {
        try{
            while(!Thread.currentThread().isInterrupted() && running){
                synchronized (engineEvents) {
                    if (engineEvents.isEmpty()) {
                        //is there's no Event, wait
                        engineEvents.wait();
                    }
                    while (!engineEvents.isEmpty()) {
                        //handle events until finished
                        handleEvent(engineEvents.remove());
                    }
                }
            }
        }catch(InterruptedException ignore){

        }
    }

    private void handleEvent(String eventName){
        eventName = eventName.toLowerCase();
        String[] eventParts = eventName.split("_"); //[0] origin, [1] event, [n>1] parameters
        System.out.println("Event occured: " + eventName);
        switch (eventParts[0]) {
            case "chessboard":
                if(eventParts[1].compareTo("clicked") == 0){
                    squareClicked(new Pair<>(Integer.parseInt(eventParts[2]), Integer.parseInt(eventParts[3])));
                }
                break;

            case "controller":
                if(eventParts[1].compareTo("close") == 0){
                    running = false;
                }
        }
    }

    public void squareClicked(Pair<Integer, Integer> square) {
        String activeSquareEvent = "GameEngine_";
        if(activePiece == null) {
            Set<Pair<Integer, Integer>> highlights;
            activePiece = myChessboard.getPiece(square);
            if (activePiece != null) {
                highlights = activePiece.getLegalMoves(myChessboard);
                ArrayList<String> highlights2 = new ArrayList<>();
                highlights2.add("red");
                highlights2.add(square.getKey().toString());
                highlights2.add(square.getValue().toString());
                for (Pair<Integer, Integer> current : highlights) {
                    highlights2.add("green");
                    highlights2.add(current.getKey().toString());
                    highlights2.add((current.getValue().toString()));
                }
                activeSquareEvent += String.join("_", highlights2);
            }
        }
        else{
            if(activePiece.isMoveLegal(activePiece.getSquare(), square, myChessboard)){
                myChessboard.movePiece(activePiece.getSquare(), square);
                activePiece.updateSquare(square);
            }
            activePiece = null;
        }

        synchronized (controllerEvents) {
            controllerEvents.add(activeSquareEvent);
            controllerEvents.notifyAll();
        }
    }

    public String[][] getStringBoard() {
        return myChessboard.getStringBoard();
    }

    public void createEvent(String event){
        synchronized (engineEvents){
            engineEvents.add(event);
            engineEvents.notifyAll();
        }
    }

}
