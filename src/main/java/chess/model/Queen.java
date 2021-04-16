package chess.model;

public class Queen extends Figure {


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
        if(super.posX == newX || super.posY == newY || super.posX - newX == super.posY - newY){
            //moving the Queen to new position
            board.getBoard()[newX][newY]=this;
            //deleting the old Queen
            board.getBoard()[super.posX][super.posY]=new None(super.posX, super.posY, super.getTeam());
            return true;
        }
        return false;
    }
    @Override
    public String getSymbol() {
        return team == 0 ? "U+2658" : "U+265E";
    }

}

