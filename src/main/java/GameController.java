import java.util.*;

public class GameController {

    private final Object eventInputLock = new Object();
    private final Queue<String> eventInput = new LinkedList<>();
    GUIController GUI;
    Thread GUIThread;

    GameController(String[] args) {
        ChessMenu.eventTemp = eventInput;
    }

    void run() {
        GUI = new GUIController();
        GUIThread = new Thread(GUI);
        GUIThread.start();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                synchronized (eventInput) {
                    while (!eventInput.isEmpty()) {
                        handleEvent(eventInput.remove());
                    }
                    eventInput.wait();
                }
            } catch (InterruptedException e) {
                GUIThread.interrupt();
            }
        }
    }

    void handleEvent(String eventName) {
        System.out.println("Event occured: " + eventName);
        switch (eventName) {
            case "Play":
                GUI.setChangeWindow("Chessboard");
                break;
            case "Close":
                Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        GameController gameController = new GameController(args);
        gameController.run();
    }
}

