import java.util.*;

public class GameController {

    private final Object eventInputLock = new Object();
    private final Queue<String> eventInput = new LinkedList<>();
    private static volatile boolean running;
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
                        eventInput.wait();
                    }
                    while (!eventInput.isEmpty()) {
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
        System.out.println("Event occured: " + eventName);
        switch (eventName) {
            case "chessmenu_startgame":
                GUI.setChangeWindow("Chessboard");
                break;

            case "chessboard_stopgame":
                GUI.setChangeWindow("Menu");
                break;

            case "chesswindow_closeprogram":
                closeProgram();
                break;
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

