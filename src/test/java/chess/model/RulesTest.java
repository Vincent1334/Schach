package chess.model;

import chess.figures.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains tests to check the pawn conversion and castling moves
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-07
 *
 */
public class RulesTest {

    Board board = new Board();

    /**
     * Tests valid pawn conversion for white figure
     */
    @Test
    public void testPerformPawnConversionWhite(){
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                board.setFigure(x, y, new None());
            }
        }
        board.setFigure(new Position(3,6),new Pawn(false));
        Rules.performPawnConversion(new Position(3,6),new Position(3,7),3,board);
        assertTrue(board.getFigure(new Position(3,7)) instanceof Knight, "white pawnConversion to Knight failed");

        board.setFigure(new Position(3,6),new Pawn(false));
        Rules.performPawnConversion(new Position(3,6),new Position(3,7),4,board);
        assertTrue(board.getFigure(new Position(3,7)) instanceof Bishop, "white pawnConversion to Bishop failed");

        board.setFigure(new Position(3,6),new Pawn(false));
        Rules.performPawnConversion(new Position(3,6),new Position(3,7),2,board);
        assertTrue(board.getFigure(new Position(3,7)) instanceof Rook, "white pawnConversion to Rook failed");

        board.setFigure(new Position(3,6),new Pawn(false));
        Rules.performPawnConversion(new Position(3,6),new Position(3,7),5,board);
        assertTrue(board.getFigure(new Position(3,7)) instanceof Queen, "white pawnConversion to Queen failed");
    }

    /**
     * Tests valid pawn conversion for black figure
     */
    @Test
    public void testPerformPawnConversionBlack(){
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                board.setFigure(x, y, new None());
            }
        }
        board.setFigure(new Position(3,1),new Pawn(true));
        Rules.performPawnConversion(new Position(3,1),new Position(3,0),3,board);
        assertTrue(board.getFigure(new Position(3,0)) instanceof Knight, "black pawnConversion to Knight failed");

        board.setFigure(new Position(3,1),new Pawn(true));
        Rules.performPawnConversion(new Position(3,1),new Position(3,0),4,board);
        assertTrue(board.getFigure(new Position(3,0)) instanceof Bishop, "black pawnConversion to Bishop failed");

        board.setFigure(new Position(3,1),new Pawn(true));
        Rules.performPawnConversion(new Position(3,1),new Position(3,0),2,board);
        assertTrue(board.getFigure(new Position(3,0)) instanceof Rook, "black pawnConversion to Rook failed");

        board.setFigure(new Position(3,1),new Pawn(true));
        Rules.performPawnConversion(new Position(3,1),new Position(3,0),5,board);
        assertTrue(board.getFigure(new Position(3,0)) instanceof Queen, "black pawnConversion to Queen failed");
    }


    /**
     * Tests valid castling move on the right side of the board
     */
    @Test
    public void testPerformCastlingMoveRight() {
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                board.setFigure(x, y, new None());
            }
        }
        board.setFigure(new Position(4,0),new King(false));
        board.setFigure(new Position(7,0),new Rook(false));
        Rules.performCastlingMove(new Position(4,0),new Position(6,0),board);
        assertTrue(board.getFigure(new Position(4,0)) instanceof None, "Castling failed: king was not removed from original position");
        assertTrue(board.getFigure(new Position(5,0)) instanceof Rook, "Castling failed: rook was not moved correctly");
        assertTrue(board.getFigure(new Position(6,0)) instanceof King, "Castling failed: king was not moved correctly");
        assertTrue(board.getFigure(new Position(7,0)) instanceof None, "Castling failed: rook was not removed from original position");
    }

    /**
     * Tests valid castling move on the left side of the board
     */
    @Test
    public void testPerformCastlingMoveLeft() {
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                board.setFigure(x, y, new None());
            }
        }
        board.setFigure(new Position(4,0),new King(false));
        board.setFigure(new Position(0,0),new Rook(false));
        Rules.performCastlingMove(new Position(4,0),new Position(2,0),board);
        assertTrue(board.getFigure(new Position(0,0)) instanceof None, "Castling failed: rook was not removed from original position");
        assertTrue(board.getFigure(new Position(2,0)) instanceof King, "Castling failed: king was not moved correctly");
        assertTrue(board.getFigure(new Position(3,0)) instanceof Rook, "Castling failed: rook was not moved correctly");
        assertTrue(board.getFigure(new Position(4,0)) instanceof None, "Castling failed: king was not removed from original position");
    }

}
