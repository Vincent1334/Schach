package chess.model;

/**
 * This class contains the information about the rooks valid movements
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-05
 */

public class Rook extends Figure {

    /**
     * The constructor of a rook
     * The rooks team and figure ID are initialized here.
     */
    public Rook(int team) {
        super(team);
        super.figureID = 2;
    }

    /**
     * The copy constructor of this class
     * @param sourceClass
     */
    public Rook(Rook sourceClass) {
        super(sourceClass.team);
        super.alreadyMoved = sourceClass.alreadyMoved;
        super.figureID = 2;
    }

    /**
     * Proofs if the move is a valid move for Rook
     *
     * @param actualPos actual position of the Rook
     * @param targetPos new input position for Rook
     * @param board     actual state of chessboard
     * @return whether move was successful
     */
    @Override
    public boolean validMove(Position actualPos, Position targetPos, Board board) {
        // is a figure between the old position and the new position?
        return moveRight(actualPos,targetPos,board)||moveLeft(actualPos,targetPos,board)||moveUp(actualPos,targetPos,board)||moveDown(actualPos,targetPos,board);
    }

    /**
     * tests if the rook moves right and that there are no figures between the actual- and target-position
     * @param actualPos actual position of the Rook
     * @param targetPos new input position for Rook
     * @param board actual state of chessboard
     * @return if the rook moves right and that there are no figures between the actual- and target-position
     */
    private boolean moveRight(Position actualPos, Position targetPos, Board board){
        int posX = actualPos.getPosX();
        int posY = actualPos.getPosY();
        int newX = targetPos.getPosX();
        int newY = targetPos.getPosY();

        if (newY == posY && newX > posX) {
            for (int j = 1; j <= Math.abs(posX - newX) - 1; j++) {
                if (!(board.getFigure(posX + j, posY) instanceof None)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * tests if the rook moves left and that there are no figures between the actual- and target-position
     * @param actualPos actual position of the Rook
     * @param targetPos new input position for Rook
     * @param board actual state of chessboard
     * @return if the rook moves left and that there are no figures between the actual- and target-position
     */
    private boolean moveLeft(Position actualPos, Position targetPos, Board board){
        int posX = actualPos.getPosX();
        int posY = actualPos.getPosY();
        int newX = targetPos.getPosX();
        int newY = targetPos.getPosY();

        if (newY == posY && newX < posX) {
            for (int j = 1; j <= Math.abs(posX - newX) - 1; j++) {
                if (!(board.getFigure(posX - j, posY) instanceof None)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * tests if the rook moves upwards and that there are no figures between the actual- and target-position
     * @param actualPos actual position of the Rook
     * @param targetPos new input position for Rook
     * @param board actual state of chessboard
     * @return if the rook moves upwards and that there are no figures between the actual- and target-position
     */
    private boolean moveUp(Position actualPos, Position targetPos, Board board){
        int posX = actualPos.getPosX();
        int posY = actualPos.getPosY();
        int newX = targetPos.getPosX();
        int newY = targetPos.getPosY();

        if (newX == posX && newY > posY) {
            for (int j = 1; j <= (Math.abs(posY - newY) - 1); j++) {
                if (!(board.getFigure(posX, posY + j) instanceof None)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * tests if the rook moves downwards and that there are no figures between the actual- and target-position
     * @param actualPos actual position of the Rook
     * @param targetPos new input position for Rook
     * @param board actual state of chessboard
     * @return if the rook moves downwards and that there are no figures between the actual- and target-position
     */
    private boolean moveDown(Position actualPos, Position targetPos, Board board){
        int posX = actualPos.getPosX();
        int posY = actualPos.getPosY();
        int newX = targetPos.getPosX();
        int newY = targetPos.getPosY();

        if (newX == posX && newY < posY) {
            for (int j = 1; j <= Math.abs(posY - newY) - 1; j++) {
                if (!(board.getFigure(posX, posY - j) instanceof None)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    @Override
    public char getSymbol() {
        return team == 0 ? 'R' : 'r';
    }

}