package chess.ki;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PieceSquareTableTest {

    /**
     * Test PawnTable output
     */
    @Test
    public void testPawnTable(){
        assertEquals(PieceSquareTable.getTable(1)[3][3], 27, "PawnTable fail");
    }

    /**
     * Test KnightTable output
     */
    @Test
    public void testKnightTable(){
        assertEquals(PieceSquareTable.getTable(3) [3][3],20, "KnightTable fail");
    }

    /**
     * Test BishopTable output
     */
    @Test
    public void testBishopTable(){
        assertEquals(PieceSquareTable.getTable(4)[3][3], 10, "BishopTable fail");
    }

    /**
     * Test KingTable output
     */
    @Test
    public void testKingTable(){
        assertEquals(PieceSquareTable.getTable(6) [3][3],-50, "KnightTable fail");
    }

    /**
     * Test NullTable output
     */
    @Test
    public void testNullTable(){
        assertEquals(PieceSquareTable.getTable(0) [3][3],0, "NullTable fail");
        assertEquals(PieceSquareTable.getTable(2) [3][3],0, "NullTable fail");
        assertEquals(PieceSquareTable.getTable(5) [3][3],0, "NullTable fail");
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