package chess.model;

public class Pawn extends Figure {

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
        if(team==0 && posX==newX && posY+1==newY
            || team==1 && posX==newX && posY-1==newY){
            //normal move
            alreadyMoved = true;
            return true;
        }
        if(team==0 && posX+1==newX && posY+1==newY && board.getFigure(newX,newY).getTeam()==1
                || team==1 && posX-1==newX && posY-1==newY && board.getFigure(newX,newY).getTeam()==0){
            //normal attack
            alreadyMoved = true;
            return true;
        }
        if(team==0 && posX==newX && posY+2==newY && !alreadyMoved
                || team==1 && posX==newX && posY-2==newY && !alreadyMoved){
            //first move
            alreadyMoved = true;
            return true;
        }
        if(team==0 && posX+1==newX && posY+1==newY && posY==5
                && board.getFigure(newX,5).getTeam()==1 && board.getFigure(newX,5) instanceof Pawn
                || team==1 && posX-1==newX && posY-1==newY && posY==2
                && board.getFigure(newX,2).getTeam()==0 && board.getFigure(newX,2) instanceof Pawn){
            //attack en passant
            board.setFigure(newX,posY,new None(newX, posY,1));
            alreadyMoved = true;
            return true;
        }
        return false;
    }

    @Override
    public char getSymbol() {
        return team == 0 ? '\u265F' : '\u2659';
    }

}
