import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;

import java.util.LinkedList;
import java.util.Queue;

public class ChessActionEventHandler implements EventHandler<ActionEvent> {

    public final Queue<String> events;

    ChessActionEventHandler(Queue<String> eventsQueue){
        events = eventsQueue;
    }

    @Override
    public void handle(ActionEvent event) {
        synchronized (events){
            events.add(event.toString().split("'")[1]);
            events.notifyAll();
        }
    }
}
