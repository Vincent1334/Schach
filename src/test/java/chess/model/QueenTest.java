package chess.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QueenTest {

    public QueenTest(){
    }

    Board board = new Board();
    Figure whiteQueen = new Queen(0);
    Figure blackQueen = new Queen(1);

    @Test
    public void testValidMove(){
        assertTrue(whiteQueen.validMove(new Position(0, 0), new Position(0, 5), board), "!white queen can't move vertical");
        assertTrue(blackQueen.validMove(new Position(0, 0), new Position(0, 5), board), "!black queen can't move vertical");

        assertTrue(whiteQueen.validMove(new Position(0, 0), new Position(5, 0), board), "!white queen can't move horizontal");
        assertTrue(blackQueen.validMove(new Position(0, 0), new Position(5, 0), board), "!black queen can't move horizontal");

        assertTrue(whiteQueen.validMove(new Position(0, 0), new Position(5, 5), board), "!white queen can't move diagonal");
        assertTrue(blackQueen.validMove(new Position(0, 0), new Position(5, 5), board), "!black queen can't move diagonal");

        assertFalse(whiteQueen.validMove(new Position(0, 0), new Position(0, 0), board), "!white queen can stay in the same place for a move");
        assertFalse(blackQueen.validMove(new Position(0, 0), new Position(0, 0), board), "!black queen can stay in the same place for a move");

    }

    @Test
    public void testAttack() {
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                board.setFigure(x, y, new None());
            }
        }

        //beating an opposing figure horizontally
        board.setFigure(0, 0, whiteQueen);
        board.setFigure(5, 0, blackQueen);
        assertTrue(whiteQueen.validMove(new Position(0, 0), new Position(5, 0), board), "!white queen can't beat opposing figure horizontally");
        assertTrue(blackQueen.validMove(new Position(5, 0), new Position(0, 0), board), "!black queen can't beat opposing figure horizontally");

        //beating an opposing figure vertically
        board.setFigure(0, 0, whiteQueen);
        board.setFigure(0, 5, blackQueen);
        assertTrue(whiteQueen.validMove(new Position(0, 0), new Position(0, 5), board), "!white queen can't beat opposing figure vertically");
        assertTrue(blackQueen.validMove(new Position(0, 5), new Position(0, 0), board), "!black queen can't beat opposing figure vertically");

        //beating an opposing figure diagonally
        board.setFigure(0, 0, whiteQueen);
        board.setFigure(5, 5, blackQueen);
        assertTrue(whiteQueen.validMove(new Position(0, 0), new Position(5, 5), board), "!white queen can't beat opposing figure diagonally");
        assertTrue(blackQueen.validMove(new Position(5, 5), new Position(0, 0), board), "!black queen can't beat opposing figure diagonally");

        //beating a figure from the same team horizontally
        board.setFigure(0, 0, whiteQueen);
        board.setFigure(5, 0, whiteQueen);
        assertTrue(whiteQueen.validMove(new Position(0, 0), new Position(5, 0), board), "!white queen can beat figure from the same team horizontally");

        board.setFigure(0, 0, blackQueen);
        board.setFigure(5, 0, blackQueen);
        assertTrue(blackQueen.validMove(new Position(0, 0), new Position(5, 0), board), "!black queen can beat figure from the same team horizontally");

        //beating a figure from the same team vertical
        board.setFigure(0, 0, whiteQueen);
        board.setFigure(0, 5, whiteQueen);
        assertTrue(whiteQueen.validMove(new Position(0, 0), new Position(0, 5), board), "!white queen can beat figure from the same team vertically");

        board.setFigure(0, 0, blackQueen);
        board.setFigure(0, 5, blackQueen);
        assertTrue(blackQueen.validMove(new Position(0, 0), new Position(0, 5), board), "!black queen can beat figure from the same team vertically");

        //beating a figure from the same team diagonal
        board.setFigure(0, 0, whiteQueen);
        board.setFigure(5, 5, whiteQueen);
        assertTrue(whiteQueen.validMove(new Position(0, 0), new Position(5, 5), board), "!white queen can beat figure from the same team diagonally");

        board.setFigure(0, 0, blackQueen);
        board.setFigure(5, 5, blackQueen);
        assertTrue(blackQueen.validMove(new Position(0, 0), new Position(5, 5), board), "!black queen can beat figure from the same team diagonally");
    }

}