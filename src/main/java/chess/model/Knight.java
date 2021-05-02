package chess.model;

public class Knight extends Figure {

    public Knight(int team) {
        super(team);
    }

    /**
     * Proofs if the move is a valid move for Knight
     * @param posX actual x-position for Knight
     * @param posY actual y-position for Knight
     * @param newX new input x-position for Knight
     * @param newY new input y-position for Knight
     * @param board actual state of chessboard
     * @return whether move was successful
     */
    public boolean validMove(int posX, int posY, int newX, int newY, Board board) {
        return (posX + 1 == newX && posY + 2 == newY) || (posX + 1 == newX && posY - 2 == newY)
                || (posX - 1 == newX && posY + 2 == newY) || (posX - 1 == newX && posY - 2 == newY)
                || (posX + 2 == newX && posY + 1 == newY) || (posX + 2 == newX && posY - 1 == newY)
                || (posX - 2 == newX && posY + 1 == newY) || (posX - 2 == newX && posY - 1 == newY);
    }

    @Override
    public char getSymbol() {
        return team == 0 ?  '\u265E' : '\u2658';
    }

}

