package DameEngine.Pieces;

import Controller.GameController;
import DameEngine.GameEngine;
import org.ietf.jgss.GSSManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.*;

public class PawnTest {

    GameEngine myEngine;

    private final Queue<String> eventInput = new LinkedList<>();

    @Before
    public void setUp() throws Exception {
        GameEngine.eventTemp = eventInput;
        System.out.println("SetUp!");
    }

    @After
    public void tearDown() throws Exception {
        if(myEngine != null) {
            myEngine.createEvent("controller_close");
        }
        myEngine = null;
        GameController.myEngine = null;
        System.out.println("Tear Down!");
    }

    @Test
    public void legalMoves() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, InterruptedException {

        Thread engineThread = new Thread("EngineThread") {
            @Override
            public void run() {
                try {
                    new GameEngine(new String[][]{
                            {"Pawn_black", "", "", "", "", "", "", ""},
                            {"", "", "", "", "", "", "", ""},
                            {"", "", "", "", "", "", "", ""},
                            {"", "", "", "", "", "", "", ""},
                            {"", "", "", "", "", "", "", ""},
                            {"", "", "", "", "", "", "", ""},
                            {"", "", "", "", "", "", "", ""},
                            {"", "", "", "", "", "", "", "Pawn_white"}
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        engineThread.start();

        while(GameController.myEngine == null || !GameController.myEngine.ready){
            Thread.sleep(100);
        }
        myEngine = GameController.myEngine;

        myEngine.createEvent("chessBoard_clicked_7_7");
        synchronized (eventInput){
            eventInput.wait(1000);
        }
        myEngine.createEvent("chessBoard_clicked_6_6");
        synchronized (eventInput){
            eventInput.wait(1000);
        }

        assertEquals(2, eventInput.size());

        myEngine.createEvent("chessBoard_clicked_0_0");
        myEngine.createEvent("chessBoard_clicked_1_1");
        Thread.sleep(100);

        assertEquals(4, eventInput.size());


        if(myEngine != null) {
            myEngine.createEvent("controller_close");
        }
    }

    @Test
    public void illegalMoves() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, InterruptedException
    {

        Thread engineThread = new Thread("EngineThread") {
            @Override
            public void run() {
                try {
                    new GameEngine(new String[][]{
                            {"Pawn_black", "", "", "", "", "", "", ""},
                            {"", "Pawn_black", "", "", "", "", "", ""},
                            {"", "", "", "", "", "", "", ""},
                            {"", "", "", "", "", "", "", ""},
                            {"", "", "", "", "", "", "", ""},
                            {"", "", "", "", "", "", "", ""},
                            {"", "", "", "", "", "", "Pawn_white", ""},
                            {"", "", "", "", "", "", "", "Pawn_white"}
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        engineThread.start();

        while(GameController.myEngine == null){
            Thread.sleep(100);
        }
        myEngine = GameController.myEngine;

        myEngine.createEvent("chessBoard_clicked_7_7");
        myEngine.createEvent("chessBoard_clicked_6_6");
        Thread.sleep(100);

        assertEquals(2, eventInput.size());
        eventInput.remove();
        assertEquals("GameEngine_highlights_", eventInput.remove());
    }
}