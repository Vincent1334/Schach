package chess.model;

public class Queen extends Figure {

    boolean alreadyMoved;
    int posX;
    int posY;
    int team;

    public Queen(int posX, int posY, int team) {
        super(posX, posY, team);
    }

    /**
     * Proofs if the move is a valid move for Queen and makes move if it is
     * @param newX new input x-position for Queen
     * @param newY new input y-position for Queen
     * @param board actual state of chessboard
     * @return whether move was successful
     */
    public boolean makeMove(int newX, int newY, Board board) {
        // TODO
    }

}

