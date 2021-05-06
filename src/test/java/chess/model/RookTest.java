package chess.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RookTest {

    Board board = new Board();
    Figure whiteRook = new Rook(0);
    Figure blackRook = new Rook(1);

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

        assertFalse(whiteRook.validMove(new Position(0,0), new Position(0,0), board), "!white rook can stay in the same place for a move");

        board.setFigure(0,4,new Pawn(0));
        assertFalse(whiteRook.validMove(new Position(0,0), new Position(0,7), board), "!white rook can move to the top even if there is a figure from the same team in the way");
        assertFalse(whiteRook.validMove(new Position(0,7), new Position(0,0), board), "!white rook can move to the bottom even if there is a figure from the same team in the way");

        board.setFigure(4,0,new Pawn(0));
        assertFalse(whiteRook.validMove(new Position(0,0), new Position(7,0), board), "!white rook can move to the right even if there is a figure from the same team in the way");
        assertFalse(whiteRook.validMove(new Position(7,0), new Position(0,0), board), "!white rook can move to the left even if there is a figure from the same team in the way");
    }

    @Test
    public void testGetSymbol() {
        assertEquals('\u265C',whiteRook.getSymbol(), "!wrong Symbol for white Rook");
        assertEquals('\u2656',blackRook.getSymbol(), "!wrong Symbol for black Rook");
    }
}
