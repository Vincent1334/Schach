package chess.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains tests to check the bishops movements
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-07
 *
 */
public class BishopTest {

    Board board = new Board();
    Figure whiteBishop = new Bishop(0);
    Figure blackBishop = new Bishop(1);

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
        assertTrue(whiteBishop.validMove(new Position(0,0), new Position(7,7), board), "!white bishop can't move to the upper right correctly");
        assertTrue(whiteBishop.validMove(new Position(7,7), new Position(0,0), board), "!white bishop can't move to the bottom left correctly");
        assertTrue(whiteBishop.validMove(new Position(7,0), new Position(0,7), board), "!white bishop can't move to the upper left correctly");
        assertTrue(whiteBishop.validMove(new Position(0,7), new Position(7,0), board), "!white bishop can't move to the bottom right correctly");

        assertFalse(whiteBishop.validMove(new Position(0,0), new Position(0,0), board), "!white bishop can stay in the same place for a move");

        board.setFigure(3,3,new Pawn(0));
        assertFalse(whiteBishop.validMove(new Position(0,0), new Position(7,7), board), "!white bishop can move to the upper right even if there is a figure from the same team in the way");
        assertFalse(whiteBishop.validMove(new Position(7,7), new Position(0,0), board), "!white bishop can move to the bottom left even if there is a figure from the same team in the way");

        board.setFigure(3,4,new Pawn(0));
        assertFalse(whiteBishop.validMove(new Position(0,7), new Position(7,0), board), "!white bishop can move to the bottom right even if there is a figure from the same team in the way");
        assertFalse(whiteBishop.validMove(new Position(7,0), new Position(0,7), board), "!white bishop can move to the upper left even if there is a figure from the same team in the way");
    }

    /**
     * Tests if the right symbol for the rooks is used
     */
    @Test
    public void testGetSymbol() {
        assertEquals('\u265D', whiteBishop.getSymbol(), "!wrong Symbol for white bishop");
        assertEquals('\u2657', blackBishop.getSymbol(), "!wrong Symbol for black bishop");
    }
}
