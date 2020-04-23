import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;

import java.util.LinkedList;
import java.util.Queue;

abstract class ChessActionEventHandler implements EventHandler<ActionEvent> {

    private final Queue<String> events;
    public static Queue<String> eventTemp;

    ChessActionEventHandler(){
        events = eventTemp;
    }

    protected void send(String eventMessage){
        synchronized (events){
            events.add(eventMessage);
            events.notifyAll();
        }
    }
}
