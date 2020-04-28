abstract class Piece {

    String square;

    public Piece(String startingSquare) {
        square = startingSquare;
    }

    abstract String[] getLegalMoves(Chessboard myChessboard);

    abstract void isMoveLegal(String start, String end);
}
