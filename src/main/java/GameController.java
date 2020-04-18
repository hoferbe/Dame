import java.util.*;

public class GameController {

    private final Object eventInputLock = new Object();
    private final Queue<String> eventInput = new LinkedList<>();
    private static volatile boolean running;
    GUIController GUI;
    Thread GUIThread;

    GameController(String[] args) {
        ChessMenu.eventTemp = eventInput;
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
            } catch (InterruptedException ignored) {
            }finally {
                GUI.closeGUI();
            }
        }
    }

    void handleEvent(String eventName) {
        eventName = eventName.toLowerCase();
        System.out.println("Event occured: " + eventName);
        switch (eventName) {
            case "chessmenu_startgame":
                System.out.println("1");
                GUI.setChangeWindow("Chessboard");
                break;
            case "chessmenu_closeprogram":
                System.out.println("2");
                closeProgram();
                break;
        }
    }

    void closeProgram() {
        running = false;
    }


    public static void main(String[] args) {
        GameController gameController = new GameController(args);
        gameController.run();
    }
}

