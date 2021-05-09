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

        //Is the target position the actual position
        if (actualPos.getPosX() == targetPos.getPosX() && actualPos.getPosY() == targetPos.getPosY()) {
            return false;
        }

        //Is the new position on a diagonal from the old position
        if(!targetPositionOnDiagonal(actualPos, targetPos)){
           return false;
        }

        //is between the old position and the new position a figure
        return !figureBetweenActualAndTargetPosition(actualPos, targetPos, board);
    }

    private boolean targetPositionOnDiagonal(Position actualPos, Position targetPos){
        int posX = actualPos.getPosX();
        int posY = actualPos.getPosY();
        int newX = targetPos.getPosX();
        int newY = targetPos.getPosY();

        for (int i = 1; i <= Math.abs(posX - newX); i++) {
            boolean y = newY == posY + i || newY == posY - i;
            if ((newX == posX + i && y) || (newX == posX - i && y)) {
                return true;
            }
        }
        return false;
    }

    private boolean figureBetweenActualAndTargetPosition(Position actualPos, Position targetPos, Board board){
        int posX = actualPos.getPosX();
        int posY = actualPos.getPosY();
        int newX = targetPos.getPosX();
        int newY = targetPos.getPosY();

            //new position on the upper right
            if (posX - newX < 0 && posY - newY < 0) {
                return upperRight(actualPos,targetPos,board);
            }
            //new position on the upper left
            if (posX - newX > 0 && posY - newY < 0) {
                return upperLeft(actualPos,targetPos,board);
            }
            //new position on the bottom right
            if (posX - newX < 0 && posY - newY > 0) {
                return bottomRight(actualPos,targetPos,board);
            }
            //new position on the bottom left
            if (posX - newX > 0 && posY - newY > 0) {
                return bottomLeft(actualPos,targetPos,board);
            }
        return false;
    }

    private boolean upperRight(Position actualPos, Position targetPos, Board board){
        int posX = actualPos.getPosX();
        int posY = actualPos.getPosY();
        int newX = targetPos.getPosX();

        for (int j = 1; j <= Math.abs(posX - newX); j++) {
            if(!(board.getFigure(posX + j, posY + j) instanceof None)){
                return true;
            }
        }
        return false;
    }

    private boolean upperLeft(Position actualPos, Position targetPos, Board board){
        int posX = actualPos.getPosX();
        int posY = actualPos.getPosY();
        int newX = targetPos.getPosX();

        for (int j = 1; j <= Math.abs(posX - newX); j++) {
            if(!(board.getFigure(posX - j, posY + j) instanceof None)){
                return true;
            }
        }
        return false;
    }

    private boolean bottomRight(Position actualPos, Position targetPos, Board board){
        int posX = actualPos.getPosX();
        int posY = actualPos.getPosY();
        int newX = targetPos.getPosX();

        for (int j = 1; j <= Math.abs(posX - newX); j++) {
            if(!(board.getFigure(posX + j, posY - j) instanceof None)){
                return true;
            }
        }
        return false;
    }

    private boolean bottomLeft(Position actualPos, Position targetPos, Board board){
        int posX = actualPos.getPosX();
        int posY = actualPos.getPosY();
        int newX = targetPos.getPosX();

        for (int j = 1; j <= Math.abs(posX - newX); j++) {
            if(!(board.getFigure(posX - j, posY - j) instanceof None)){
                return true;
            }
        }
        return false;
    }

    @Override
    public char getSymbol() {
        return team == 0 ? 'B' : 'b';
    }

}
