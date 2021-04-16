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
    public boolean validMove(int newX, int newY, Board board) {
        // TODO
    }

    @Override
    public String getSymbol() {
        return team == 0 ? "U+2659" : "U+265F";
    }

}
