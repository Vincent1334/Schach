package chess.model;

import java.util.Objects;

public class Move {

    private Position actualPosition;
    private Position targetPosition;
    private int pawnConversionTo = 5;

    public Move(Position actualPosition, Position targetPosition, int pawnConversionTo) {
        this.actualPosition = actualPosition;
        this.targetPosition = targetPosition;
        this.pawnConversionTo = pawnConversionTo;
    }

    public Move(Position actualPosition, Position targetPosition) {
        this.actualPosition = actualPosition;
        this.targetPosition = targetPosition;
    }


    public Position getActualPosition() {
        return actualPosition;
    }

    public Position getTargetPosition() {
        return targetPosition;
    }

    public int getPawnConversionTo() {
        return pawnConversionTo;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Move move = (Move) other;
        return pawnConversionTo == move.pawnConversionTo && Objects.equals(actualPosition, move.actualPosition) && Objects.equals(targetPosition, move.targetPosition);
    }
}
