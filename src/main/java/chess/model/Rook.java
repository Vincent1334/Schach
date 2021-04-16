package chess.model;

public class Rook extends Figure {

    public Rook(int posX, int posY, int team) {
        super(posX, posY, team);
    }

    /**
     * Proofs if the move is a valid move for Rook and makes move if it is
     *
     * @param newX  new input x-position for Rook
     * @param newY  new input y-position for Rook
     * @param board actual state of chessboard
     * @return whether move was successful
     */
    public boolean validMove(int newX, int newY, Board board) {
        return newX == posX || newY == posY;
    }

    @Override
    public char getSymbol() {
        return team == 0 ? '\u2656' : '\u265C';
    }

}