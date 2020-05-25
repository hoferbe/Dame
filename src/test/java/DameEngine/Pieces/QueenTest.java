package DameEngine.Pieces;

import Controller.GameController;
import DameEngine.GameEngine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.*;

public class QueenTest {

    GameEngine myEngine;

    private final Queue<String> eventInput = new LinkedList<>();

    @Before
    public void setUp() {
        GameEngine.eventTemp = eventInput;
        System.out.println("SetUp!");
    }

    @After
    public void tearDown() {
        if(myEngine != null) {
            myEngine.createEvent("controller_close");
        }
        myEngine = null;
        GameController.myEngine = null;
        System.out.println("Tear Down!");
    }

    @Test
    public void legalMoves() throws InterruptedException
    {

        Thread engineThread = new Thread("EngineThread") {
            @Override
            public void run() {
                try {
                    new GameEngine(new String[][]{
                            {"Queen_black", "", "", "", "", "", "", ""},
                            {"", "", "", "", "", "", "", ""},
                            {"", "", "", "", "", "", "", ""},
                            {"", "", "", "", "", "", "", ""},
                            {"", "", "", "", "", "", "", ""},
                            {"", "", "", "", "", "", "", ""},
                            {"", "", "", "", "", "", "", ""},
                            {"", "", "", "", "", "", "", "Queen_white"}
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
        synchronized (eventInput){
            eventInput.wait(1000);
        }
        myEngine.createEvent("chessBoard_clicked_1_1");
        synchronized (eventInput){
            eventInput.wait(1000);
        }

        assertEquals(4, eventInput.size());


        myEngine.createEvent("chessBoard_clicked_6_6");
        synchronized (eventInput){
            eventInput.wait(1000);
        }
        myEngine.createEvent("chessBoard_clicked_7_7");
        synchronized (eventInput){
            eventInput.wait(1000);
        }
        assertEquals(6, eventInput.size());


        myEngine.createEvent("chessBoard_clicked_1_1");
        synchronized (eventInput){
            eventInput.wait(1000);
        }
        myEngine.createEvent("chessBoard_clicked_0_0");
        synchronized (eventInput){
            eventInput.wait(1000);
        }

        assertEquals(8, eventInput.size());


        if(myEngine != null) {
            myEngine.createEvent("controller_close");
        }
    }

    @Test
    public void takingMoves() throws InterruptedException {

        Thread engineThread = new Thread("EngineThread") {
            @Override
            public void run() {
                try {
                    new GameEngine(new String[][]{
                            {"Queen_white", "", "", "", "", "", "", ""},
                            {"", "Queen_black", "", "", "", "", "", ""},
                            {"", "", "", "", "", "", "", ""},
                            {"", "", "", "", "", "", "", ""},
                            {"", "", "", "", "", "", "", ""},
                            {"", "", "", "", "", "", "", ""},
                            {"", "", "", "", "", "", "Queen_white", ""},
                            {"", "", "", "", "", "", "", "Queen_black"}
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        engineThread.start();

        while (GameController.myEngine == null || !GameController.myEngine.ready) {
            Thread.sleep(100);
        }
        myEngine = GameController.myEngine;

        myEngine.createEvent("chessBoard_clicked_7_7");
        synchronized (eventInput){
            eventInput.wait(1000);
        }
        myEngine.createEvent("chessBoard_clicked_5_5");
        synchronized (eventInput){
            eventInput.wait(1000);
        }
        assertNull(myEngine.getStringBoard()[6][6]);

        myEngine.createEvent("chessBoard_clicked_0_0");
        synchronized (eventInput){
            eventInput.wait(1000);
        }
        myEngine.createEvent("chessBoard_clicked_2_2");
        synchronized (eventInput){
            eventInput.wait(1000);
        }
        assertNull(myEngine.getStringBoard()[1][1]);
    }
}