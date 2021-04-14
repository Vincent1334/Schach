package chess.model;

public class Pawn extends Figure {

    boolean alreadyMoved;
    int posX;
    int posY;
    int team;

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
    public boolean makeMove(int newX, int newY, Board board) {
        // TODO
    }

}
