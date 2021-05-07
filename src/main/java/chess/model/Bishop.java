package chess.model;

/**
 * This class contains the information about the bishops valid movements
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-05
 */

public class Bishop extends Figure {

    /**
     * The constructor of a bishop
     * The bishops team and figure ID are initialized here.
     */
    public Bishop(int team) {
        super(team);
        super.figureID = 4;
    }

    /**
     * The copy constructor of this class
     *
     * @param sourceClass
     */
    public Bishop(Bishop sourceClass) {
        super(sourceClass.team);
        super.alreadyMoved = sourceClass.alreadyMoved;
        super.figureID = 4;
    }

    /**
     * Proofs if the move is a valid move for Bishop
     *
     * @param actualPos actual position for Bishop
     * @param targetPos new input position for Bishop
     * @param board     actual state of chessboard
     * @return whether move was successful
     */

    @Override
    public boolean validMove(Position actualPos, Position targetPos, Board board) {

        int posX = actualPos.getPosX();
        int posY = actualPos.getPosY();
        int newX = targetPos.getPosX();
        int newY = targetPos.getPosY();

        if (actualPos.getPosX() == targetPos.getPosX() && actualPos.getPosY() == targetPos.getPosY()) {
            return false;
        }

        //Is the new position on a diagonal from the old position
        for (int i = -8; i < 9; i++) {
            if ((newX == posX + i && newY == posY + i)
                    || (newX == posX - i && newY == posY + i)
                    || (newX == posX + i && newY == posY - i)) {
                //is between the old position and the new position a figure
                //new position on the upper right
                if (posX - newX < 0 && posY - newY < 0) {
                    for (int j = 1; j < Math.abs(posX - newX); j++) {
                        if (!(board.getFigure(posX + j, posY + j) instanceof None)) {
                            return false;
                        }
                    }
                }
                //new position on the upper left
                if (posX - newX > 0 && posY - newY < 0) {
                    for (int j = 1; j < Math.abs(posX - newX); j++) {
                        if (!(board.getFigure(posX - j, posY + j) instanceof None)) {
                            return false;
                        }
                    }
                }
                //new position on the bottom right
                if (posX - newX < 0 && posY - newY > 0) {
                    for (int j = 1; j < Math.abs(posX - newX); j++) {
                        if (!(board.getFigure(posX + j, posY - j) instanceof None)) {
                            return false;
                        }
                    }
                }
                //new position on the bottom left
                if (posX - newX > 0 && posY - newY > 0) {
                    for (int j = 1; j < Math.abs(posX - newX); j++) {
                        if (!(board.getFigure(posX - j, posY - j) instanceof None)) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }


    /*@Override
    public char getSymbol() {
        return team == 0 ? '\u265D' : '\u2657';
    }*/
    @Override
    public char getSymbol() {
        return team == 0 ? 'B' : 'b';
    }

}
