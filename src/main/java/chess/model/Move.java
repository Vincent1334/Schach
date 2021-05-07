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
     * @param actualPosition
     * @param targetPosition
     * @param pawnConversionTo
     */
    public Move(Position actualPosition, Position targetPosition, int pawnConversionTo) {
        this.actualPosition = actualPosition;
        this.targetPosition = targetPosition;
        this.pawnConversionTo = pawnConversionTo;
    }

    /**
     * Manager move parameters with default pawn conversion
     * @param actualPosition
     * @param targetPosition
     */
    public Move(Position actualPosition, Position targetPosition) {
        this.actualPosition = actualPosition;
        this.targetPosition = targetPosition;
    }

    /**
     * return actual position
     * @return actualPosition
     */
    public Position getActualPosition() {
        return actualPosition;
    }

    /**
     * return target position
     * @return targetPosition
     */
    public Position getTargetPosition() {
        return targetPosition;
    }

    /**
     * return PawnConversion ID
     * @return pawnConversion
     */
    public int getPawnConversionTo() {
        return pawnConversionTo;
    }

    /**
     * Override equals
     * @param other
     * @return
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
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(actualPosition, targetPosition, pawnConversionTo);
    }
}
