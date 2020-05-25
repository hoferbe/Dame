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
    public boolean ready;
    private boolean running;

    //List of the events needing to be handled by the Engine
    private final Queue<String> engineEvents = new LinkedList<>();

    //Events sent to the controller
    private final Queue<String> controllerEvents;
    public static Queue<String> eventTemp;

    //List to have all pieces
    private final Set<String> availablePiecesNames = new HashSet<>(Arrays.asList(
            "DameEngine.Pieces.Pawn",
            "DameEngine.Pieces.Queen"
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

    //Constructor for standard board setting
    public GameEngine() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        //calls the constructor for a specific board setup with the standard boardstate
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

    //Constructor for specific board setting (mainly used for unit tests)
    public GameEngine(String[][] boardState) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        myBoard = new Board();

        //saves a reference to the Queue for the events in the controller
        controllerEvents = eventTemp;

        //registering pieces
        registerPieces();
        fillBoard(boardState);

        //saving reference to this object in the controller
        GameController.myEngine = this;

        running = true;
        ready = true;

        run();
    }

    //Registering pieces at the GUI == saving the url for the images of the pieces
    private void registerPieces() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //for each piece named in the Set, creating an instance to call the method to save the file paths at the GUI
        for (String name : availablePiecesNames) {
            //Creating class by name
            Class<? extends Piece> pieceClass = (Class<? extends Piece>) Class.forName(name);
            //creating instance by name
            Piece placeHolder = pieceClass.getDeclaredConstructor(new Class[]{String.class}).newInstance("white");
            //saving the urls
            ChessboardPane.imagePaths.put(name + "_white", placeHolder.getImagePathWhite());
            ChessboardPane.imagePaths.put(name + "_black", placeHolder.getImagePathBlack());
        }
    }

    //filling board with a specific board state
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

    //run routin which is running while the game is running
    private void run() {
        try {
            //game loop for the enginge
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

    //method to properly stop the engine
    private void stopEngine() {
        engineEvents.clear();
        running = false;
    }

    //Methog returns the current Board as 2D array of String
    public String[][] getStringBoard() {
        return myBoard.getStringBoard();
    }

    //Method that creates an event for the engine and wakes the thread up
    public void createEvent(String eventName) {
        synchronized (engineEvents) {
            engineEvents.add(eventName);
            engineEvents.notifyAll();
        }
    }

    //Method that handles the events
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

    //Method that handles the square clicked event
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
        //send the event to the controller
        sendEventController(activeSquareEvent);
    }

    //method that gets called when there is no square active
    private String highlightSquare(Coordinates coordinates){
        String highlightedSquares = "";
        Set<Coordinates> legalMoves;
        //Get the piece which is on the square clicked
        Piece clickedPiece = myBoard.getPiece(coordinates);
        //Make square only active if there is a piece on the square and the clicked piece is of color as current player
        if(clickedPiece != null && clickedPiece.getPieceColor().equals(currentPlayer)){
            activeSquare = coordinates;

            //If a piece can be taken, one has to take a piece. Additionally if a piece can take a second time, it has to do so. These cases are dealt with here
            if(currentGameState == GameState.takable || (currentGameState == GameState.hasToTake && coordinates.equals(coordinatesOfPieceNeedsToTake))) legalMoves = myBoard.getPiece(coordinates).getLegalTakes(coordinates, myBoard);
            //If no piece can be taken, we deal with it here
            else legalMoves = myBoard.getPiece(coordinates).getLegalMoves(coordinates, myBoard);

            //creating an ArrayList with constructions for the GUI on which squares to highlight
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

    //method called when there is a square active
    private String checkMove(Coordinates coordinates){
        //used to make sure active doesn't get reset after piece has taken and can retake
        boolean resetActive = true;

        String returnString = "";
        //if we are in the movable game state, check if the chosen move is legal
        //if we are in takable or hasToTake game state, make sure the chosen move is legal and taking
        if((currentGameState == GameState.movable && myBoard.getPiece(activeSquare).isMoveLegal(activeSquare, coordinates, myBoard))
        || ((currentGameState == GameState.takable || currentGameState == GameState.hasToTake) && myBoard.getPiece(activeSquare).isTakeLegal(activeSquare, coordinates, myBoard))){
            //Check what move type it is
            String moveType = myBoard.getPiece(activeSquare).moveType(activeSquare, coordinates);
            //actually move the piece
            myBoard.movePiece(activeSquare, coordinates);

            //check if it is taking, promoting (or both)
            boolean taking = moveType.split("_")[0].equals("taking");
            boolean promotion = moveType.split("_")[1].equals("promotion");
            if(taking){
                //if it is taking, remove the taken piece
                myBoard.removePiece(new Coordinates((coordinates.getX()+activeSquare.getX())/2, (coordinates.getY()+activeSquare.getY())/2));
            }
            if(taking && myBoard.getPiece(coordinates).canTake(coordinates, myBoard)){
                //if it was a taking move and the piece can take another time, make sure the game state is changed accordingly.
                currentGameState = GameState.hasToTake;
                //save the coordinates of the piece that has to take next
                coordinatesOfPieceNeedsToTake = coordinates;
                //choose it as a active square
                activeSquare = coordinates;
                //create highlighting for the piece
                returnString += highlightSquare(coordinates);
                //make sure the active fields don't get reset
                resetActive = false;
            }else {
                //if the moved piece can't move again, change player
                changePlayer();

                if (takesLeft()) {
                    //check if any takes are left, force a take on the next move
                    currentGameState = GameState.takable;
                } else if (movesLeft()) {
                    //check if any moves are left
                    currentGameState = GameState.movable;
                } else {
                    //if no moves are left, close the game
                    currentGameState = GameState.finished;
                    sendEventController("gameengine_finished_" + currentPlayer);
                }
            }
            if (promotion){
                myBoard.placePiece(coordinates, new Queen(myBoard.getPiece(coordinates).getPieceColor()));
            }
        }
        if (resetActive) activeSquare = null;
        return returnString;
    }

    //method to change the current player from black to white and vice versa
    private void changePlayer(){
        if(currentPlayer.equals("white")){
            currentPlayer = "black";
        }else{
            currentPlayer = "white";
        }
    }

    //checks whether there are any legal moves left for the current player
    private boolean movesLeft(){
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++){
                Piece testPiece = myBoard.getPiece(new Coordinates(x, y));
                if (testPiece != null
                        && testPiece.getPieceColor().equals(currentPlayer)
                        && testPiece.canMove(new Coordinates(x, y), myBoard)) return true;
            }
        }
        return false;
    }

    //checks if there are any moves left that are taking
    private boolean takesLeft(){
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++){
                Piece testPiece = myBoard.getPiece(new Coordinates(x, y));
                if (testPiece != null
                        && testPiece.getPieceColor().equals(currentPlayer)
                        && testPiece.canTake(new Coordinates(x, y), myBoard)) return true;
            }
        }
        return false;
    }

    //method that sends an event to the controller
    private void sendEventController(String event){
        synchronized (controllerEvents) {
            controllerEvents.add(event);
            controllerEvents.notifyAll();
        }
    }

    public Queue<String> getControllerEvents(){
        return controllerEvents;
    }

    public Queue<String> getEngineEvents(){
        return engineEvents;
    }
}
