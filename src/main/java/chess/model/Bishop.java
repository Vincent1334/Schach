package chess.model;

/**
 * This class contains the information about the bishops valid movements
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-05
 */

public class Bishop extends Figure {

    /**
     * The constructor of a bishop
     * The bishops team and figure ID are initialized here.
     */
    public Bishop(int team) {
        super(team);
        super.figureID = 4;
    }

    /**
     * The copy constructor of this class
     * @param sourceClass
     */
    public Bishop(Bishop sourceClass) {
        super(sourceClass.team);
        super.alreadyMoved = sourceClass.alreadyMoved;
        super.figureID = 4;
    }

    /**
     * Proofs if the move is a valid move for Bishop
     *
     * @param actualPos actual position for Bishop
     * @param targetPos new input position for Bishop
     * @param board     actual state of chessboard
     * @return whether move was successful
     */

    @Override
    public boolean validMove(Position actualPos, Position targetPos, Board board){

        //Is the new position on a diagonal from the old position
        if(Math.abs(actualPos.getPosX() - targetPos.getPosX()) != Math.abs(actualPos.getPosY() - targetPos.getPosY())) return false;

        //is between the old position and the new position a figure
        int directionX = 1;
        int directionY = 1;
        if(actualPos.getPosX() > targetPos.getPosX()) directionX = -1;
        if(actualPos.getPosY() > targetPos.getPosY()) directionY = -1;

        for(int i = 1; i < Math.abs(actualPos.getPosX()-targetPos.getPosX()); i++){
            if(!(board.getFigure(actualPos.getPosX()+i*directionX, actualPos.getPosY()+i*directionY) instanceof None)) return false;
        }

        return true;
    }

    @Override
    public char getSymbol() {
        return team == 0 ? 'B' : 'b';
    }

}
