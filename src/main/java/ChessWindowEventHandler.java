import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.stage.WindowEvent;

import java.util.Queue;

public class ChessWindowEventHandler implements EventHandler<WindowEvent> {

    public final Queue<String> events;

    ChessWindowEventHandler(Queue<String> eventsQueue){
        events = eventsQueue;
    }

    @Override
    public void handle(WindowEvent event) {
        System.out.println(event.toString());
        System.out.println(event.getEventType().getName());
        if (event.getEventType().getName().compareTo("WINDOW_CLOSE_REQUEST") == 0){
            synchronized (events){
                events.add("Close");
                events.notifyAll();
            }
        }
    }
}
