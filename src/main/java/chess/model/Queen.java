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
    public boolean validMove(int newX, int newY, Board board) {
        //checks if the new position is on the board
        if (newX < 9 && newX > 0 && newY < 9 && newY > 0) {
            //Is a Figure of the own team on the new position?
            if(board.getFigure(newX,newY).getTeam() != board.getFigure(posX,posY).getTeam()) {
                if (posX == newX || posY == newY || posX - newX == posY - newY) {
                    //moving the Queen to new position
                    board.getBoard()[newX][newY] = this;
                    //deleting the old Queen
                    board.getBoard()[posX][posY] = new None(posX, posY, getTeam());
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public String getSymbol() {
        return team == 0 ? "U+2658" : "U+265E";
    }

}

