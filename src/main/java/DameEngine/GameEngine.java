package DameEngine;

import Controller.GUIController;
import Controller.GameController;
import DameEngine.Pieces.*;
import DameEngine.Pieces.Piece;
import GUI.ChessboardPane;
import javafx.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class GameEngine {

    //Currents state of the Engine
    public boolean ready = false;
    private boolean running = false;

    //List of the events needing to be handled by the Engine
    private final Queue<String> engineEvents = new LinkedList<>();

    //Events sent to the controller
    private final Queue<String> controllerEvents;
    public static Queue<String> eventTemp;

    //List to have all pieces
    private final Set<String> availablePiecesNames = new HashSet<>(Arrays.asList(
            "DameEngine.Pieces.Pawn"
    ));

    //Current State of the game
    private final Board myBoard;
    private String currentPlayer = "white";
    private Coordinates activeSquare = null;
    private GameState currentGameState = GameState.movable;
    Coordinates coordinatesOfPieceNeedsToTake = null;

    enum GameState {
        finished,
        movable,
        takable,
        hasToTake
    }

    public GameEngine() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        this(new String[][]{
                {"", "Pawn_black", "", "Pawn_black", "", "Pawn_black", "", "Pawn_black"},
                {"Pawn_black", "", "Pawn_black", "", "Pawn_black", "", "Pawn_black", ""},
                {"", "Pawn_black", "", "Pawn_black", "", "Pawn_black", "", "Pawn_black"},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"Pawn_white", "", "Pawn_white", "", "Pawn_white", "", "Pawn_white", ""},
                {"", "Pawn_white", "", "Pawn_white", "", "Pawn_white", "", "Pawn_white"},
                {"Pawn_white", "", "Pawn_white", "", "Pawn_white", "", "Pawn_white", ""},

        });
    }

    public GameEngine(String[][] boardState) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        myBoard = new Board();

        controllerEvents = eventTemp;

        registerPieces();
        fillBoard(boardState);

        GameController.myEngine = this;

        running = true;
        ready = true;

        run();
    }

    private void registerPieces() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        for (String name : availablePiecesNames) {
            Class<? extends Piece> pieceClass = (Class<? extends Piece>) Class.forName(name);
            Piece placeHolder = pieceClass.getDeclaredConstructor(new Class[]{String.class}).newInstance("white");
            ChessboardPane.imagePaths.put(name + "_white", placeHolder.getImagePathWhite());
            ChessboardPane.imagePaths.put(name + "_black", placeHolder.getImagePathBlack());
        }
    }

    private void fillBoard(String[][] boardState) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                switch (boardState[i][j].split("_")[0]) {
                    case "Pawn":
                        myBoard.placePiece(new Coordinates(j, i), new Pawn(boardState[i][j].split("_")[1]));
                        break;

                    case "":
                        break;

                    default:
                        break;
                }
            }
        }
    }

    private void run() {
        try {
            while (!Thread.currentThread().isInterrupted() && running) {
                synchronized (engineEvents) {
                    if (engineEvents.isEmpty()) {
                        //if there's no Event, wait
                        engineEvents.wait();
                    }
                    while (!engineEvents.isEmpty()) {
                        //handle events until finished
                        handleEvent(engineEvents.remove());
                    }
                }
            }
        } catch (InterruptedException ignore) {

        }
    }

    private void stopEngine() {
        running = false;
    }

    public String[][] getStringBoard() {
        return myBoard.getStringBoard();
    }

    public void createEvent(String eventName) {
        synchronized (engineEvents) {
            engineEvents.add(eventName);
            engineEvents.notifyAll();
        }
    }

    private void handleEvent(String eventName) {
        eventName = eventName.toLowerCase();
        String[] eventParts = eventName.split("_"); //[0] origin, [1] event, [n>1] parameters
        System.out.println("Event occured: " + eventName);
        switch (eventParts[0]) {
            case "chessboard":
                if (eventParts[1].equals("clicked")) {
                    squareClicked(new Coordinates(Integer.parseInt(eventParts[2]), Integer.parseInt(eventParts[3])));
                }
                break;

            case "controller":
                if (eventParts[1].equals("close")) {
                    stopEngine();
                }

        }

    }

    private void squareClicked(Coordinates coordinates) {
        String activeSquareEvent = "GameEngine_highlights_";

        //No square is active yet
        if (activeSquare == null) {
            activeSquareEvent += highlightSquare(coordinates);
        }
        //If a square is already active
        else{
            activeSquareEvent += checkMove(coordinates);
        }
        synchronized (controllerEvents) {
            controllerEvents.add(activeSquareEvent);
            controllerEvents.notifyAll();
        }
    }

    private String highlightSquare(Coordinates coordinates){
        String highlightedSquares = "";
        Set<Coordinates> legalMoves;
        //Get the piece which is on the square clicked
        Piece clickedPiece = myBoard.getPiece(coordinates);
        //Make square only active if there is a piece on the square and the clicked piece is of color as current player
        if(clickedPiece != null && clickedPiece.getPieceColor().equals(currentPlayer)){
            activeSquare = coordinates;

            if(currentGameState == GameState.takable || currentGameState == GameState.hasToTake) legalMoves = myBoard.getPiece(coordinates).getLegalTakes(coordinates, myBoard);
            else legalMoves = myBoard.getPiece(coordinates).getLegalMoves(coordinates, myBoard);

            ArrayList<String> highlights = new ArrayList<>();
            highlights.add("red");
            highlights.add(coordinates.getXs());
            highlights.add(coordinates.getYs());
            for (Coordinates current : legalMoves) {
                highlights.add("green");
                highlights.add(current.getXs());
                highlights.add(current.getYs());
            }
            highlightedSquares += String.join("_", highlights);
        }

        return highlightedSquares;
    }

    private String checkMove(Coordinates coordinates){
        boolean resetActive = true;
        String returnString = "";
        if((currentGameState == GameState.movable && myBoard.getPiece(activeSquare).isMoveLegal(activeSquare, coordinates, myBoard))
        || ((currentGameState == GameState.takable || currentGameState == GameState.hasToTake) && myBoard.getPiece(activeSquare).isTakeLegal(activeSquare, coordinates, myBoard))){
            String moveType = myBoard.getPiece(activeSquare).moveType(activeSquare, coordinates);
            myBoard.movePiece(activeSquare, coordinates);
            String[] test = moveType.split("_");
            boolean taking = moveType.split("_")[0].equals("taking");
            boolean promotion = moveType.split("_")[1].equals("promotion");
            if(taking){
                myBoard.removePiece(new Coordinates((coordinates.getX()+activeSquare.getX())/2, (coordinates.getY()+activeSquare.getY())/2));
            }
            if(taking && myBoard.getPiece(coordinates).canTake(coordinates, myBoard)){
                currentGameState = GameState.hasToTake;
                Coordinates coordinatesOfPieceNeedsToTake = coordinates;
                activeSquare = coordinates;
                returnString += highlightSquare(coordinates);
                resetActive = false;
            }else {
                changePlayer();
                if (takesLeft()) {
                    currentGameState = GameState.takable;
                } else if (movesLeft()) {
                    currentGameState = GameState.movable;
                } else {
                    currentGameState = GameState.finished;
                }
            }
            if (promotion){
                myBoard.placePiece(coordinates, new Queen(myBoard.getPiece(coordinates).getPieceColor()));
            }
        }
        if (resetActive) activeSquare = null;
        return returnString;
    }

    private void changePlayer(){
        if(currentPlayer.equals("white")){
            currentPlayer = "black";
        }else{
            currentPlayer = "white";
        }
    }

    private boolean movesLeft(){
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (myBoard.getPiece(new Coordinates(i, j)) != null
                        && myBoard.getPiece(new Coordinates(i, j)).canMove(new Coordinates(i, j), myBoard)) return true;
            }
        }
        return false;
    }

    private boolean takesLeft(){
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                Piece testPiece = myBoard.getPiece(new Coordinates(i, j));
                if (testPiece != null
                        && testPiece.getPieceColor().equals(currentPlayer)
                        && testPiece.canTake(new Coordinates(i, j), myBoard)) return true;
            }
        }
        return false;
    }
}
