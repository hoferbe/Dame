import javafx.util.Pair;

public class TestPiece extends Piece {


    public TestPiece(Pair<Integer, Integer> startingSquare, String color) {
        super(startingSquare, color, "file:src/main/resources/circle.png", "file:src/main/resources/circle.png");

    }

    @Override
    public boolean isMoveLegal(Pair<Integer, Integer> start, Pair<Integer, Integer> end, Chessboard myChessboard) {
        return start == square && Math.abs(start.getKey() - end.getKey()) <= 1 && Math.abs(start.getValue() - end.getValue()) <= 1 && Math.abs(start.getKey() - end.getKey()) + Math.abs(start.getValue() - end.getValue()) != 0;
    }
}
