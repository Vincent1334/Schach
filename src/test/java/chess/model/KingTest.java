package chess.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KingTest {

    Board board = new Board();
    Figure whiteKing = new King(0);
    Figure blackKing = new King(1);

    @Test
    public void testValidMove() {
        assertTrue(whiteKing.validMove(new Position(4, 3), new Position(4, 4), board), "!white king can't move one field vertical correctly");
        assertTrue(blackKing.validMove(new Position(4, 3), new Position(4, 4), board), "!black king can't move one field vertical correctly");

        assertTrue(whiteKing.validMove(new Position(4, 3), new Position(5, 4), board), "!white king can't move one field horizontal correctly");
        assertTrue(blackKing.validMove(new Position(4, 3), new Position(5, 4), board), "!black king can't move one field horizontal correctly");

        assertFalse(whiteKing.validMove(new Position(4, 0), new Position(4, 0), board), "!white king can stay in the same place for a move");
        assertFalse(blackKing.validMove(new Position(4, 7), new Position(4 ,7), board), "!black king can stay in the same place for a move");
    }

    @Test
    public void testAttack() {
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                board.setFigure(x, y, new None());
            }
        }

        //beating an opposing figure beside
        board.setFigure(1, 1, whiteKing);
        board.setFigure(2, 1, blackKing);
        assertTrue(whiteKing.validMove(new Position(1, 1), new Position(2, 1), board), "!white king can't beat opposing figure horizontally");
        assertTrue(blackKing.validMove(new Position(2, 1), new Position(1, 1), board), "!black king can't beat opposing figure horizontally");

        //beating an opposing figure vertically
        board.setFigure(1, 1, whiteKing);
        board.setFigure(1, 2, blackKing);
        assertTrue(whiteKing.validMove(new Position(1, 1), new Position(1, 2), board), "!white king can't beat opposing figure vertically");
        assertTrue(blackKing.validMove(new Position(1, 2), new Position(1, 1), board), "!black king can't beat opposing figure vertically");

        //beating an opposing figure diagonally
        board.setFigure(1, 1, whiteKing);
        board.setFigure(2, 2, blackKing);
        assertTrue(whiteKing.validMove(new Position(1, 1), new Position(2, 2), board), "!white king can't beat opposing figure diagonally");
        assertTrue(blackKing.validMove(new Position(2, 2), new Position(1, 1), board), "!black king can't beat opposing figure diagonally");

    }

    @Test
    public void testGetSymbol() {
        assertEquals('\u265A',whiteKing.getSymbol(), "!wrong Symbol for white King");
        assertEquals('\u2654',blackKing.getSymbol(), "!wrong Symbol for black King");
    }
}
