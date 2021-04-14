package chess.model;

public class King extends Figure {

    boolean alreadyMoved;
    int posX;
    int posY;
    int team;

    public King(int posX, int posY, int team) {
        super(posX, posY, team);
    }

    /**
     * Proofs if the move is a valid move for King and makes move if it is
     * @param newX new input x-position for King
     * @param newY new input y-position for King
     * @param board actual state of chessboard
     * @return whether move was successful
     */
    public boolean makeMove(int newX, int newY, Board board) {
        // TODO
    }

}
