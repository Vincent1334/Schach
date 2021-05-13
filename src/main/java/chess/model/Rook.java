package chess.model;

/**
 * This class contains the information about the rooks valid movements
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-05
 */

public class Rook extends Figure {

    /**
     * The constructor of a rook
     * The rooks team and figure ID are initialized here.
     */
    public Rook(boolean blackTeam) {
        super(blackTeam);
        super.figureID = 2;
    }

    /**
     * The copy constructor of this class
     *
     * @param sourceClass Rook you want to clone
     */
    public Rook(Rook sourceClass) {
        super(sourceClass.blackTeam);
        super.setAlreadyMoved(sourceClass.isAlreadyMoved());
        super.figureID = 2;
    }

    /**
     * Proofs if the move is a valid move for Rook
     *
     * @param actualPos actual position of the Rook
     * @param targetPos new input position for Rook
     * @param board     actual state of chessboard
     * @return whether move was successful
     */
    @Override
    public boolean validMove(Position actualPos, Position targetPos, Board board) {
        // don't stay at the same place
        if (actualPos.getPosX() != targetPos.getPosX() && actualPos.getPosY() != targetPos.getPosY()) return false;

        //is between the old position and the new position a figure
        int directionX = 1;
        int directionY = 1;
        if(actualPos.getPosX() > targetPos.getPosX()) directionX = -1;
        if(actualPos.getPosY() > targetPos.getPosY()) directionY = -1;

        if(actualPos.getPosX() == targetPos.getPosX()){
            for(int i = 1; i < Math.abs(actualPos.getPosY()- targetPos.getPosY()); i++){
                if(!(board.getFigure(actualPos.getPosX(), actualPos.getPosY()+i*directionY) instanceof None)) return false;
            }
        }else{
            for(int i = 1; i < Math.abs(actualPos.getPosX()- targetPos.getPosX()); i++){
                if(!(board.getFigure(actualPos.getPosX()+i*directionX, actualPos.getPosY()) instanceof None)) return false;
            }
        }

        return true;
    }

    /**
     * return Symbol
     * @return
     */
    @Override
    public char getSymbol() {
        return !blackTeam ? 'R' : 'r';
    }

}