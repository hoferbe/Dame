import java.util.*;

public class GameController {

    private final Object eventInputLock = new Object();
    private final Queue<String> eventInput = new LinkedList<>();
    private static volatile boolean running;
    private GameEngine myEngine;
    GUIController GUI;
    Thread GUIThread;

    GameController(String[] args) {
        ChessEventHandler.eventTemp = eventInput;
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
                        //handle events until finisheed
                        handleEvent(eventInput.remove());
                    }
                }
            } catch (InterruptedException interruptEx) {
                closeProgram();
            }
        }
    }

    void handleEvent(String eventName) {
        eventName = eventName.toLowerCase();
        String[] eventParts = eventName.split("_"); //[0] origin, [1] event, [n>1] parameters
        System.out.println("Event occured: " + eventName);
        switch (eventParts[0]) {
            case "chessmenuwindow":
                if(eventParts[1].compareTo("startgame") == 0) {
                    GUI.setChangeWindow("Chessboard");
                    myEngine = new GameEngine();
                }
                break;

            case "chessboardwindow":
                if(eventParts[1].compareTo("stopgame") == 0) {
                    GUI.setChangeWindow("Menu");
                }
                break;

            case "chesswindow":
                if(eventParts[1].compareTo("closeprogram") == 0) {
                    closeProgram();
                }
                break;

            case "chessboard":
                if(eventParts[1].compareTo("clicked") == 0){
                    myEngine.move(eventParts[2], eventParts[3]);
                    /*
                    String[] test = new String[9];
                    test[0] = "green";
                    test[3] = "red";
                    test[6] = "green";
                    System.arraycopy(eventParts, 2, test, 1, 2);
                    test[4] = Integer.toString((Integer.parseInt(test[1])+1)%8);
                    test[5] = Integer.toString((Integer.parseInt(test[2])+1)%8);

                    test[7] = Integer.toString((Integer.parseInt(test[1])+1)%8);
                    test[8] = Integer.toString((Integer.parseInt(test[2]))%8);
                    GUI.setHighlightSquares(test);
                     */
                }
        }
    }

    void closeProgram() {
        GUI.closeGUI();

        running = false;
    }


    public static void main(String[] args) {
        GameController gameController = new GameController(args);
        gameController.run();
    }
}

