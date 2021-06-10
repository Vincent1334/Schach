package chess.ki;

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
        assertEquals(27, PieceSquareTable.getTable(1)[3][3], "PawnTable fail");
    }

    /**
     * Test KnightTable output
     */
    @Test
    public void testKnightTable(){
        assertEquals(20, PieceSquareTable.getTable(3) [3][3], "KnightTable fail");
    }

    /**
     * Test BishopTable output
     */
    @Test
    public void testBishopTable(){
        assertEquals(10, PieceSquareTable.getTable(4)[3][3], "BishopTable fail");
    }

    /**
     * Test KingTable output
     */
    @Test
    public void testKingTable(){
        assertEquals(-50, PieceSquareTable.getTable(6) [3][3], "KnightTable fail");
    }

    /**
     * Test NullTable output
     */
    @Test
    public void testNullTable(){
        assertEquals(0, PieceSquareTable.getTable(0) [3][3], "NullTable fail");
        assertEquals(0, PieceSquareTable.getTable(2) [3][3], "NullTable fail");
        assertEquals(0, PieceSquareTable.getTable(5) [3][3], "NullTable fail");
    }

    /**
     * Test getTable output
     */
    @Test
    public void testGetTable(){
        assertArrayEquals(PieceSquareTable.getTable(1), PieceSquareTable.pawnTable);
        assertArrayEquals(PieceSquareTable.getTable(3), PieceSquareTable.knightTable);
        assertArrayEquals(PieceSquareTable.getTable(4), PieceSquareTable.bishopTable);
        assertArrayEquals(PieceSquareTable.getTable(6), PieceSquareTable.kingTable);
        assertArrayEquals(PieceSquareTable.getTable(5), PieceSquareTable.nullTable);
    }
}