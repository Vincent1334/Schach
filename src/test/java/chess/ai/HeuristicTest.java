package chess.ai;

import chess.figures.*;
import chess.model.Board;
import chess.model.Move;
import chess.model.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains tests to check whether the computer determines the right move
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-10
 */
public class HeuristicTest {

    /**
     * tests whether the computer repeats moves
     */
    @Test
    public void testCheckRepeat() {
        Move bestMove = new Move(new Position(0, 0), new Position(0, 0));
        Move lastMove = new Move(new Position(0, 0), new Position(0, 0));

        Figure testFigure01 = new Bishop(true);
        Figure testFigure02 = new Pawn(true);

        bestMove.setActualFigure(testFigure01);
        bestMove.setTargetFigure(new None());

        lastMove.setActualFigure(testFigure01);
        lastMove.setTargetFigure(new None());

        assertNotEquals(0, Heuristic.checkRepeat(bestMove, lastMove, true), "checkRepeat test fail");

        lastMove.setActualFigure(testFigure02);

        assertEquals(0, Heuristic.checkRepeat(bestMove, lastMove, true), "checkRepeat test fail");
    }


    /**
     * tests whether the figure score for each figure is correct
     */
    @Test
    public void testCheckFigureScore() {
        Move bestMove = new Move(new Position(0, 0), new Position(0, 0));

        Figure[] testFigure = new Figure[5];
        testFigure[0] = new Pawn(false);
        testFigure[1] = new Rook(false);
        testFigure[2] = new Knight(false);
        testFigure[3] = new Bishop(false);
        testFigure[4] = new Queen(false);


        for (int i = 0; i < 5; i++) {
            bestMove.setActualFigure(testFigure[i]);
            assertTrue(0 < Heuristic.checkFigureScore(bestMove, false));
        }

        assertTrue(0 <= Heuristic.checkFigureScore(bestMove, false));
        assertTrue(0 >= Heuristic.checkFigureScore(bestMove, true));
    }

    /**
     * tests whether the material score is correct
     */
    @Test
    public void testCheckMaterial() {
        int[][] material01 = new int[][]{{3, 2, 4, 5, 3, 7}, {1, 1, 1, 1, 1, 1}};
        int[][] material02 = new int[][]{{1, 1, 1, 1, 1, 1}, {3, 2, 4, 5, 3, 7}};

        assertTrue(Heuristic.checkMaterial(material01, false) > Heuristic.checkMaterial(material02, false), "checkMaterial test fail");
    }

    /**
     * tests whether the check castling flag is noticed correctly
     */
    @Test
    public void testCheckCastling() {
        Board testBoard = new Board();
        testBoard.setCastlingFlag(true, false);

        assertTrue(0 > Heuristic.checkCastling(testBoard, true), "Castling test fail");
        assertTrue(0 < Heuristic.checkCastling(testBoard, false), "Castling test fail");
    }

    /**
     * tests whether the check chess flag is noticed correctly
     */
    @Test
    public void testCheckChess() {
        Board testBoard = new Board();

        assertEquals(0, Heuristic.checkChess(testBoard, false), "Check test fail");
        assertEquals(0, Heuristic.checkChess(testBoard, true), "Check test fail");

        testBoard.setCheckFlag(true, false);

        assertNotEquals(0, Heuristic.checkChess(testBoard, false), "Check test fail");
    }

    /**
     * tests whether the endgame points set correctly
     */
    @Test
    public void testCheckEndGame(){
        Board testBoard = new Board();
        //Delete black Knights
        testBoard.setFigure(1, 7, new None());
        testBoard.setFigure(6, 7, new None());

        assertTrue(Heuristic.checkEndGame(testBoard, true, true) > Heuristic.checkEndGame(testBoard, false, true), "Endgame test fail");

        //Delete white Knights
        testBoard.setFigure(1, 0, new None());
        testBoard.setFigure(6, 0, new None());

        assertEquals(Heuristic.checkEndGame(testBoard, true, true), Heuristic.checkEndGame(testBoard, false, true), "Endgame test fail");
    }
}