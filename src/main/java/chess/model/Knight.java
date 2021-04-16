package chess.model;

public class Knight extends Figure {

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
    public boolean validMove(int newX, int newY, Board board) {
        if(posX + 1 == newX && posY + 2 == newY || posX + 1 == newX && posY - 2 == newY
                ||posX - 1 == newX && posY + 2 == newY|| posX - 1 == newX && posY - 2 == newY
                || posX + 2 == newX && posY + 1 == newY|| posX + 2 == newX && posY - 1 == newY
                || posX - 2 == newX && posY + 1 == newY|| posX - 2 == newX && posY - 1 == newY){
            return true;
        }
        return false;
    }

    @Override
    public char getSymbol() {
        return team == 0 ? '\u2658' : '\u265E';
    }

}

