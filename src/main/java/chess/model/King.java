package chess.model;

public class King extends Figure {

    public King(int team) {
        super(team);
    }

    /**
     * Proofs if the move is a valid move for King
     *
     * @param posX  actual x-position for King
     * @param posY  actual y-position for King
     * @param newX  new input x-position for King
     * @param newY  new input y-position for King
     * @param board actual state of chessboard
     * @return whether move was successful
     */
    public boolean validMove(int posX, int posY, int newX, int newY, Board board) {
        // normal move
        return (newX == posX + 1 || newX == posX - 1 || newX == posX)
                && (newY == posY + 1 || newY == posY - 1 || newY == posY)
                && !(newX == posX && newY == posY);
    }

    @Override
    public char getSymbol() {
        return team == 0 ? '\u265A' : '\u2654';
    }

    @Override
    public String getTitle(){
        return "King";
    }

}
