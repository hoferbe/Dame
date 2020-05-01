package Engine.Pieces;

import Engine.GameEngine;
import javafx.util.Pair;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import static org.junit.Assert.*;

public class BishopTest {

    private GameEngine myEngine;
    private Thread myThread;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testMove(){
        String[] pieces = {"Bishop_0_0_white", "Bishop_7_7_black", "King_0_1_white", "King_7_6_black"};

        Queue<String> events = new LinkedList<>();

        GameEngine.eventTemp = events;

        myThread = new Thread("Engine Thread"){
            @Override
            public void run() {
                try {
                    myEngine = new GameEngine(pieces);
                } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();



        myEngine.squareClicked(new Pair<>(0, 0));
        events.clear();
        myEngine.squareClicked(new Pair<>(7, 7));
        Assert.assertTrue(Objects.equals(events.peek(), "gameengine_highlights_"));
    }


    @After
    public void tearDown() throws Exception {
    }
}