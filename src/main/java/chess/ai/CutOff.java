package chess.ai;

import chess.model.Move;

import java.util.List;

/**
 * This class contains the information about the cutoff and last move
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-09
 */
public class CutOff {

    private List<Move> parentCutOff;
    private Move lastMove;

    /**
     * The cutOff constructor
     *
     * @param parentCutOff parent cutoff
     * @param lastMove the last move
     */
    public CutOff(List<Move> parentCutOff, Move lastMove){
        this.parentCutOff = parentCutOff;
        this.lastMove = lastMove;
    }

    //--------------getter / setter---------------------------------------------------------------------------------------------------------------

    public List<Move> getParentCutOff() {
        return parentCutOff;
    }

    public void setParentCutOff(List<Move> parentCutOff) {
        this.parentCutOff = parentCutOff;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public void setLastMove(Move lastMove) {
        this.lastMove = lastMove;
    }
}
