import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;

public class GUIController implements Runnable {

    static private boolean openNewWindow = false;
    static private String newWindowName = "Menu";
    private final Object changeWindowLock = new Object();
    Thread windowThread;
    private static volatile boolean running;
    public static MyWindow myWindow;


    GUIController() {
        running = true;
    }

    @Override
    public void run() {
        windowThread = new Thread("WindowThread") {
            @Override
            public void run() {
                Application.launch(MyWindow.class);
            }
        };
        windowThread.start();

        while (!Thread.currentThread().isInterrupted() && running) {
            try {
                synchronized (changeWindowLock) {
                    if (!openNewWindow) {
                        changeWindowLock.wait();
                    }
                    if (openNewWindow) {
                        openNewWindow();
                        openNewWindow = false;
                    }
                }
            } catch (InterruptedException ignored) {
                closeGUI();
            }
        }
    }

    public void closeGUI() {
        synchronized (changeWindowLock) {
            running = false;
            //to get the Thread out of sleep and actually check the running boolean
            changeWindowLock.notifyAll();
        }
        myWindow.closeWindow();
    }

    private void openNewWindow(){
        switch (newWindowName) {
            case "Menu":
                Platform.runLater(()->myWindow.changeScene("Menu"));
                break;
            case "Chessboard":
                Platform.runLater(()->myWindow.changeScene("Chessboard"));
                break;
        }
    }

    public void setChangeWindow(String newWindow) {
        synchronized (changeWindowLock) {
            newWindowName = newWindow;
            openNewWindow = true;
            changeWindowLock.notifyAll();
        }
    }

    public void setHighlightSquares(String[] highlightSquares){
        Platform.runLater(()->myWindow.setHighlightSquares(highlightSquares));
    }
}
