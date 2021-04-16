package chess.model;

public class Pawn extends Figure {

    public Pawn(int posX, int posY, int team) {
        super(posX, posY, team);
    }

    /**
     * Proofs if the move is a valid move for Pawn and makes move if it is
     * @param newX new input x-position for Pawn
     * @param newY new input y-position for Pawn
     * @param board actual state of chessboard
     * @return whether move was successful
     */
    public boolean validMove(int newX, int newY, Board board) {
        return false;
        // TODO
    }

    @Override
    public char getSymbol() {
        return team == 0 ? '\u2659' : '\u265F';
    }

}
