package chess.model;


import java.util.Objects;

/**
 * This class contains the information about a Position
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-05
 */
public class Position {

    private final int POS_X;
    private final int POS_Y;

    /**
     * The constructor of a Position
     * posX and posY are initialized here
     *
     * @param posX x Position
     * @param posY y Position
     */
    public Position(int posX, int posY) {
        this.POS_X = posX;
        this.POS_Y = posY;
    }

    //--------------getter / setter---------------------------------------------------------------------------------------------------------------

    /**
     * return x-position
     *
     * @return x-position
     */
    public int getPOS_X() {
        return this.POS_X;
    }

    /**
     * return y-position
     *
     * @return y-position
     */
    public int getPOS_Y() {
        return this.POS_Y;
    }

    /**
     * proofs if two positions are equal
     *
     * @param other the other position
     * @return if two positions are equal
     */

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Position position = (Position) other;
        return POS_X == position.POS_X && POS_Y == position.POS_Y;
    }

    /**
     * return hashCode
     *
     * @return hashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(POS_X, POS_Y);
    }
}
