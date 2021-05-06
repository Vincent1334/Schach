package chess.model;

public class Move {

    private Position actualPosition;
    private Position targetPosition;
    private int pawnConversionTo;

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
}
