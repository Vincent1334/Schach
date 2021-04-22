package chess.model;

public class Pawn extends Figure {
    public Pawn(int posX, int posY, int team) {
        super(posX, posY, team);
    }

    boolean enPassant = false;
    int enPosY = 0;

    /**
     * getter for enPassant
     * @return if the move is a en passant
     */
    public boolean isEnPassant() {
        return enPassant;
    }
    public void clearEnPassant(){
        enPassant = false;
    }
    public int getEnPassant(){
        return enPosY;
    }
    /**
     * Proofs if the move is a valid move for Pawn and makes move if it is
     * @param newX new input x-position for Pawn
     * @param newY new input y-position for Pawn
     * @param board actual state of chessboard
     * @return whether move was successful
     */
    public boolean validMove(int newX, int newY, Board board) {
        enPassant = false;
        if(((team==0  && posY+1==newY) || (team==1 && posY-1==newY)) && posX==newX){
            //normal move
            alreadyMoved = true;
            posX=newX;
           posY=newY;
            return true;
        }
        if((team==0 && posY+1==newY && board.getFigure(newX,newY).getTeam()==1)
                ||(team==1 && posY-1==newY && board.getFigure(newX,newY).getTeam()==0)
                && (posX+1==newX||posX-1==newX)){
            //normal attack
            alreadyMoved = true;
            posX=newX;
            posY=newY;
            return true;
        }
        if(((team==0 && posY+2==newY) || (team==1 && posY-2==newY)) && (posX==newX && !alreadyMoved)){
            //first move
            alreadyMoved = true;
            posX=newX;
            posY=newY;
            return true;
        }
        if(((team==0  && posY+1==newY && posY==5) || (team==1 && posY-1==newY && posY==2))
            && ((posX+1==newX || posX-1==newX) && board.getFigure(newX,posY).getTeam()==0 && board.getFigure(newX,posY) instanceof Pawn)){
            //attack en passant
            enPassant = true;
            alreadyMoved = true;
            posX=newX;
            posY=newY;
            return true;
        }
        return false;
    }

    @Override
    public char getSymbol() {
        return team == 0 ? '\u265F' : '\u2659';
    }


}
