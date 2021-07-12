package chess.figures;

import chess.model.Board;
import chess.model.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains tests to check the rooks movements
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-07
 *
 */
public class RookTest {

    Board board = new Board();
    Figure whiteRook = new Rook(false);
    Figure blackRook = new Rook(true);

    /**
     * Tests some random valid and invalid moves for a rook
     *
     */
    @Test
    public void testValidMove() {
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                board.setFigure(x, y, new None());
            }
        }
        assertTrue(whiteRook.validMove(new Position(0,0), new Position(7,0), board), "!white rook can't move to the right correctly");
        assertTrue(whiteRook.validMove(new Position(7,0), new Position(0,0), board), "!white rook can't move to the left correctly");

        assertTrue(whiteRook.validMove(new Position(0,0), new Position(0,7), board), "!white rook can't move to the top correctly");
        assertTrue(whiteRook.validMove(new Position(0,7), new Position(0,0), board), "!white rook can't move to the bottom correctly");
    }

    /**
     * Tests whether the rook can skip a figure from the own team
     *
     */
    @Test
    public void testSkipFigure() {
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                board.setFigure(x, y, new None());
            }
        }
        board.setFigure(0,4,new Pawn(false));
        assertFalse(whiteRook.validMove(new Position(0,0), new Position(0,7), board), "!white rook can move to the top even if there is a figure from the same team in the way");
        assertFalse(whiteRook.validMove(new Position(0,7), new Position(0,0), board), "!white rook can move to the bottom even if there is a figure from the same team in the way");

        board.setFigure(4,0,new Pawn(false));
        assertFalse(whiteRook.validMove(new Position(0,0), new Position(7,0), board), "!white rook can move to the right even if there is a figure from the same team in the way");
        assertFalse(whiteRook.validMove(new Position(7,0), new Position(0,0), board), "!white rook can move to the left even if there is a figure from the same team in the way");
    }

    /**
     * Tests if the right symbol for the rooks is used
     */
    @Test
    public void testGetSymbol() {
        assertEquals('R',whiteRook.getSymbol(), "!wrong Symbol for white Rook");
        assertEquals('r',blackRook.getSymbol(), "!wrong Symbol for black Rook");
    }
}
