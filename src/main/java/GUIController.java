import javafx.application.Application;

import java.awt.Menu;
import java.util.Queue;

public class GUIController implements Runnable {

    static private boolean openWindow = true;
    static private String newWindowName = "Menu";
    private final Object changeWindowLock = new Object();
    Thread windowThread = new Thread();


    GUIController() {
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                synchronized (changeWindowLock) {
                    if (openWindow){
                        if(windowThread.isAlive()) windowThread.interrupt();
                        openNewWindow();
                        openWindow = false;
                    }
                    changeWindowLock.wait();
                }
            } catch(InterruptedException e){
                windowThread.interrupt();
            }
        }
    }

    private void openNewWindow(){
        switch (newWindowName) {
            case "Menu":
                windowThread = new Thread(new ChessMenu());
                windowThread.start();
                break;
            case "Chessboard":
                windowThread = new Thread(new ChessWindow());
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
