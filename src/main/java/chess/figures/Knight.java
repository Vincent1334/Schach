package chess.figures;

import chess.model.Board;
import chess.model.Position;

/**
 * This class contains the information about the knights valid movements
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-05
 */

public class Knight extends Figure {

    /**
     * The constructor of a knight
     * The knights team and figure ID are is initialized here.
     * @param blackTeam
     */
    public Knight(boolean blackTeam) {
        super(blackTeam);
        super.figureID = 3;
    }

    /**
     * The copy constructor of this class
     *
     * @param sourceClass Knight you want to clone
     */
    public Knight(Knight sourceClass){
        super(sourceClass.blackTeam);
        super.setAlreadyMoved(sourceClass.isAlreadyMoved());
        super.figureID = 3;
    }

    /**
     * Proofs if the move is a valid move for Knight
     *
     * @param actualPos actual position for Knight
     * @param targetPos new input position for Knight
     * @param board     actual state of chessboard
     * @return whether move was successful
     */

    @Override
    public boolean validMove(Position actualPos, Position targetPos, Board board) {

        //is the move legal?
        return Math.abs(actualPos.getPosX()-targetPos.getPosX()) == 2 && Math.abs(actualPos.getPosY()-targetPos.getPosY()) == 1
                || Math.abs(actualPos.getPosX()-targetPos.getPosX()) == 1 && Math.abs(actualPos.getPosY()-targetPos.getPosY()) == 2;
    }

    /**
     * return Symbol
     * @return the Symbol of a knight
     */
    @Override
    public char getSymbol() {
        return !blackTeam ? 'N' : 'n';
    }


}

