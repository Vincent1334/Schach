package chess.figures;

import chess.model.Board;
import chess.model.Position;

/**
 * This class contains the information about the queens valid movements
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-05
 */

public class Queen extends Figure {

    /**
     * The constructor of a queen.
     * The queens team and figure ID are initialized here.
     *
     * @param blackTeam white or not
     */
    public Queen(boolean blackTeam) {
        super(blackTeam);
        super.figureID = 5;
    }

    /**
     * The copy constructor of this class
     *
     * @param sourceClass Queen you want to clone
     */
    public Queen(Queen sourceClass) {
        super(sourceClass.blackTeam);
        super.setAlreadyMoved(sourceClass.isAlreadyMoved());
        super.figureID = 5;
    }


    /**
     * Proofs if the move is a valid move for Queen
     *
     * @param actualPos actual position for Queen
     * @param targetPos new input position for Queen
     * @param board     actual state of chessboard
     * @return whether move was successful
     */
    @Override
    public boolean validMove(Position actualPos, Position targetPos, Board board) {

        Bishop b = new Bishop(blackTeam);
        Rook r = new Rook(blackTeam);

        //is the move legal?
        return b.validMove(actualPos, targetPos, board) || r.validMove(actualPos, targetPos, board);
    }

    /**
     * return Symbol
     *
     * @return the symbol of a queen
     */
    @Override
    public char getSymbol() {
        return !blackTeam ? 'Q' : 'q';
    }
}