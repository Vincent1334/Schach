package chess.model;

/**
 * This class contains the information about the pawns valid movements
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-05
 */

public class Pawn extends Figure {
    boolean enPassant = false;
    /**
     * The constructor of a pawn
     * The pawns team and figure ID are initialized here.
     */
    public Pawn(boolean team) {
        super(team);
        super.figureID = 1;
    }

    /**
     * The copy constructor of this class
     * @param sourceClass
     */
    public Pawn(Pawn sourceClass) {
        super(sourceClass.team);
        super.alreadyMoved = sourceClass.alreadyMoved;
        this.enPassant = sourceClass.enPassant;
        super.figureID = 1;
    }

    /**
     * getter for enPassant
     *
     * @return true if the pawn can be beaten with an en passant
     */
    public boolean isEnPassant() {
        return enPassant;
    }

    /**
     * Proofs if the move is a valid move for Pawn
     *
     * @param actualPos actual position for Pawn
     * @param targetPos new input position for Pawn
     * @param board     actual state of chessboard
     * @return whether move was successful
     */
    @Override
    public boolean validMove(Position actualPos, Position targetPos, Board board) {

        //is move legal?
        if(!(normalMove(actualPos,targetPos) || normalAttack(actualPos,targetPos,board) || firstMove(actualPos,targetPos,board))) return false;

        //is the field empty?
        if((board.getFigure(targetPos) instanceof  None)) return true;

        // is the target field with an enemy figure?
        if(board.getFigure(targetPos).getTeam() == team) return false;

        return true;
    }

    /**
     * Tests if the pawn makes an normal move
     * @param actualPos actual position for Pawn
     * @param targetPos new input position for Pawn
     * @return pawn makes an normal move
     */
    private boolean normalMove(Position actualPos, Position targetPos){
        int posX = actualPos.getPosX();
        int posY = actualPos.getPosY();
        int newX = targetPos.getPosX();
        int newY = targetPos.getPosY();

        return (team == false && posY + 1 == newY || team == true && posY - 1 == newY) && posX == newX;
    }

    /**
     * Tests if the pawn makes an normal attack
     * @param actualPos actual position for Pawn
     * @param targetPos new input position for Pawn
     * @return pawn makes an normal attack
     */
    private boolean normalAttack(Position actualPos, Position targetPos, Board board){
        int posX = actualPos.getPosX();
        int posY = actualPos.getPosY();
        int newX = targetPos.getPosX();
        int newY = targetPos.getPosY();

        return (team == false && posY + 1 == newY  || team == true && posY - 1 == newY ) && (posX + 1 == newX || posX - 1 == newX) && !(board.getFigure(targetPos) instanceof None);
    }

    /**
     * Tests if the pawn makes his first move
     * @param actualPos actual position for Pawn
     * @param targetPos new input position for Pawn
     * @return pawn makes first move
     */
    private boolean firstMove(Position actualPos, Position targetPos, Board board){
        int posX = actualPos.getPosX();
        int posY = actualPos.getPosY();
        int newX = targetPos.getPosX();
        int newY = targetPos.getPosY();

        if ((team == false && posY + 2 == newY && board.getFigure(posX,posY+1) instanceof None
                || team == true && posY - 2 == newY && board.getFigure(posX,posY-1) instanceof None)
                && posX == newX && !alreadyMoved) {
            enPassant = true;
            return true;
        }
        return false;
    }

    /**
     * sets enPassant to false
     */
    public void resetEnPassant() {
        this.enPassant = false;
    }

    //only for Testing
    public void setEnPassant(boolean enPassant) {
        this.enPassant = enPassant;
    }

    @Override
    public char getSymbol() {
        return team == false ? 'P' : 'p';
    }

}
