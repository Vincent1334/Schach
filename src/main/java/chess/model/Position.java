package chess.model;


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
     * @param posX x Position
     * @param posY y Position
     */
    public Position(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }

    /**
     * proofs if two positions are equal
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

}
