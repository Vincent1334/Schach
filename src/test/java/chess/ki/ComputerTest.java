package chess.ki;

import chess.controller.CoreGame;
import chess.figures.*;
import chess.model.Board;
import chess.model.Move;
import chess.model.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains information to test the AI
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-01
 */
class ComputerTest {

    @Test
    public void heuristic(){
        short[][] board01 ={{0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 11, 0, 0, 0, 8, 0, 0},
                            {0, 0, 1, 0, 0, 1, 0, 0},
                            {0, 0, 0, 1, 1, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0}};

        short[][] board02 ={{0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 11, 0, 0, 0, 8, 0, 0},
                            {0, 0, 1, 0, 0, 1, 0, 0},
                            {0, 0, 0, 1, 1, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0}};







    }

    public Board createBoard(short[][] array){
        Board tmpBoard = new Board();

        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                switch(array[x][y]){
                    case 0: tmpBoard.setFigure(x, y, new None()); break;
                    case 1: tmpBoard.setFigure(x, y, new Pawn(false)); break;
                    case 2: tmpBoard.setFigure(x, y, new Rook(false)); break;
                    case 3: tmpBoard.setFigure(x, y, new Knight(false)); break;
                    case 4: tmpBoard.setFigure(x, y, new Bishop(false)); break;
                    case 5: tmpBoard.setFigure(x, y, new Queen(false)); break;
                    case 6: tmpBoard.setFigure(x, y, new King(false)); break;
                    case 7: tmpBoard.setFigure(x, y, new Pawn(true)); break;
                    case 8: tmpBoard.setFigure(x, y, new Rook(true)); break;
                    case 9: tmpBoard.setFigure(x, y, new Knight(true)); break;
                    case 10: tmpBoard.setFigure(x, y, new Bishop(true)); break;
                    case 11: tmpBoard.setFigure(x, y, new Queen(true)); break;
                    case 12: tmpBoard.setFigure(x, y, new King(true)); break;
                }
            }
        }
        return tmpBoard;
    }
}