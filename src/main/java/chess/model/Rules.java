package chess.model;

import chess.figures.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the information about all allowed chess moves
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-05
 */
public class Rules {

    /*
     * <------DefaultMove----------------------------------------------------------------------------------------------->
     */

    /**
     * checks whether a standard move is valid or not
     *
     * @param actualPos current position of the figure you want to move
     * @param targetPos target position of the figure you want to move
     * @param board     the current chessboard
     * @return whether normal move is possible
     */
    public static boolean checkDefaultMove(Position actualPos, Position targetPos, Board board) {

        Figure actualFigure = board.getFigure(actualPos);

        //check target field is valid
        if (!(board.getFigure(targetPos) instanceof None) && board.getFigure(targetPos).isBlack() == actualFigure.isBlack()) {
            return false;
        }

        //check move is possible
        if (actualFigure.validMove(actualPos, targetPos, board)) {

            //create a tmpBoard with the new untested figure position
            Board tmpBoard = new Board(board);
            //perform the Figure move on a temporary board. IMPORTANT this move is untested and can be illegal
            tmpBoard.setFigure(actualPos, new None());
            tmpBoard.setFigure(targetPos, actualFigure);

            return !Board.kingInCheck(tmpBoard, actualFigure.isBlack());
        }
        return false;
    }

    /**
     * executes a standard move on the board
     *
     * @param actualPos current position of the figure you want to move
     * @param targetPos target position of the figure you want to move
     * @param board     the current chessboard
     */
    public static void performDefaultMove(Position actualPos, Position targetPos, Board board) {

        Figure actualFigure = board.getFigure(actualPos);
        Figure targetFigure = board.getFigure(targetPos);

        if (!(targetFigure instanceof None)) {
            board.getBeatenFigures().add(targetFigure);
        }

        //Update Board
        board.setFigure(targetPos, actualFigure);
        board.setFigure(actualPos, new None());
        board.getFigure(targetPos).setAlreadyMoved(true);
    }

    /*
     * <------EnPassant------------------------------------------------------------------------------------------------->
     */

    /**
     * checks valid enPassant move
     *
     * @param actualPos current position of the figure you want to move
     * @param targetPos target position of the figure you want to move
     * @param board     the current chessboard
     * @return Whether enPassant is possible
     */
    public static boolean checkEnPassant(Position actualPos, Position targetPos, Board board) {

        Figure actualFigure = board.getFigure(actualPos);
        Figure targetFigure = board.getFigure(targetPos.getPOS_X(), actualPos.getPOS_Y());

        //check target field is valid
        if (!(board.getFigure(targetPos) instanceof None)) {
            return false;
        }

        //check EnPassant is possible
        if (actualFigure instanceof Pawn && targetFigure instanceof Pawn
                && actualFigure.isBlack() != targetFigure.isBlack()
                && Math.abs(actualPos.getPOS_X() - targetPos.getPOS_X()) == 1
                && Math.abs(actualPos.getPOS_Y() - targetPos.getPOS_Y()) == 1
                && ((Pawn) targetFigure).isEnPassant()
                && ((Pawn) actualFigure).checkRightDirection(actualPos, targetPos)) {

            //create a tmpBoard with the new untested figure position
            Board tmpBoard = new Board(board);
            //perform the Figure move on a temporary board. IMPORTANT this move is untested and can be illegal
            tmpBoard.setFigure(targetPos, tmpBoard.getFigure(actualPos));
            tmpBoard.setFigure(actualPos, new None());
            tmpBoard.setFigure(targetPos.getPOS_X(), actualPos.getPOS_Y(), new None());

            return !(Board.kingInCheck(tmpBoard, actualFigure.isBlack()));
        }

        return false;
    }

    /**
     * executes an enPassant move on the board
     *
     * @param actualPos current position of the figure you want to move
     * @param targetPos target position of the figure you want to move
     * @param board     the current chessboard
     */
    public static void performEnPassantMove(Position actualPos, Position targetPos, Board board) {

        Figure actualFigure = board.getFigure(actualPos);
        Figure targetFigure = board.getFigure(targetPos.getPOS_X(), actualPos.getPOS_Y());

        board.getBeatenFigures().add(targetFigure);

        board.setFigure(targetPos.getPOS_X(), actualPos.getPOS_Y(), new None());
        board.setFigure(targetPos, actualFigure);
        board.setFigure(actualPos, new None());
    }

    /*
     * <------Castling-------------------------------------------------------------------------------------------------->
     */

    /**
     * checks valid castling move
     *
     * @param actualPos current position of the figure you want to move
     * @param targetPos target position of the figure you want to move
     * @param board     the current chessboard
     * @return 0 if castling is not possible, 1 if a queenSide castling is possible, 2 if a kingSide castling is possible
     */
    public static boolean checkCastling(Position actualPos, Position targetPos, Board board) {
        //King
        Figure actualFigure = board.getFigure(actualPos);

        //check chess or wrong input
        if (actualPos.getPOS_Y() != targetPos.getPOS_Y() || Board.kingInCheck(board, actualFigure.isBlack())) {
            return false;
        }

        if (actualFigure instanceof King && !(actualFigure.isAlreadyMoved())) {
            if (checkShortCastling(board, actualPos, targetPos)) {
                return true;
            }
            return checkLongCastling(board, actualPos, targetPos);
        }
        return false;
    }

    /**
     * check long castling
     *
     * @param board     the board you are playing on
     * @param actualPos the actual position of the figure you want to test
     * @param targetPos the target position of the figure you want to test
     * @return true, if the figure can perform a long castling
     */
    private static boolean checkLongCastling(Board board, Position actualPos, Position targetPos) {
        //check long castling left (queenSide)
        if (targetPos.getPOS_X() == 2 && board.getFigure(0, actualPos.getPOS_Y()) instanceof Rook
                && !(board.getFigure(0, actualPos.getPOS_Y()).isAlreadyMoved())) {
            //check, whether all field between are empty and are not threatened
            for (int j = 3; j > 0; j--) {
                if (!(board.getFigure(j, actualPos.getPOS_Y()) instanceof None)
                        || Board.isThreatened(board, new Position(j, actualPos.getPOS_Y()), !board.getFigure(actualPos).isBlack())) {
                    return false;
                }
            }
            //check Rook is not threatened
            return !Board.isThreatened(board, new Position(0, actualPos.getPOS_Y()), !board.getFigure(actualPos).isBlack());
            //Castling is possible
        }
        return false;
    }

    /**
     * check short castling
     *
     * @param board     the board you are playing on
     * @param actualPos the actual position of the figure you want to test
     * @param targetPos the target position of the figure you want to test
     * @return true, if the figure can perform a short castling
     */
    private static boolean checkShortCastling(Board board, Position actualPos, Position targetPos) {
        //check short castling right (kingSide)
        if (targetPos.getPOS_X() == 6 && board.getFigure(7, actualPos.getPOS_Y()) instanceof Rook
                && !(board.getFigure(7, actualPos.getPOS_Y()).isAlreadyMoved())) {
            //check, whether all field between are empty and are not threatened
            for (int j = 5; j < 7; j++) {
                if (!(board.getFigure(j, actualPos.getPOS_Y()) instanceof None)
                        || Board.isThreatened(board, new Position(j, actualPos.getPOS_Y()), !board.getFigure(actualPos).isBlack())) {
                    return false;
                }
            }
            //check Rook is not threatened
            return !Board.isThreatened(board, new Position(7, actualPos.getPOS_Y()), !board.getFigure(actualPos).isBlack());
            //Castling is possible
        }
        return false;
    }

    /**
     * executes a queenSide (left) castling move on the board
     *
     * @param actualPos current position of the figure you want to move
     * @param targetPos target position of the figure you want to move
     * @param board     the current chessboard
     */
    public static void performCastlingMove(Position actualPos, Position targetPos, Board board) {

        Figure actualFigure = board.getFigure(actualPos);

        //perform short castling
        if (targetPos.getPOS_X() == 6) {
            //set King
            board.setFigure(targetPos, actualFigure);
            board.setFigure(actualPos, new None());
            board.setCastlingFlag(true, board.getFigure(targetPos).isBlack());
            board.getFigure(targetPos).setAlreadyMoved(true);

            //set Rook
            board.setFigure(5, actualPos.getPOS_Y(), board.getFigure(7, actualPos.getPOS_Y()));
            board.setFigure(7, actualPos.getPOS_Y(), new None());

            board.getFigure(5, actualPos.getPOS_Y()).setAlreadyMoved(true);
        }

        //perform long castling
        if (targetPos.getPOS_X() == 2) {
            //set King
            board.setFigure(targetPos, actualFigure);
            board.setFigure(actualPos, new None());
            board.setCastlingFlag(true, board.getFigure(targetPos).isBlack());
            board.getFigure(targetPos).setAlreadyMoved(true);

            //set Rook
            board.setFigure(3, actualPos.getPOS_Y(), board.getFigure(0, actualPos.getPOS_Y()));
            board.setFigure(0, actualPos.getPOS_Y(), new None());

            board.getFigure(3, actualPos.getPOS_Y()).setAlreadyMoved(true);
        }
    }

    /*
     * <------Pawn-conversion------------------------------------------------------------------------------------------->
     */

    /**
     * checks valid pawn conversion
     *
     * @param actualPos current position of the figure you want to move
     * @param targetPos target position of the figure you want to move
     * @param board     the current chessboard
     * @return Whether a pawn conversion is possible
     */
    public static boolean checkPawnConversion(Position actualPos, Position targetPos, Board board) {

        Figure actualFigure = board.getFigure(actualPos);

        //check valid move
        if (actualFigure instanceof Pawn && actualFigure.validMove(actualPos, targetPos, board) && (targetPos.getPOS_Y() == 7 || targetPos.getPOS_Y() == 0)) {
            //create a tmpBoard with the new untested figure position
            Board tmpBoard = new Board(board);
            //perform the Figure move on a temporary board. IMPORTANT this move is untested and can be illegal
            tmpBoard.setFigure(actualPos, new None());
            tmpBoard.setFigure(targetPos, actualFigure);
            //check chess
            return !Board.kingInCheck(tmpBoard, actualFigure.isBlack());
        }
        return false;
    }

    /**
     * executes a pawn conversion on the board
     *
     * @param actualPos current position of the figure you want to move
     * @param targetPos target position of the figure you want to move
     * @param figureID  the number of the figure you want the pawn to convert to (2 for rook, 3 for knight, 4 for bishop, 5 for queen)
     * @param board     the current chessboard
     */
    public static void performPawnConversion(Position actualPos, Position targetPos, int figureID, Board board) {
        switch (figureID) {
            //to knight
            case 3: {
                board.setFigure(targetPos, new Knight(board.getFigure(actualPos).isBlack()));
                break;
            }
            //to bishop
            case 4: {
                board.setFigure(targetPos, new Bishop(board.getFigure(actualPos).isBlack()));
                break;
            }
            //to rook
            case 2: {
                board.setFigure(targetPos, new Rook(board.getFigure(actualPos).isBlack()));
                break;
            }
            //to queen
            default: {
                board.setFigure(targetPos, new Queen(board.getFigure(actualPos).isBlack()));
                break;
            }
        }

        board.getFigure(targetPos).setAlreadyMoved(true);
        board.setFigure(actualPos, new None());
    }


    /**
     * determines a list of possible target fields on the board
     *
     * @param actualPos the actual pos
     * @param board     the used board
     * @return a list of possible target fields
     */
    public static List<Position> possibleTargetFields(Position actualPos, Board board) {
        List<Position> fields = new ArrayList<>();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Position targetPos = new Position(x, y);
                if (Rules.checkDefaultMove(actualPos, targetPos, board) ||
                        Rules.checkCastling(actualPos, targetPos, board) ||
                        Rules.checkEnPassant(actualPos, targetPos, board) ||
                        Rules.checkPawnConversion(actualPos, targetPos, board)) {
                    fields.add(new Position(x, y));
                }
            }
        }
        return fields;
    }
}
