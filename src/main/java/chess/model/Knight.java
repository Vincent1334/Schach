package chess.model;

public class Knight extends Figure {

    boolean alreadyMoved;
    int posX;
    int posY;
    int team;

    public Knight(int posX, int posY, int team) {
        super(posX, posY, team);
    }

    /**
     * Proofs if the move is a valid move for Knight and makes move if it is
     * @param newX new input x-position for Knight
     * @param newY new input y-position for Knight
     * @param board actual state of chessboard
     * @return whether move was successful
     */
    public boolean makeMove(int newX, int newY, Board board) {
        // TODO
    }

}

