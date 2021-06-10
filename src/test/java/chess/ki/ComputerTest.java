package chess.ki;

import chess.controller.CoreGame;
import chess.figures.*;
import chess.model.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains information to test the AI
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-01
 */
public class ComputerTest {

    @Test
    public void testKI(){
        try {
            CoreGame testGame = new CoreGame();
            Computer testPlayerWhite = new Computer(false);
            Computer testPlayerBlack = new Computer(true);

            testPlayerWhite.makeMove(testGame.getCurrentBoard());
            testPlayerWhite.getThread().join();

            assertTrue(testGame.chessMove(testPlayerWhite.getMove()));

            testPlayerBlack.makeMove(testGame.getCurrentBoard());
            testPlayerBlack.getThread().join();

            assertTrue(testGame.chessMove(testPlayerBlack.getMove()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }















    /**
     * Convert Array to Board
     * @param array Array with figureIDs
     * @return  converted Board
     */
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