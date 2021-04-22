package chess.model;

public class Queen extends Figure {

    public Queen(int team) {
        super(team);
    }

    /**
     * Proofs if the move is a valid move for Queen and makes move if it is
     * @param newX new input x-position for Queen
     * @param newY new input y-position for Queen
     * @param board actual state of chessboard
     * @return whether move was successful
     */
    public boolean validMove(int posX, int posY, int newX, int newY, Board board) {

        //Is the new position valid
        for (int i=-8; i<9; i++) {
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
        return team == 0 ?  '\u265B' : '\u2655';
    }

}

