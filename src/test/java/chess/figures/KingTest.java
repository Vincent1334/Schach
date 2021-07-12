package chess.figures;

import chess.model.Board;
import chess.model.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains tests to check the kings movements
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-07
 *
 */
public class KingTest {

    Board board = new Board();
    Figure whiteKing = new King(false);
    Figure blackKing = new King(true);

    /**
     * Tests some random valid and invalid moves for a white king
     *
     */
    @Test
    public void testValidMoveWhite() {
        assertTrue(whiteKing.validMove(new Position(4, 3), new Position(4, 4), board), "!white king can't move one field vertical correctly");

        assertTrue(whiteKing.validMove(new Position(4, 3), new Position(5, 4), board), "!white king can't move one field horizontal correctly");
    }

    /**
     * Tests some random valid and invalid moves for a black king
     *
     */
    @Test
    public void testValidMoveBlack() {
        assertTrue(blackKing.validMove(new Position(4, 3), new Position(4, 4), board), "!black king can't move one field vertical correctly");

        assertTrue(blackKing.validMove(new Position(4, 3), new Position(5, 4), board), "!black king can't move one field horizontal correctly");
    }

    /**
     * Tests some random valid and invalid attacks for a white king
     */
    @Test
    public void testAttackWhite() {
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                board.setFigure(x, y, new None());
            }
        }

        //beating an opposing figure beside
        board.setFigure(1, 1, whiteKing);
        board.setFigure(2, 1, blackKing);
        assertTrue(whiteKing.validMove(new Position(1, 1), new Position(2, 1), board), "!white king can't beat opposing figure horizontally");

        //beating an opposing figure vertically
        board.setFigure(1, 1, whiteKing);
        board.setFigure(1, 2, blackKing);
        assertTrue(whiteKing.validMove(new Position(1, 1), new Position(1, 2), board), "!white king can't beat opposing figure vertically");

        //beating an opposing figure diagonally
        board.setFigure(1, 1, whiteKing);
        board.setFigure(2, 2, blackKing);
        assertTrue(whiteKing.validMove(new Position(1, 1), new Position(2, 2), board), "!white king can't beat opposing figure diagonally");

    }

    /**
     * Tests some random valid and invalid attacks for a black king
     */
    @Test
    public void testAttackBlack() {
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                board.setFigure(x, y, new None());
            }
        }

        //beating an opposing figure beside
        board.setFigure(1, 1, whiteKing);
        board.setFigure(2, 1, blackKing);
        assertTrue(blackKing.validMove(new Position(2, 1), new Position(1, 1), board), "!black king can't beat opposing figure horizontally");

        //beating an opposing figure vertically
        board.setFigure(1, 1, whiteKing);
        board.setFigure(1, 2, blackKing);
        assertTrue(blackKing.validMove(new Position(1, 2), new Position(1, 1), board), "!black king can't beat opposing figure vertically");

        //beating an opposing figure diagonally
        board.setFigure(1, 1, whiteKing);
        board.setFigure(2, 2, blackKing);
        assertTrue(blackKing.validMove(new Position(2, 2), new Position(1, 1), board), "!black king can't beat opposing figure diagonally");

    }

    /**
     * Tests if the right symbol for the kings is used
     */
    @Test
    public void testGetSymbol() {
        assertEquals('K',whiteKing.getSymbol(), "!wrong Symbol for white King");
        assertEquals('k',blackKing.getSymbol(), "!wrong Symbol for black King");
    }
}
