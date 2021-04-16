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
    public boolean validMove(int newX, int newY, Board board) {
        //Is the new position on the board?
        if(newX<9 && newX>0 && newY<9 && newY>0){
            //Is a Figure of the own team on the new position?
            if(board.getFigure(newX,newY).getTeam() != board.getFigure(posX,posY).getTeam()){
                //Is the new position on a diagonal from the old position
                for (int i=-8; i<9; i++){
                    if((newX==posX+i && newY==posY+i)
                            || (newX==posX-i && newY==posY+i)
                            || (newX==posX+i && newY==posY-i)){
                        //is between the old position and the new position a figure
                        for (int j=1; j < Math.abs(posX-newX); j++){
                            if( !(board.getFigure(posX+j,posY+j) instanceof None)) {
                                return false;
                            }
                        }
                        // sets new position
                        posX = newX;
                        posY = newY;
                        return true;
                    }
                }
            }

        }
        return false;
    }

    @Override
    public String getSymbol() {
        return team == 0 ? "U+2657" : "U+265D";
    }

}
