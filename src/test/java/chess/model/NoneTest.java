package chess.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class NoneTest {

    Board board = new Board();
    Figure none = new None();

    @Test
    public void testValidMove(){
        assertFalse(none.validMove(new Position(0,0), new Position(0,0), board));
    }

    @Test
    public void testGetSymbol() {
        assertEquals('\u0020',none.getSymbol(), "!wrong Symbol for empty field");
    }

}
