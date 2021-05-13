package chess.model;

import chess.figures.*;
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
            setup[i][1] = new Pawn(false);
        }
        setup[0][0] = new Rook(false);
        setup[7][0] = new Rook(false);
        setup[1][0] = new Knight(false);
        setup[6][0] = new Knight(false);
        setup[2][0] = new Bishop(false);
        setup[5][0] = new Bishop(false);
        setup[3][0] = new Queen(false);
        setup[4][0] = new King(false);

        //Build default setup black
        for(int i = 0; i < 8; i++) {
            setup[i][6] = new Pawn(true);
        }
        setup[0][7] = new Rook(true);
        setup[7][7] = new Rook(true);
        setup[1][7] = new Knight(true);
        setup[6][7] = new Knight(true);
        setup[2][7] = new Bishop(true);
        setup[5][7] = new Bishop(true);
        setup[3][7] = new Queen(true);
        setup[4][7] = new King(true);

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
        boardA.setFigure(3, 5, new Rook(true));

        Board boardB = new Board(boardA);

        assertNotSame(boardA, boardB);
        assertEquals(boardA, boardB, "Boards are not equal, but should be!");

        boardB.getFigure(3, 5).setAlreadyMoved(true);

        assertNotEquals(boardA, boardB, "Boards are equal, but shouldn't be!");
    }
    /**
     * Tests the white kings position
     */
    @Test
    public void testGetKingWhite(){
        String x = "x-position is incorrect!";
        String y = "y-position is incorrect!";
        Board boardA = new Board();

        //white default King
        Position pos1 = new Position(4, 0);
        assertEquals(Board.getKingPos(boardA, false).getPosX(), pos1.getPosX(), x);
        assertEquals(Board.getKingPos(boardA, false).getPosY(), pos1.getPosY(), y);

        //white random king position
        Position pos3 = new Position(2, 4);
        boardA.setFigure(4, 0, null);
        boardA.setFigure(2, 4, new King(false));
        assertEquals(Board.getKingPos(boardA, false).getPosX(), pos3.getPosX(), x);
        assertEquals(Board.getKingPos(boardA, false).getPosY(), pos3.getPosY(), y);
    }
    /**
     * Tests the black kings position
     */
    @Test
    public void testGetKingBlack(){
        Board boardA = new Board();
        String x = "x-position is incorrect!";
        String y = "y-position is incorrect!";

        //black default King
        Position pos2 = new Position(4, 7);
        assertEquals(Board.getKingPos(boardA, true).getPosX(), pos2.getPosX(), x);
        assertEquals(Board.getKingPos(boardA, true).getPosY(), pos2.getPosY(), y);

        //black random king position
        Position pos4 = new Position(7, 2);
        boardA.setFigure(4, 7, null);
        boardA.setFigure(7, 2, new King(true));
        assertEquals(Board.getKingPos(boardA, true).getPosX(), pos4.getPosX(), x);
        assertEquals(Board.getKingPos(boardA, true).getPosY(), pos4.getPosY(), y);
    }

    /**
     * Tests whether the king is threatened by an opposing figure
     */
    @Test
    public void testIsThreatened(){
        //Test start position
        Board boardA = new Board();

        assertFalse(Board.isThreatened(boardA, Board.getKingPos(boardA, false), true), "white king is threatened!");
        assertFalse(Board.isThreatened(boardA, Board.getKingPos(boardA, true), false), "black king is threatened!");

        //Test threatened position
        Board boardB = new Board();
        boardB.setFigure(4, 0, new None());
        boardB.setFigure(4, 7, new None());
        boardB.setFigure(2, 0, new None());
        boardB.setFigure(2, 7, new None());

        boardB.setFigure(2, 4, new King(true));
        boardB.setFigure(5, 4, new King(false));
        boardB.setFigure(3, 2, new Bishop(true));
        boardB.setFigure(4, 2, new Bishop(false));

        assertTrue(Board.isThreatened(boardB, Board.getKingPos(boardB, false), true), "is not Threatened!");
        assertTrue(Board.isThreatened(boardB, Board.getKingPos(boardB, true), false), "is not Threatened!");
    }

    /**
     * Tests some random setups for checkmate escape
     */
    @Test
    public void testChessMateEscapes() {
        Board board = new Board();
        Figure whiteKing = new King(false);
        whiteKing.setAlreadyMoved(true);

        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                board.setFigure(x, y, new None());
            }
        }

        // king can move out of chess
        board.setFigure(3,4,whiteKing);
        board.setFigure(3,1,new Rook(true));
        assertFalse(Board.checkChessMate(board,false), "return checkmate even if the king could move away");

        // any figure can beat the figure that threatens the king
        board.setFigure(3,4,new None());
        board.setFigure(3,1,new None());
        board.setFigure(0,1,whiteKing);
        board.setFigure(2,0,new Queen(true));
        board.setFigure(6,7,new Bishop(true));
        board.setFigure(7,6,new Pawn(false));
        assertFalse(Board.checkChessMate(board,false), "return checkmate even if the threatening figure can be beaten");

        // any figure except the king can protect the king
        board.setFigure(7,6,new None());
        board.setFigure(3,7,new Rook(false));
        assertFalse(Board.checkChessMate(board,false), "return checkmate even if a figure could block the attack");

        // a pawn can block the king by performing enPassant
        board.setFigure(3,7,new None());

        Figure enPassantWhite = new Pawn(false);
        enPassantWhite.setAlreadyMoved(true);
        board.setFigure(5,4,enPassantWhite);

        Pawn enPassantBlack = new Pawn(true);
        enPassantBlack.setEnPassant(true);
        board.setFigure(4,4,enPassantBlack);

        assertFalse(Board.checkChessMate(board,false), "return checkmate even if a pawn could block the king by performing EnPassant");

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
        board.setFigure(0,0,new King(false));
        board.setFigure(0,4,new Rook(true));
        board.setFigure(6,0,new Rook(true));
        board.setFigure(3,3,new Bishop(true));

        assertTrue(Board.checkChessMate(board,false), "checkmate is not recognized");
    }

}
