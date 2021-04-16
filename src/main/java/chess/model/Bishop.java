package chess.model;

public class Bishop extends Figure {

    public Bishop(int posX, int posY, int team) {
        super(posX, posY, team);
    }

    /**
     * Proofs if the move is a valid move for Bishop and makes move if it is
     * @param newX new input x-position for Bishop
     * @param newY new input y-position for Bishop
     * @param board actual state of chessboard
     * @return whether move was successful
     */
    public boolean validMove(int newX, int newY, Board board) {

                //Is the new position on a diagonal from the old position
                for (int i=-8; i<9; i++) {
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


    @Override
    public char getSymbol() {
        return team == 0 ? '\u2657' : '\u265D';
    }

}
