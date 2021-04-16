package chess.model;

public class Rook extends Figure {

    boolean alreadyMoved;
    int posX;
    int posY;
    int team;

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
    public boolean makeMove(int newX, int newY, Board board) {

        if (newX == posX || newY == posY) {
            board.setFigure(newX, newY, this);
            return true;
        }
        return false;
    }

    @Override
    public String getSymbol() {
        return team == 0 ? "U+2656" : "U+265C";
    }

}