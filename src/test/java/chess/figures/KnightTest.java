package chess.figures;

import chess.model.Board;
import chess.model.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains tests to check the knights movements
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-07
 *
 */
public class KnightTest {

    Board board = new Board();
    Figure whiteKnight = new Knight(false);
    Figure blackKnight = new Knight(true);


    /**
     * Tests some random valid and invalid moves for a knight
     *
     */
    @Test
    public void testValidMove(){
        assertTrue(whiteKnight.validMove(new Position(1, 0), new Position(2, 2), board), "!white knight can't move forward correctly");
        assertTrue(blackKnight.validMove(new Position(1, 7), new Position(2, 5), board), "!black knight can't move forward correctly");

        assertFalse(whiteKnight.validMove(new Position(1, 0), new Position(1, 0), board), "!white knight can stay in the same place for a move");
        assertFalse(blackKnight.validMove(new Position(1, 7), new Position(1 ,7), board), "!black knight can stay in the same place for a move");
    }

    /**
     * Tests some random valid and invalid attacks for a knight
     */
    @Test
    public void testAttack(){
        for (int x=0; x<8; x++){
            for (int y=0; y<8; y++) {
                board.setFigure(x, y, new None());
            }
        }
        board.setFigure(1, 0, whiteKnight);
        board.setFigure(2, 2, blackKnight);
        assertTrue(whiteKnight.validMove(new Position(1, 0), new Position(2, 2), board), "!white knight can't attack an opposing figure");
        assertTrue(blackKnight.validMove(new Position(2, 2), new Position(1, 0), board), "!black knight can't attack an opposing figure");

        /*board.setFigure(1, 0, whiteKnight);
        board.setFigure(2, 2, whiteKnight);
        assertFalse(whiteKnight.validMove(new Position(1, 0), new Position(2, 2), board), "!white knight can attack a figure from the same team");

        board.setFigure(1, 0, blackKnight);
        board.setFigure(2, 2, blackKnight);
        assertFalse(blackKnight.validMove(new Position(1, 0), new Position(2, 2), board), "!black knight can attack a figure from the same team");*/
    }

    /**
     * Tests if the right symbol for the knights is used
     */
    /*@Test
    public void testGetSymbol() {
        assertEquals('\u265E',whiteKnight.getSymbol(), "!wrong Symbol for white knight");
        assertEquals('\u2658',blackKnight.getSymbol(), "!wrong Symbol for black Knight");
    }*/
    @Test
    public void testGetSymbol() {
        assertEquals('N',whiteKnight.getSymbol(), "!wrong Symbol for white knight");
        assertEquals('n',blackKnight.getSymbol(), "!wrong Symbol for black Knight");
    }
}