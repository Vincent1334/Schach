package chess.model;

/**
 * This class contains the information about the queens valid movements
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-05
 */

public class Queen extends Figure {

    /**
     * The constructor of a queen
     * The queens team and figure ID are initialized here.
     */
    public Queen(int team) {
        super(team);
        super.figureID = 5;
    }

    /**
     * The copy constructor of this class
     * @param sourceClass
     */
    public Queen(Queen sourceClass) {
        super(sourceClass.team);
        super.alreadyMoved = sourceClass.alreadyMoved;
        super.figureID = 5;
    }


    /**
     * Proofs if the move is a valid move for Queen
     *
     * @param actualPos actual position for Queen
     * @param targetPos new input position for Queen
     * @param board     actual state of chessboard
     * @return whether move was successful
     */
    @Override
    public boolean validMove(Position actualPos, Position targetPos, Board board) {

        int posX = actualPos.getPosX();
        int posY = actualPos.getPosY();
        int newX = targetPos.getPosX();
        int newY = targetPos.getPosY();

        //Is the new position valid
        for (int i = -8; i < 9; i++) {
            if ((newX == posX + i && newY == posY + i)
                    || (newX == posX - i && newY == posY + i)
                    || (newX == posX + i && newY == posY - i)
                    || (newX == posX) || (newY == posY)) {

                //is there a figure in the way

                //new position upper right
                if (posX - newX < 0 && posY - newY < 0) {
                    for (int j = 1; j < Math.abs(posX - newX); j++) {
                        if (!(board.getFigure(posX + j, posY + j) instanceof None)) {
                            return false;
                        }
                    }
                }
                //new position upper left
                if (posX - newX > 0 && posY - newY < 0) {
                    for (int j = 1; j < Math.abs(posX - newX); j++) {
                        if (!(board.getFigure(posX - j, posY + j) instanceof None)) {
                            return false;
                        }
                    }
                }
                //new position bottom right
                if (posX - newX < 0 && posY - newY > 0) {
                    for (int j = 1; j < Math.abs(posX - newX); j++) {
                        if (!(board.getFigure(posX + j, posY - j) instanceof None)) {
                            return false;
                        }
                    }
                }
                //new position bottom left
                if (posX - newX > 0 && posY - newY > 0) {
                    for (int j = 1; j < Math.abs(posX - newX); j++) {
                        if (!(board.getFigure(posX - j, posY - j) instanceof None)) {
                            return false;
                        }
                    }
                }
                //new position horizontal, right
                if (posX - newX < 0) {
                    for (int j = 1; j < Math.abs(posX - newX); j++) {
                        if (!(board.getFigure(posX + j, posY) instanceof None)) {
                            return false;
                        }
                    }
                }
                //new position horizontal, left
                if (posX - newX > 0) {
                    for (int j = 1; j < Math.abs(posX - newX); j++) {
                        if (!(board.getFigure(posX - j, posY) instanceof None)) {
                            return false;
                        }
                    }
                }
                //new position vertical, up
                if (posY - newY < 0) {
                    for (int j = 1; j < Math.abs(posY - newY); j++) {
                        if (!(board.getFigure(posX, posY + j) instanceof None)) {
                            return false;
                        }
                    }
                }
                //new position vertical, down
                if (posY - newY > 0) {
                    for (int j = 1; j < Math.abs(posY - newY); j++) {
                        if (!(board.getFigure(posX, posY - j) instanceof None)) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public char getSymbol() {
        return team == 0 ? '\u265B' : '\u2655';
    }
}

