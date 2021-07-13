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

    private final List<Move> PARENT_CUT_OFF;
    private final Move LASTMOVE;

    /**
     * The cutOff constructor
     *
     * @param PARENT_CUT_OFF parent cutoff
     * @param LASTMOVE       the last move
     */
    public CutOff(List<Move> PARENT_CUT_OFF, Move LASTMOVE) {
        this.PARENT_CUT_OFF = PARENT_CUT_OFF;
        this.LASTMOVE = LASTMOVE;
    }

    //--------------getter / setter---------------------------------------------------------------------------------------------------------------

    protected List<Move> getPARENT_CUT_OFF() {
        return PARENT_CUT_OFF;
    }

    protected Move getLASTMOVE() {
        return LASTMOVE;
    }

}
