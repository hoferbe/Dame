package Engine;

import Controller.GameController;
import Engine.Pieces.*;
import GUI.ChessboardPane;
import javafx.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class GameEngine {

    private final Chessboard myChessboard;

    public final Queue<String> engineEvents = new LinkedList<>();
    private static volatile boolean running;

    private final Queue<String> controllerEvents;
    public static Queue<String> eventTemp;

    private Pair<Integer, Integer> activeSquare = null;

    private String currentPlayer;

    public boolean ready;

    public GameEngine() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Set<String> availablePiecesNames = new HashSet<>(Arrays.asList(
                "Engine.Pieces.TestPiece",
                "Engine.Pieces.King",
                "Engine.Pieces.Rook",
                "Engine.Pieces.Bishop",
                "Engine.Pieces.Knight",
                "Engine.Pieces.Queen",
                "Engine.Pieces.Pawn"
        ));
        myChessboard = new Chessboard();
        registerPieces(availablePiecesNames);

        fillBoard();

        Piece.myEnginge = this;

        GameController.myEngine = this;

        controllerEvents = eventTemp;

        currentPlayer = "white";

        running = true;
        ready = true;
        run();
    }

    private void fillBoard() {

        myChessboard.placePiece(new Pair<>(0, 0), new Rook("black"));
        myChessboard.placePiece(new Pair<>(1, 0), new Knight("black"));
        myChessboard.placePiece(new Pair<>(2, 0), new Bishop("black"));
        myChessboard.placePiece(new Pair<>(3, 0), new Queen("black"));
        myChessboard.placePiece(new Pair<>(4, 0), new King("black"));
        myChessboard.placePiece(new Pair<>(5, 0), new Bishop("black"));
        myChessboard.placePiece(new Pair<>(6, 0), new Knight("black"));
        myChessboard.placePiece(new Pair<>(7, 0), new Rook("black"));

        myChessboard.placePiece(new Pair<>(0, 1), new Pawn("black"));
        myChessboard.placePiece(new Pair<>(1, 1), new Pawn("black"));
        myChessboard.placePiece(new Pair<>(2, 1), new Pawn("black"));
        myChessboard.placePiece(new Pair<>(3, 1), new Pawn("black"));
        myChessboard.placePiece(new Pair<>(4, 1), new Pawn("black"));
        myChessboard.placePiece(new Pair<>(5, 1), new Pawn("black"));
        myChessboard.placePiece(new Pair<>(6, 1), new Pawn("black"));
        myChessboard.placePiece(new Pair<>(7, 1), new Pawn("black"));


        myChessboard.placePiece(new Pair<>(0, 7), new Rook("white"));
        myChessboard.placePiece(new Pair<>(1, 7), new Knight("white"));
        myChessboard.placePiece(new Pair<>(2, 7), new Bishop("white"));
        myChessboard.placePiece(new Pair<>(3, 7), new Queen("white"));
        myChessboard.placePiece(new Pair<>(4, 7), new King("white"));
        myChessboard.placePiece(new Pair<>(5, 7), new Bishop("white"));
        myChessboard.placePiece(new Pair<>(6, 7), new Knight("white"));
        myChessboard.placePiece(new Pair<>(7, 7), new Rook("white"));

        myChessboard.placePiece(new Pair<>(0, 6), new Pawn("white"));
        myChessboard.placePiece(new Pair<>(1, 6), new Pawn("white"));
        myChessboard.placePiece(new Pair<>(2, 6), new Pawn("white"));
        myChessboard.placePiece(new Pair<>(3, 6), new Pawn("white"));
        myChessboard.placePiece(new Pair<>(4, 6), new Pawn("white"));
        myChessboard.placePiece(new Pair<>(5, 6), new Pawn("white"));
        myChessboard.placePiece(new Pair<>(6, 6), new Pawn("white"));
        myChessboard.placePiece(new Pair<>(7, 6), new Pawn("white"));

    }

    public boolean checkCheck(String color, Chessboard testBoard) {
        Pair<Integer, Integer> currentKingsquare;
        if (color.compareTo("white") == 0) currentKingsquare = testBoard.getKingSquare("white");
        else currentKingsquare = testBoard.getKingSquare("black");

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece currentPiece = testBoard.getPiece(new Pair<>(i, j));
                if (currentPiece != null && currentPiece.getPieceColor().compareTo(color) != 0 && currentPiece.isMoveable(new Pair<>(i, j), currentKingsquare, testBoard)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void registerPieces(Set<String> availablePiecesNames) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        for (String name : availablePiecesNames) {
            Class<? extends Piece> pieceClass = (Class<? extends Piece>) Class.forName(name);
            Piece placeHolder = pieceClass.getDeclaredConstructor(new Class[]{String.class}).newInstance("white");
            ChessboardPane.imagePaths.put(name + "_white", placeHolder.imagePathWhite);
            ChessboardPane.imagePaths.put(name + "_black", placeHolder.imagePathBlack);
        }
    }

    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted() && running) {
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
        } catch (InterruptedException ignore) {

        }
    }

    private void handleEvent(String eventName) {
        eventName = eventName.toLowerCase();
        String[] eventParts = eventName.split("_"); //[0] origin, [1] event, [n>1] parameters
        System.out.println("Event occured: " + eventName);
        switch (eventParts[0]) {
            case "chessboard":
                if (eventParts[1].compareTo("clicked") == 0) {
                    squareClicked(new Pair<>(Integer.parseInt(eventParts[2]), Integer.parseInt(eventParts[3])));
                }
                break;

            case "controller":
                if (eventParts[1].compareTo("close") == 0) {
                    running = false;
                }

        }
    }

    public void squareClicked(Pair<Integer, Integer> square) {
        String activeSquareEvent = "GameEngine_highlights_";
        if (activeSquare == null) {
            Set<Pair<Integer, Integer>> highlights;
            Piece clickedPiece = myChessboard.getPiece(square);
            if (clickedPiece != null && clickedPiece.getPieceColor().compareTo(currentPlayer) == 0) {
                activeSquare = square;
                highlights = myChessboard.getPiece(activeSquare).getLegalMoves(square, myChessboard);
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
        } else {
            if (myChessboard.getPiece(activeSquare).isMoveLegal(activeSquare, square, myChessboard)) {
                myChessboard.getPiece(activeSquare).writeSpecialMove();
                myChessboard.getPiece(activeSquare).canCastleFalse();
                String[] specialMove = myChessboard.getPiece(activeSquare).getSpecialMove().split("_");
                myChessboard.movePiece(activeSquare, square);
                switch (specialMove[0]) {
                    case "castle":
                        myChessboard.getPiece(new Pair<>(Integer.parseInt(specialMove[1]), activeSquare.getValue())).canCastleFalse();
                        myChessboard.movePiece(new Pair<>(Integer.parseInt(specialMove[1]), activeSquare.getValue()), new Pair<>(Integer.parseInt(specialMove[2]), activeSquare.getValue()));
                        break;

                    case "enpassant":
                        myChessboard.removePiece(new Pair<>(Integer.parseInt(specialMove[1]), Integer.parseInt(specialMove[2])));
                        break;

                    case "promotion":
                        myChessboard.placePiece(square, new Queen(myChessboard.getPiece(square).getPieceColor()));
                        break;
                }
                changeColor();
                resetEnPassant();
                if(checkCheck(currentPlayer, myChessboard)){
                    if(!legalMovesLeft(currentPlayer)){
                        stopGame();
                    }
                }
                else{
                    if(!legalMovesLeft(currentPlayer)){
                        stopGame();
                    }
                }
            }
            activeSquare = null;
        }

        synchronized (controllerEvents) {
            controllerEvents.add(activeSquareEvent);
            controllerEvents.notifyAll();
        }
    }

    private boolean legalMovesLeft(String color){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece currentPiece = myChessboard.getPiece(new Pair<>(i, j));
                if(currentPiece != null && currentPiece.getPieceColor().equals(color) && currentPiece.getLegalMoves(new Pair<>(i, j), myChessboard).size() > 0) return true;
            }
        }
        return false;
    }

    public String[][] getStringBoard() {
        return myChessboard.getStringBoard();
    }

    public void createEvent(String event) {
        synchronized (engineEvents) {
            engineEvents.add(event);
            engineEvents.notifyAll();
        }
    }

    public void changeColor() {
        if (currentPlayer.compareTo("white") == 0) currentPlayer = "black";
        else currentPlayer = "white";
    }

    public void resetEnPassant(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece currentPiece = myChessboard.getPiece(new Pair<>(i, j));
                if(currentPiece != null && currentPiece.getPieceColor().equals(currentPlayer)) currentPiece.resetEnPassent();
            }
        }
    }

    private void stopGame(){
        synchronized (controllerEvents){
            controllerEvents.add("gameengine_closeChessboard");
            controllerEvents.notifyAll();
        }
    }
}
