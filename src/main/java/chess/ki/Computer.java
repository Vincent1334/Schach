package chess.ki;

import chess.figures.Figure;
import chess.figures.None;
import chess.model.Board;
import chess.model.Move;
import java.util.List;

public class Computer {

    private boolean isBlack;

    public Computer(boolean isBlack) {
        this.isBlack = isBlack;
    }

    public Move makeMove(Board board) {
        Node parentNode = new Node(null, board, isBlack, 4, -10000, 10000);

        return parentNode.bestMove();
    }

    public static int heuristic(Board board, boolean isBlack) {

        //check Material
        int[][] material = new int[2][6];

        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                if(!(board.getFigure(x, y) instanceof None)){
                    switch(board.getFigure(x, y).getFigureID()){
                        case 1: material[board.getFigure(x, y).isBlackTeam() ? 1 : 0][0] ++; break;
                        case 2: material[board.getFigure(x, y).isBlackTeam() ? 1 : 0][1] ++; break;
                        case 3: material[board.getFigure(x, y).isBlackTeam() ? 1 : 0][2] ++; break;
                        case 4: material[board.getFigure(x, y).isBlackTeam() ? 1 : 0][3] ++; break;
                        case 5: material[board.getFigure(x, y).isBlackTeam() ? 1 : 0][4] ++; break;
                        case 6: material[board.getFigure(x, y).isBlackTeam() ? 1 : 0][5] ++; break;
                    }
                }
            }
        }

                //King material
        return 200*(material[isBlack ? 1 : 0][5]-material[isBlack ? 0 : 1][5])
                //Queen material
                + 9*(material[isBlack ? 1 : 0][4]-material[isBlack ? 0 : 1][4])
                //Rook material
                + 5*(material[isBlack ? 1 : 0][1]-material[isBlack ? 0 : 1][1])
                //Bishop and knight material
                + 3*((material[isBlack ? 1 : 0][2]-material[isBlack ? 0 : 1][2]) + (material[isBlack ? 1 : 0][3]-material[isBlack ? 0 : 1][3]))
                //pawn material
                + (material[isBlack ? 1 : 0][0]-material[isBlack ? 0 : 1][0]);
    }
}

