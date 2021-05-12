package chess.model;


import java.util.Objects;

/**
 * This class contains the information about a Position
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-05
 */
public class Position {

    private int posX, posY;

    /**
     * The constructor of a Position
     * posX and posY are initialized here
     *
     * @param posX x Position
     * @param posY y Position
     */
    public Position(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    /**
     * return x-position
     *
     * @return x-position
     */
    public int getPosX() {
        return this.posX;
    }

    /**
     * return y-position
     *
     * @return y-position
     */
    public int getPosY() {
        return this.posY;
    }

    /**
     * proofs if two positions are equal
     *
     * @param other the other position
     * @return if two positions are equal
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Position position = (Position) other;
        return posX == position.posX && posY == position.posY;
    }

    /**
     * return hashCode
     *
     * @return hashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(posX, posY);
    }
}
