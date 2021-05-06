package chess.model;

import org.junit.jupiter.api.Test;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

public class RulesTest {

    Board board = new Board();

    @Test
    public void testPerformPawnConversion(){
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                board.setFigure(x, y, new None());
            }
        }

        // white
        board.setFigure(new Position(3,6),new Pawn(0));
        Rules.performPawnConversion(new Position(3,6),new Position(3,7),3,board);
        assertTrue(board.getFigure(new Position(3,7)) instanceof Knight, "white pawnConversion to Knight failed");

        board.setFigure(new Position(3,6),new Pawn(0));
        Rules.performPawnConversion(new Position(3,6),new Position(3,7),4,board);
        assertTrue(board.getFigure(new Position(3,7)) instanceof Bishop, "white pawnConversion to Bishop failed");

        board.setFigure(new Position(3,6),new Pawn(0));
        Rules.performPawnConversion(new Position(3,6),new Position(3,7),2,board);
        assertTrue(board.getFigure(new Position(3,7)) instanceof Rook, "white pawnConversion to Rook failed");

        board.setFigure(new Position(3,6),new Pawn(0));
        Rules.performPawnConversion(new Position(3,6),new Position(3,7),5,board);
        assertTrue(board.getFigure(new Position(3,7)) instanceof Queen, "white pawnConversion to Queen failed");

        // black
        board.setFigure(new Position(3,1),new Pawn(1));
        Rules.performPawnConversion(new Position(3,1),new Position(3,0),3,board);
        assertTrue(board.getFigure(new Position(3,0)) instanceof Knight, "black pawnConversion to Knight failed");

        board.setFigure(new Position(3,1),new Pawn(1));
        Rules.performPawnConversion(new Position(3,1),new Position(3,0),4,board);
        assertTrue(board.getFigure(new Position(3,0)) instanceof Bishop, "black pawnConversion to Bishop failed");

        board.setFigure(new Position(3,1),new Pawn(1));
        Rules.performPawnConversion(new Position(3,1),new Position(3,0),2,board);
        assertTrue(board.getFigure(new Position(3,0)) instanceof Rook, "black pawnConversion to Rook failed");

        board.setFigure(new Position(3,1),new Pawn(1));
        Rules.performPawnConversion(new Position(3,1),new Position(3,0),5,board);
        assertTrue(board.getFigure(new Position(3,0)) instanceof Queen, "black pawnConversion to Queen failed");
    }

}
