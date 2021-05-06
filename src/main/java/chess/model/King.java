package chess.model;

/**
 * This class contains the information about the kings valid movements
 *
 * @author Lydia Engelhardt, Sofia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-05
 *
 */

public class King extends Figure {

    public King(int team) {
        super(team);
        super.figureID = 6;
    }

    public King(King sourceClass){
        super(sourceClass.team);
        super.alreadyMoved = sourceClass.alreadyMoved;
        super.figureID = 6;
    }

    /**
     * Proofs if the move is a valid move for King
     *
     * @param actualPos actual position for King
     * @param targetPos new input position for King
     * @param board actual state of chessboard
     * @return whether move was successful
     */

    @Override
    public boolean validMove(Position actualPos, Position targetPos, Board board) {

        int posX = actualPos.getPosX();
        int posY = actualPos.getPosY();
        int newX = targetPos.getPosX();
        int newY = targetPos.getPosY();

        // normal move
        return (newX == posX + 1 || newX == posX - 1 || newX == posX)
                && (newY == posY + 1 || newY == posY - 1 || newY == posY)
                && !(newX == posX && newY == posY);
    }

    @Override
    public char getSymbol() {
        return team == 0 ? '\u265A' : '\u2654';
    }



}
