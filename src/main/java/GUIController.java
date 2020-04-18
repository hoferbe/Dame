import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;

import java.awt.Menu;
import java.util.Queue;

public class GUIController implements Runnable {

    static private boolean openWindow = true;
    static private String newWindowName = "Menu";
    private final Object changeWindowLock = new Object();
    Thread windowThread = new Thread();
    private static volatile boolean running;


    GUIController() {
        running = true;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && running) {
            try {
                synchronized (changeWindowLock) {
                    if (!openWindow) {
                        changeWindowLock.wait();
                    }
                    if (openWindow) {
                        if (windowThread.isAlive()) {
                            Platform.exit();
                            System.out.println(windowThread.isAlive());
                        }
                        openNewWindow();
                        openWindow = false;
                    }
                }
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void closeGUI() {
        synchronized (changeWindowLock) {
            running = false;
            changeWindowLock.notifyAll();
        }
        Platform.exit();
    }

    private void openNewWindow() throws InterruptedException {
        while(windowThread.isAlive()) Thread.sleep(100);
        switch (newWindowName) {
            case "Menu":
                windowThread = new Thread(new ChessMenu());
                windowThread.setName("Menu Thread");
                windowThread.start();
                break;
            case "Chessboard":
                windowThread = new Thread(new ChessWindow());
                windowThread.setName("Chess Window Thread");
                windowThread.start();
                break;
        }
    }

    public void setChangeWindow(String newWindow) {
        synchronized (changeWindowLock) {
            newWindowName = newWindow;
            openWindow = true;
            changeWindowLock.notifyAll();
        }
    }
}
