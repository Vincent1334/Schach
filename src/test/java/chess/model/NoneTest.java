package chess.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * This class contains tests to check the nones movements
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-07
 *
 */
public class NoneTest {

    Board board = new Board();
    Figure none = new None();

    /**
     * Tests if none can move
     *
     */
    @Test
    public void testValidMove(){
        assertFalse(none.validMove(new Position(0,0), new Position(0,0), board));
    }

    /**
     * Tests if the right symbol for none is used
     */
    @Test
    public void testGetSymbol() {
        assertEquals('\u0020',none.getSymbol(), "!wrong Symbol for empty field");
    }

}
