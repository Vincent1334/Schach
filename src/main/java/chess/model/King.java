package chess.model;

public class King extends Figure {

    public King(int posX, int posY, int team) {
        super(posX, posY, team);
    }

    /**
     * Proofs if the move is a valid move for King and makes move if it is
     * @param newX new input x-position for King
     * @param newY new input y-position for King
     * @param board actual state of chessboard
     * @return whether move was successful
     */
    public boolean validMove(int newX, int newY, Board board) {
        // black castle queenside (left)
        if (!alreadyMoved && newX == posX-2 && !board.getFigure(0,posY).isAlreadyMoved()) {
            board.setFigure(newX+1,newY,board.getFigure(0,posY));
            // TODO: wir erzeugen bei jedem Zug neue None-Figuren...
            board.setFigure(0,0,new None(0,0,1));
            return true;
        }
        // black castle kingside (right)
        if (!alreadyMoved && newX == posX+2 && !board.getFigure(0,posY).isAlreadyMoved()) {
            board.setFigure(newX-1,newY,board.getFigure(0,posY));
            board.setFigure(7,0,new None(7,0,1));
            return true;
        }
        // white castle kingside (left)
        if (!alreadyMoved && newX == posX+2 && !board.getFigure(7,posY).isAlreadyMoved()) {
            board.setFigure(newX-1,newY,board.getFigure(7,posY));
            board.setFigure(0,7,new None(0,7,0));
            return true;
        }
        // white castle queenside (right)
        if (!alreadyMoved && newX == posX+2 && !board.getFigure(7,posY).isAlreadyMoved()) {
            board.setFigure(newX-1,newY,board.getFigure(7,posY));
            board.setFigure(7,7,new None(7,7,0));
            return true;
        }
        if (newX == posX+1 || newX == posX-1 || newY == posY+1 || newY == posY-1) {
            return true;
        }
        return false;
    }

    @Override
    public char getSymbol() {
        return team == 0 ? '\u2654' : '\u265A';
    }

}
