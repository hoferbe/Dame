import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.InputEvent;

import java.util.Queue;

abstract class ChessEventHandler<T extends Event> implements EventHandler<T> {

    private final Queue<String> events;
    public static Queue<String> eventTemp;

    ChessEventHandler(){
        events = eventTemp;
    }

    protected void send(String eventMessage){
        synchronized (events){
            events.add(eventMessage);
            events.notifyAll();
        }
    }

}
