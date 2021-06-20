package chess.ai;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains tests to check the piece square table information
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-10
 *
 */
public class PieceSquareTableTest {

    /**
     * Test PawnTable output
     */
    @Test
    public void testPawnTable(){
        assertEquals(27, PieceSquareTable.getTable(1, false)[3][3], "PawnTable fail");
    }

    /**
     * Test KnightTable output
     */
    @Test
    public void testKnightTable(){
        assertEquals(20, PieceSquareTable.getTable(3, false) [3][3], "KnightTable fail");
    }

    /**
     * Test BishopTable output
     */
    @Test
    public void testBishopTable(){
        assertEquals(10, PieceSquareTable.getTable(4, false)[3][3], "BishopTable fail");
    }

    /**
     * Test KingTable output
     */
    @Test
    public void testKingTable(){
        assertEquals(-50, PieceSquareTable.getTable(6, false) [3][3], "KnightTable fail");
    }

    /**
     * Test NullTable output
     */
    @Test
    public void testNullTable(){
        assertEquals(0, PieceSquareTable.getTable(0, false) [3][3], "NullTable fail");
        assertEquals(0, PieceSquareTable.getTable(2, false) [3][3], "NullTable fail");
        assertEquals(0, PieceSquareTable.getTable(5, false) [3][3], "NullTable fail");
    }

    /**
     * Test getTable output
     */
    @Test
    public void testGetTable(){
        assertArrayEquals(PieceSquareTable.getTable(1, false), PieceSquareTable.PAWN_TABLE);
        assertArrayEquals(PieceSquareTable.getTable(3, false), PieceSquareTable.KNIGHT_TABLE);
        assertArrayEquals(PieceSquareTable.getTable(4, false), PieceSquareTable.BISHOP_TABLE);
        assertArrayEquals(PieceSquareTable.getTable(6, false), PieceSquareTable.KING_TABLE);
        assertArrayEquals(PieceSquareTable.getTable(5, false), PieceSquareTable.NULL_TABLE);
    }
}