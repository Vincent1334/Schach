package chess.model;

public class Board {

    private Figure[][] board;

    /**
     * Creates a new board with the standard figure setup
     */
    public Board() {
        board = new Figure[8][8];

        for (int team = 0; team <= 1; team++) {
            //create Pawns
            for (int i = 0; i < 8; i++) {
                board[i][1 + team * 5] = new Pawn(team);
            }
            //create King
            board[4][team * 7] = new King(team);

            //create Queen
            board[3][team * 7] = new Queen(team);

            //create Rook
            for (int i = 0; i <= 1; i++) {
                board[i * 7][team * 7] = new Rook(team);
            }
            //create Bishop
            for (int i = 0; i <= 1; i++) {
                board[2 + i * 3][team * 7] = new Bishop(team);
            }
            //create Knight
            for (int i = 0; i <= 1; i++) {
                board[1 + i * 5][team * 7] = new Knight(team);
            }
        }
        //create None
        for (int y = 2; y < 6; y++) {
            for (int x = 0; x < 8; x++) {
                board[x][y] = new None();
            }
        }
    }

    /**
     * copies actual board
     *
     * @return copy of the actual board
     */
    public Board copyBoard() {
        Board copy = new Board();
//        copy.setBoard(this.getBoard());
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                copy.setFigure(i, j, this.getFigure(i, j));
            }
        }
        return copy;
    }

    /*public void setBoard(Figure[][] board){
        this.board = board;
    }*/

    public Figure[][] getBoard() {
        return board;
    }

    public Figure getFigure(int x, int y) {
        return board[x][y];
    }

    public void setFigure(int x, int y, Figure figure) {
        board[x][y] = figure;
    }

    /**
     * Get the position of the target king
     *
     * @param team
     * @return position of target king, {0,0} if there is no king found
     */
    public int[] getKing(int team) {
        //Searching target King position
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (board[x][y] instanceof King && board[x][y].getTeam() == team) {
                    int[] pos = {x, y};
                    return pos;
                }
            }
        }
        int[] pos = {0, 0};
        return pos;
    }
}
