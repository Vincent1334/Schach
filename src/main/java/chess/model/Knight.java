package chess.model;

/**
 * This class contains the information about the knights valid movements
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-05
 *
 */

public class Knight extends Figure {

    /**
     * The constructor of a knight
     * The knights team and figure ID are is initialized here.
     */
    public Knight(int team) {
        super(team);
        super.figureID = 3;
    }

    /**
     * The copy constructor of this class
     * @param sourceClass
     */
    public Knight(Knight sourceClass){
        super(sourceClass.team);
        super.alreadyMoved = sourceClass.alreadyMoved;
        super.figureID = 3;
    }

    /**
     * Proofs if the move is a valid move for Knight
     * @param actualPos actual position for Knight
     * @param targetPos new input position for Knight
     * @param board actual state of chessboard
     * @return whether move was successful
     */

    @Override
    public boolean validMove(Position actualPos, Position targetPos, Board board) {

        int posX = actualPos.getPosX();
        int posY = actualPos.getPosY();
        int newX = targetPos.getPosX();
        int newY = targetPos.getPosY();

        return (posX + 1 == newX && posY + 2 == newY || posX + 1 == newX && posY - 2 == newY
                || posX - 1 == newX && posY + 2 == newY || posX - 1 == newX && posY - 2 == newY
                || posX + 2 == newX && posY + 1 == newY || posX + 2 == newX && posY - 1 == newY
                || posX - 2 == newX && posY + 1 == newY || posX - 2 == newX && posY - 1 == newY);
    }

    /*@Override
    public char getSymbol() {
        return team == 0 ?  '\u265E' : '\u2658';
    }*/
    @Override
    public char getSymbol() {
        return team == 0 ?  'N' : 'n';
    }



}

