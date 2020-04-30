import javafx.application.Application;
import javafx.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class GameController {

    private final Object eventInputLock = new Object();
    private final Queue<String> eventInput = new LinkedList<>();
    private static volatile boolean running;

    public static GameEngine myEngine;
    private Thread engineThread;


    private GUIController GUI;
    private Thread GUIThread;

    GameController(String[] args) {
        ChessEventHandler.eventTemp = eventInput;
        GameEngine.eventTemp = eventInput;
        running = true;
    }

    void run() {
        GUI = new GUIController();
        GUIThread = new Thread(GUI);
        GUIThread.setName("GUI Controller Thread");
        GUIThread.start();
        while (!Thread.currentThread().isInterrupted() && running) {
            try {
                synchronized (eventInput) {
                    if (eventInput.isEmpty()) {
                        //is there's no Event, wait
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
        eventName = eventName.toLowerCase();
        String[] eventParts = eventName.split("_"); //[0] origin, [1] event, [n>1] parameters
        System.out.println("Event occured: " + eventName);
        switch (eventParts[0]) {
            case "chessmenuwindow":
                if (eventParts[1].compareTo("startgame") == 0) {
                    engineThread = new Thread("EngineThread") {
                        @Override
                        public void run() {
                            try {
                                myEngine = new GameEngine();
                            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    engineThread.start();

                    GUI.setChangeWindow("Chessboard");
                    while(myEngine == null);
                    GUI.setBoardState(myEngine.getStringBoard());
                }
                break;

            case "chessboardwindow":
                if (eventParts[1].compareTo("stopgame") == 0) {
                    GUI.setChangeWindow("Menu");
                }
                break;

            case "chesswindow":
                if (eventParts[1].compareTo("closeprogram") == 0) {
                    closeProgram();
                }
                break;

            case "chessboard":
                if (eventParts[1].compareTo("clicked") == 0) {
                    //String[] highlights = myEngine.squareClicked(new Pair<>(Integer.parseInt(eventParts[2]), Integer.parseInt(eventParts[3])));
                    myEngine.createEvent(eventName);
                }
                break;

            case "gameengine":
                GUI.setHighlightSquares(Arrays.copyOfRange(eventParts, 1, eventParts.length));
                GUI.setBoardState(myEngine.getStringBoard());
                break;
        }
    }

    void closeProgram() {
        GUI.closeGUI();

        myEngine.createEvent("controller_close");

        running = false;
    }


    public static void main(String[] args) {
        GameController gameController = new GameController(args);
        gameController.run();
    }
}

