package Controller;

import DameEngine.GameEngine;
import GUI.EventHandler.ChessEventHandler;

import java.util.*;

public class GameController {

    private final Queue<String> eventInput = new LinkedList<>();
    private static volatile boolean running;

    public static GameEngine myEngine;
    private Thread engineThread;


    private GUIController GUI;
    private Thread GUIThread;

    //constructor
    public GameController(String[] args) {
        //save the event Input for the controller in the engine and for the eventhandler of the GUI
        ChessEventHandler.eventTemp = eventInput;
        GameEngine.eventTemp = eventInput;
        running = true;
    }

    void run() {
        //Create and set up the GUI controller in a new thread
        GUI = new GUIController();
        GUIThread = new Thread(GUI);
        GUIThread.setName("GUI Controller Thread");
        GUIThread.start();
        while (!Thread.currentThread().isInterrupted() && running) {
            try {
                synchronized (eventInput) {
                    if (eventInput.isEmpty()) {
                        //if there's no Event, wait
                        eventInput.wait();
                    }
                    while (!eventInput.isEmpty()) {
                        //handle events until finished
                        handleEvent(eventInput.remove());
                    }
                }
            } catch (NullPointerException | InterruptedException e) {
                e.printStackTrace();
                closeProgram();
            }
        }
    }

    void handleEvent(String eventName){
        String eventNameL = eventName.toLowerCase();
        String[] eventParts = eventNameL.split("_"); //[0] origin, [1] event, [n>1] parameters
        System.out.println("Event occured: " + eventName);
        switch (eventParts[0]) {
            case "chessmenuwindow":
                if (eventParts[1].compareTo("startgame") == 0) {
                    //change to board window and start the game. Engine in a new thread
                    engineThread = new Thread("EngineThread") {
                        @Override
                        public void run() {
                            try {
                                new GameEngine(new String[][]{
                                        {"", "", "", "", "", "", "", ""},
                                        {"", "", "", "", "", "", "", ""},
                                        {"", "", "Pawn_black", "", "", "", "", ""},
                                        {"", "", "", "", "", "", "", ""},
                                        {"", "", "", "", "Pawn_white", "", "", ""},
                                        {"", "", "", "", "", "", "", ""},
                                        {"", "", "", "", "", "", "", ""},
                                        {"", "", "", "", "", "", "", ""}
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    engineThread.start();

                    GUI.setChangeWindow("Engine.Chessboard");
                    while(myEngine == null || !myEngine.ready);
                    GUI.setBoardState(myEngine.getStringBoard());
                }
                break;

            case "chessboardwindow":
                //change back to the menu and shut off the engine
                if (eventParts[1].compareTo("stopgame") == 0) {
                    GUI.setChangeWindow("Menu");
                    closeEngine();
                }
                break;

            case "chesswindow":
                //properly turn off the programm
                if (eventParts[1].compareTo("closeprogram") == 0) {
                    closeProgram();
                }
                //change back to the menu and shut off the engine
                else if(eventParts[1].equals("openmenu")){
                    GUI.setChangeWindow("Menu");
                    closeEngine();
                }
                break;

            case "chessboard":
                //hand over the event ot the game engine
                if (eventParts[1].compareTo("clicked") == 0) {
                    //String[] highlights = myEngine.squareClicked(new Pair<>(Integer.parseInt(eventParts[2]), Integer.parseInt(eventParts[3])));
                    myEngine.createEvent(eventName);
                }
                break;

            case "gameengine":
                //send the highlights to the GUI and reset the boardstate of the GUI
                if(eventParts[1].equals("highlights")) {
                    GUI.setHighlightSquares(Arrays.copyOfRange(eventParts, 2, eventParts.length));
                    GUI.setBoardState(myEngine.getStringBoard());
                }
                //if game has finished, change back to Menu
                else if(eventParts[1].equals("finished")){
                    GUI.setWinner(eventName.split("_")[2]);
                }
                //if the back button has been clicked, change back to the menu
                else if(eventParts[1].equals("closechessboard")){
                    GUI.setChangeWindow("Menu");
                    closeEngine();
                }
                break;
        }
    }

    void closeProgram() {
        GUI.closeGUI();

        closeEngine();

        running = false;
    }

    void closeEngine(){
        if(myEngine != null) {
            myEngine.createEvent("controller_close");
        }
    }


    public static void main(String[] args) {
        GameController gameController = new GameController(args);
        gameController.run();
    }
}

