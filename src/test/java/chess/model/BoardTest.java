package chess.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains tests to check the methods in board class
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-07
 *
 */
public class BoardTest {

    /**
     * Tests whether figure setup for beginning is correct
     */
    @Test
    public void testFigureSetUp(){
        Board boardA = new Board();
        Figure[][] setup = new Figure[8][8];

        //Build default setup white
        for(int i = 0; i < 8; i++) {
            setup[i][1] = new Pawn(0);
        }
        setup[0][0] = new Rook(0);
        setup[7][0] = new Rook(0);
        setup[1][0] = new Knight(0);
        setup[6][0] = new Knight(0);
        setup[2][0] = new Bishop(0);
        setup[5][0] = new Bishop(0);
        setup[3][0] = new Queen(0);
        setup[4][0] = new King(0);

        //Build default setup black
        for(int i = 0; i < 8; i++) {
            setup[i][6] = new Pawn(1);
        }
        setup[0][7] = new Rook(1);
        setup[7][7] = new Rook(1);
        setup[1][7] = new Knight(1);
        setup[6][7] = new Knight(1);
        setup[2][7] = new Bishop(1);
        setup[5][7] = new Bishop(1);
        setup[3][7] = new Queen(1);
        setup[4][7] = new King(1);

        //Build default setup none
        for(int y = 2; y < 6; y++){
            for(int x = 0; x < 8; x++){
                setup[x][y] = new None();
            }
        }

        //check Board is full build
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                assertNotNull(boardA.getFigure(x, y));
            }
        }

        //check that all the figures are in the right place
        //assertEquals(setup, boardA.getBoard());
    }

    /**
     * Tests whether the copied board and the board are the same
     */
    @Test
    public void testCopyConstructor() {
        Board boardA = new Board();
        boardA.setFigure(3, 5, new Rook(1));

        Board boardB = new Board(boardA);

        assertNotSame(boardA, boardB);
        assertEquals(boardA, boardB, "Boards are not equal, but should be!");

        boardB.getFigure(3, 5).setAlreadyMoved(true);

        assertNotEquals(boardA, boardB, "Boards are equal, but shouldn't be!");
    }

    /**
     * Tests the kings position
     */
    @Test
    public void testGetKing(){
        Board boardA = new Board();

        //white default King
        Position pos1 = new Position(4, 0);
        assertEquals(Board.getKing(boardA, 0).getPosX(), pos1.getPosX(), "x-position is incorrect!");
        assertEquals(Board.getKing(boardA, 0).getPosY(), pos1.getPosY(), "y-position is incorrect!");

        //black default King
        Position pos2 = new Position(4, 7);
        assertEquals(Board.getKing(boardA, 1).getPosX(), pos2.getPosX(), "x-position is incorrect!");
        assertEquals(Board.getKing(boardA, 1).getPosY(), pos2.getPosY(), "y-position is incorrect!");

        //white random king position
        Position pos3 = new Position(2, 4);
        boardA.setFigure(4, 0, null);
        boardA.setFigure(2, 4, new King(0));
        assertEquals(Board.getKing(boardA, 0).getPosX(), pos3.getPosX(), "x-position is incorrect!");
        assertEquals(Board.getKing(boardA, 0).getPosY(), pos3.getPosY(), "y-position is incorrect!");

        //black random king position
        Position pos4 = new Position(7, 2);
        boardA.setFigure(4, 7, null);
        boardA.setFigure(7, 2, new King(1));
        assertEquals(Board.getKing(boardA, 1).getPosX(), pos4.getPosX(), "x-position is incorrect!");
        assertEquals(Board.getKing(boardA, 1).getPosY(), pos4.getPosY(), "y-position is incorrect!");
    }

    /**
     * Tests whether the king is threatened by an opposing figure
     */
    @Test
    public void testIsThreatened(){
        //Test start position
        Board boardA = new Board();
        assertFalse(Board.isThreatened(boardA, Board.getKing(boardA, 0), 0), "white king is threatened!");
        assertFalse(Board.isThreatened(boardA, Board.getKing(boardA, 1), 1), "back king is threatened!");

        //Test threatened position
        Board boardB = new Board();
        boardB.setFigure(4, 0, null);
        boardB.setFigure(4, 7, null);
        boardB.setFigure(2, 0, null);
        boardB.setFigure(2, 7, null);

        boardB.setFigure(2, 4, new King(1));
        boardB.setFigure(5, 4, new King(0));
        boardB.setFigure(3, 2, new Bishop(1));
        boardB.setFigure(4, 2, new Bishop(0));

        assertTrue(Board.isThreatened(boardB, Board.getKing(boardB, 0), 0), "is not Threatened!");
        assertTrue(Board.isThreatened(boardB, Board.getKing(boardB, 1), 1), "is not Threatened!");
    }

    /**
     * Tests some random setups for checkmate escape
     */
    @Test
    public void testChessMateEscapes() {
        Board board = new Board();
        Figure whiteKing = new King(0);
        whiteKing.setAlreadyMoved(true);

        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                board.setFigure(x, y, new None());
            }
        }

        // king can move out of chess
        board.setFigure(3,4,whiteKing);
        board.setFigure(3,1,new Rook(1));
        assertFalse(Board.checkChessMate(board,0), "return checkmate even if the king could move away");

        // any figure can beat the figure that threatens the king
        board.setFigure(3,4,new None());
        board.setFigure(3,1,new None());
        board.setFigure(0,1,whiteKing);
        board.setFigure(2,0,new Queen(1));
        board.setFigure(6,7,new Bishop(1));
        board.setFigure(7,6,new Pawn(0));
        assertFalse(Board.checkChessMate(board,0), "return checkmate even if the threatening figure can be beaten");

        // any figure except the king can protect the king
        board.setFigure(7,6,new None());
        board.setFigure(3,7,new Rook(0));
        assertFalse(Board.checkChessMate(board,0), "return checkmate even if a figure could block the attack");

        // a pawn can block the king by performing enPassant
        board.setFigure(3,7,new None());

        Figure enPassantWhite = new Pawn(0);
        enPassantWhite.setAlreadyMoved(true);
        board.setFigure(5,4,enPassantWhite);

        Pawn enPassantBlack = new Pawn(1);
        enPassantBlack.setEnPassant(true);
        board.setFigure(4,4,enPassantBlack);

        assertFalse(Board.checkChessMate(board,0), "return checkmate even if a pawn could block the king by performing EnPassant");

    }

    /**
     * checks a random testcase for checkmate
     */
    @Test
    public void testCheckMates() {
        Board board = new Board();
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                board.setFigure(x, y, new None());
            }
        }
        board.setFigure(0,0,new King(0));
        board.setFigure(0,4,new Rook(1));
        board.setFigure(6,0,new Rook(1));
        board.setFigure(3,3,new Bishop(1));

        assertTrue(Board.checkChessMate(board,0), "checkmate is not recognized");
    }

}
