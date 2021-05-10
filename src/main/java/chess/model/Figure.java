package chess.model;

import java.util.Objects;

/**
 * This abstract class contains the method that are implemented in all figures
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-010
 */
public abstract class Figure {

    boolean alreadyMoved = false;
    int team, figureID;

    /**
     * Constructor for default figures
     *
     * @param team of the figure (0=white, 1=black, 2=none)
     */
    public Figure(int team) {
        this.team = team;
    }

    /**
     * Constructor for None Figures
     */
    public Figure() {
    }

    /**
     *
     * @param actualPos the actual position of the figure
     * @param targetPos the target position of the figure
     * @param board the current board
     * @return whether the move is valid or not
     */
    public abstract boolean validMove(Position actualPos, Position targetPos, Board board);

    /**
     * the getter for the team
     * @return the team of the figure
     */
    public int getTeam() {
        return this.team;
    }

    /**
     * The getter for the figures symbol
     * @return the figures symbol
     */
    public abstract char getSymbol();

    /**
     *
     * @return whether the figure has already been moved
     */
    public boolean isAlreadyMoved() {
        return alreadyMoved;
    }

    /**
     *
     * @param alreadyMoved has the figure been already moved?
     */
    public void setAlreadyMoved(boolean alreadyMoved) {
        this.alreadyMoved = alreadyMoved;
    }

    /**
     * getter for the figure ID
     * @return the figure ID
     */
    public int getFigureID() {
        return this.figureID;
    }

    /**
     *
     * @param other an object
     * @return whether the objects are equal but not identical
     */
    @Override
    public boolean equals(Object other){
        if(other instanceof Figure) {
            Figure figure1 = (Figure) other;
            return figure1.getFigureID() == figureID && figure1.getTeam() == team && figure1.isAlreadyMoved() == alreadyMoved;
        }
        return false;
    }

    /**
     *
     * @return hash code of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(alreadyMoved, team, figureID);
    }
}
