package chess.model;

import java.util.Objects;

/**
 * Handel move position
 */
public class Move {

    private Position actualPosition;
    private Position targetPosition;
    private int pawnConversionTo = 5;

    /**
     * Manage move parameters with custom pawn conversion
     *
     * @param actualPosition   the start position of the move
     * @param targetPosition   the target position of the move
     * @param pawnConversionTo the id of the figure the pawn should be converted to
     */
    public Move(Position actualPosition, Position targetPosition, int pawnConversionTo) {
        this.actualPosition = actualPosition;
        this.targetPosition = targetPosition;
        this.pawnConversionTo = pawnConversionTo;
    }

    /**
     * Manager move parameters with default pawn conversion
     *
     * @param actualPosition the start position of the move
     * @param targetPosition the target position of the move
     */
    public Move(Position actualPosition, Position targetPosition) {
        this.actualPosition = actualPosition;
        this.targetPosition = targetPosition;
    }


    /**
     * returns a move out of characters as a string
     *
     * @return move out of characters as a string
     */
    @Override
    public String toString() {
        return Character.toString(actualPosition.getPosX() + 97) + (actualPosition.getPosY() + 1) + "-" +
                Character.toString(targetPosition.getPosX() + 97) + (targetPosition.getPosY() + 1) + getConversionLetter(pawnConversionTo);
    }

    /**
     * return actual position
     *
     * @return actualPosition
     */
    public Position getActualPosition() {
        return actualPosition;
    }

    /**
     * return target position
     *
     * @return targetPosition
     */
    public Position getTargetPosition() {
        return targetPosition;
    }

    /**
     * return PawnConversion ID
     *
     * @return pawnConversion
     */
    public int getPawnConversionTo() {
        return pawnConversionTo;
    }

    /**
     * Override equals
     *
     * @param other the compared Move
     * @return whether the Moves are semantically equal
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Move move = (Move) other;
        return pawnConversionTo == move.pawnConversionTo && Objects.equals(actualPosition, move.actualPosition) && Objects.equals(targetPosition, move.targetPosition);
    }

    /**
     * Override hashCode
     *
     * @return the hash code of a move
     */
    @Override
    public int hashCode() {
        return Objects.hash(actualPosition, targetPosition, pawnConversionTo);
    }

    /**
     * Return Figure Letter
     *
     * @param pawnConversion ID of the figure you want the pawn to convert to
     * @return Letter of the figure you want the pawn to convert to
     */
    public String getConversionLetter(int pawnConversion) {
        switch (pawnConversion) {
            case 1:
                return "P";
            case 2:
                return "R";
            case 3:
                return "N";
            case 4:
                return "B";
            case 5:
                return "";
            case 6:
                return "K";
        }
        return "";
    }
}
