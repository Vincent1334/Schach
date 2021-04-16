package chess.model;

public class Bishop extends Figure {

    boolean alreadyMoved;
    int posX;
    int posY;
    int team;

    public Bishop(int posX, int posY, int team) {
        super(posX, posY, team);
    }

    /**
     * Proofs if the move is a valid move for Bishop and makes move if it is
     * @param newX new input x-position for Bishop
     * @param newY new input y-position for Bishop
     * @param board actual state of chessboard
     * @return whether move was successful
     */
    public boolean makeMove(int newX, int newY, Board board) {
        // TODO
    }

    @Override
    public String getSymbol() {
        return team == 0 ? "U+2657" : "U+265D";
    }

}
