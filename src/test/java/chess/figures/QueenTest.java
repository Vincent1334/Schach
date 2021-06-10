package chess.figures;

import chess.model.Board;
import chess.model.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains tests to check the queens movements
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-09
 *
 */
public class QueenTest {

    Board board = new Board();
    Figure whiteQueen = new Queen(false);
    Figure blackQueen = new Queen(true);


    /**
     * Tests some random valid and invalid moves for a white queen
     *
     */
    @Test
    public void testValidMoveWhite(){
        assertTrue(whiteQueen.validMove(new Position(0, 2), new Position(0, 5), board), "!white queen can't move vertical");

        assertTrue(whiteQueen.validMove(new Position(0, 2), new Position(5, 2), board), "!white queen can't move horizontal");

        assertTrue(whiteQueen.validMove(new Position(2, 2), new Position(5, 5), board), "!white queen can't move diagonal");

       // assertFalse(whiteQueen.validMove(new Position(0, 2), new Position(0, 2), board), "!white queen can stay in the same place for a move");

    }

    /**
     * Tests some random valid and invalid moves for a black queen
     *
     */
    @Test
    public void testValidMoveBlack(){
        assertTrue(blackQueen.validMove(new Position(0, 2), new Position(0, 5), board), "!black queen can't move vertical");

        assertTrue(blackQueen.validMove(new Position(0, 2), new Position(5, 2), board), "!black queen can't move horizontal");

        assertTrue(blackQueen.validMove(new Position(2, 2), new Position(5, 5), board), "!black queen can't move diagonal");

       // assertFalse(blackQueen.validMove(new Position(0, 2), new Position(0, 2), board), "!black queen can stay in the same place for a move");

    }

    /**
     * Tests some random valid and invalid attacks for a white queen
     */
    @Test
    public void testAttackWhite() {
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                board.setFigure(x, y, new None());
            }
        }

        //beating an opposing figure horizontally
        board.setFigure(0, 2, whiteQueen);
        board.setFigure(5, 2, blackQueen);
        assertTrue(whiteQueen.validMove(new Position(0, 2), new Position(5, 2), board), "!white queen can't beat opposing figure horizontally");

        //beating an opposing figure vertically
        board.setFigure(0, 2, whiteQueen);
        board.setFigure(0, 5, blackQueen);
        assertTrue(whiteQueen.validMove(new Position(0, 2), new Position(0, 5), board), "!white queen can't beat opposing figure vertically");

        //beating an opposing figure diagonally
        board.setFigure(2, 2, whiteQueen);
        board.setFigure(5, 5, blackQueen);
        assertTrue(whiteQueen.validMove(new Position(2, 2), new Position(5, 5), board), "!white queen can't beat opposing figure diagonally");

    }

    /**
     * Tests some random valid and invalid attacks for a black queen
     */
    @Test
    public void testAttackBlack() {
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                board.setFigure(x, y, new None());
            }
        }

        //beating an opposing figure horizontally
        board.setFigure(0, 2, whiteQueen);
        board.setFigure(5, 2, blackQueen);
        assertTrue(blackQueen.validMove(new Position(5, 2), new Position(0, 2), board), "!black queen can't beat opposing figure horizontally");

        //beating an opposing figure vertically
        board.setFigure(0, 2, whiteQueen);
        board.setFigure(0, 5, blackQueen);
        assertTrue(blackQueen.validMove(new Position(0, 5), new Position(0, 2), board), "!black queen can't beat opposing figure vertically");

        //beating an opposing figure diagonally
        board.setFigure(2, 2, whiteQueen);
        board.setFigure(5, 5, blackQueen);
        assertTrue(blackQueen.validMove(new Position(5, 5), new Position(2, 2), board), "!black queen can't beat opposing figure diagonally");

    }

    /**
     * Tests if the right symbol for the queens is used
     */
    /*@Test
    public void testGetSymbol() {
        assertEquals('\u265B',whiteQueen.getSymbol(), "!wrong Symbol for white queen");
        assertEquals('\u2655',blackQueen.getSymbol(), "!wrong Symbol for black queen");
    }*/
    /**
     * Tests if the right symbol for the queens is used
     */
    @Test
    public void testGetSymbol() {
        assertEquals('Q',whiteQueen.getSymbol(), "!wrong Symbol for white queen");
        assertEquals('q',blackQueen.getSymbol(), "!wrong Symbol for black queen");
    }

}