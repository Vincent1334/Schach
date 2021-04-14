package chess.model;

public class Board {

    private Figure[][] board;

    /**
     * Creates a new board with the standard figure setup
     */
    public Board(){
        board = new Figure[8][8];

        for(int team = 0; team <= 1; team++){
            //create Pawns
            for(int i = 0; i < 8; i++) board[i][1+team*7] = new Pawn(i, 1+team*7, team);
            //create King
            board[4][team*7] = new King(4, team*7, team);
            //create Queen
            board[3][team*7] = new Queen(3, team*7, team);
            //create Rook
            for(int i = 0; i <= 1; i++) board[i*7][team*7] = new Rook(i*7, team*7, team);
            //create Bishop
            for(int i = 0; i <= 1; i++) board[2+i*3][team*7] = new Bishop(2+i*3, team*7, team);
            //create Knight
            for(int i = 0; i <= 1; i++) board[1+i*5][team*7] = new Knight(2+i*5, team*7, team);
        }
    }

    /**
     *
     * @return The complete chess board
     */
    public Figure[][] getBoard(){
        return board;
    }

    /**
     *
     * @param x The x position of the figure you are looking for
     * @param y The y position of the figure you are looking for
     * @return The wanted figure
     */
    public Figure getFigure(int x, int y){
        return board[x][y];
    }
}
