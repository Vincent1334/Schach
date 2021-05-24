package chess.model;

import chess.figures.*;
import chess.util.Observable;

/**
 * This class contains the information about all allowed chess moves
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-05
 */
public class Rules extends Observable {

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
        if (!(board.getFigure(targetPos) instanceof None) && board.getFigure(targetPos).isBlackTeam() == actualFigure.isBlackTeam()) return false;

        //check move is possible
        if (actualFigure.validMove(actualPos, targetPos, board)) {

            //create a tmpBoard with the new untested figure position
            Board tmpBoard = new Board(board);
            //perform the Figure move on a temporary board. IMPORTANT this move is untested and can be illegal
            tmpBoard.setFigure(actualPos, new None());
            tmpBoard.setFigure(targetPos, actualFigure);

            return !Board.kingInCheck(tmpBoard, actualFigure.isBlackTeam());
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
            notifyObserversForDelete(targetPos.getPosX(), targetPos.getPosY());
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
        Figure targetFigure = board.getFigure(targetPos.getPosX(), actualPos.getPosY());

        //check target field is valid
        if (!(board.getFigure(targetPos) instanceof None)) return false;

        //check EnPassant is possible
        if (actualFigure instanceof Pawn && targetFigure instanceof Pawn
                && actualFigure.isBlackTeam() != targetFigure.isBlackTeam()
                && Math.abs(actualPos.getPosX() - targetPos.getPosX()) == 1
                && Math.abs(actualPos.getPosY() - targetPos.getPosY()) == 1
                && ((Pawn) targetFigure).isEnPassant()
                && ((Pawn) actualFigure).checkRightDirection(actualPos, targetPos)) {

            //create a tmpBoard with the new untested figure position
            Board tmpBoard = new Board(board);
            //perform the Figure move on a temporary board. IMPORTANT this move is untested and can be illegal
            tmpBoard.setFigure(targetPos, tmpBoard.getFigure(actualPos));
            tmpBoard.setFigure(actualPos, new None());
            tmpBoard.setFigure(targetPos.getPosX(), actualPos.getPosY(), new None());

            return !(Board.kingInCheck(tmpBoard, actualFigure.isBlackTeam()));
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
        Figure targetFigure = board.getFigure(targetPos.getPosX(), actualPos.getPosY());

        board.getBeatenFigures().add(targetFigure);
        notifyObserversForDelete(targetPos.getPosX(), actualPos.getPosY());

        board.setFigure(targetPos.getPosX(), actualPos.getPosY(), new None());
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
        if (actualPos.getPosY() != targetPos.getPosY() || Board.kingInCheck(board, actualFigure.isBlackTeam())) return false;

        if (actualFigure instanceof King && !(actualFigure.isAlreadyMoved())) {
            if(checkShortCastling(board, actualPos, targetPos)) return true;
            return checkLongCastling(board, actualPos, targetPos);
        }
        return false;
    }

    /**
     * check long castling
     * @param board the board you are playing on
     * @param actualPos the actual position of the figure you want to test
     * @param targetPos the target position of the figure you want to test
     * @return true, if the figure can perform a long castling
     */
    private static boolean checkLongCastling(Board board, Position actualPos, Position targetPos){
        //check long castling left (queenSide)
        if (targetPos.getPosX() == 2 && board.getFigure(0, actualPos.getPosY()) instanceof Rook
                && !(board.getFigure(0, actualPos.getPosY()).isAlreadyMoved())) {
            //check, whether all field between are empty and are not threatened
            for (int j = 3; j > 0; j--) {
                if (!(board.getFigure(j, actualPos.getPosY()) instanceof None)
                        || Board.isThreatened(board, new Position(j, actualPos.getPosY()), !board.getFigure(actualPos).isBlackTeam())) {
                    return false;
                }
            }
            //check Rook is not threatened
            return !Board.isThreatened(board, new Position(0, actualPos.getPosY()), !board.getFigure(actualPos).isBlackTeam());
            //Castling is possible
        }
        return false;
    }

    /**
     * check short castling
     * @param board the board you are playing on
     * @param actualPos the actual position of the figure you want to test
     * @param targetPos the target position of the figure you want to test
     * @return true, if the figure can perform a short castling
     */
    private static boolean checkShortCastling(Board board, Position actualPos, Position targetPos){
        //check short castling right (kingSide)
        if (targetPos.getPosX() == 6 && board.getFigure(7, actualPos.getPosY()) instanceof Rook
                && !(board.getFigure(7, actualPos.getPosY()).isAlreadyMoved())) {
            //check, whether all field between are empty and are not threatened
            for (int j = 5; j < 7; j++) {
                if (!(board.getFigure(j, actualPos.getPosY()) instanceof None)
                        || Board.isThreatened(board, new Position(j, actualPos.getPosY()), !board.getFigure(actualPos).isBlackTeam())) {
                    return false;
                }
            }
            //check Rook is not threatened
            return !Board.isThreatened(board, new Position(7, actualPos.getPosY()), !board.getFigure(actualPos).isBlackTeam());
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
        if (targetPos.getPosX() == 6) {
            //set King
            board.setFigure(targetPos, actualFigure);
            board.setFigure(actualPos, new None());
            board.getFigure(targetPos).setAlreadyMoved(true);

            //set Rook
            board.setFigure(5, actualPos.getPosY(), board.getFigure(7, actualPos.getPosY()));
            board.setFigure(7, actualPos.getPosY(), new None());
            notifyObserversForMovement(7, actualPos.getPosY(), 5,  actualPos.getPosY());

            board.getFigure(5, actualPos.getPosY()).setAlreadyMoved(true);
        }

        //perform long castling
        if (targetPos.getPosX() == 2) {
            //set King
            board.setFigure(targetPos, actualFigure);
            board.setFigure(actualPos, new None());
            board.getFigure(targetPos).setAlreadyMoved(true);

            //set Rook
            board.setFigure(3, actualPos.getPosY(), board.getFigure(0, actualPos.getPosY()));
            board.setFigure(0, actualPos.getPosY(), new None());
            notifyObserversForMovement(0, actualPos.getPosY(), 3,  actualPos.getPosY());

            board.getFigure(3, actualPos.getPosY()).setAlreadyMoved(true);
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
        if (actualFigure instanceof Pawn && actualFigure.validMove(actualPos, targetPos, board) && (targetPos.getPosY() == 7 || targetPos.getPosY() == 0)) {
            //create a tmpBoard with the new untested figure position
            Board tmpBoard = new Board(board);
            //perform the Figure move on a temporary board. IMPORTANT this move is untested and can be illegal
            tmpBoard.setFigure(actualPos, new None());
            tmpBoard.setFigure(targetPos, actualFigure);
            //check chess
            return !Board.kingInCheck(tmpBoard, actualFigure.isBlackTeam());
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
                board.setFigure(targetPos, new Knight(board.getFigure(actualPos).isBlackTeam()));
                break;
            }
            //to bishop
            case 4: {
                board.setFigure(targetPos, new Bishop(board.getFigure(actualPos).isBlackTeam()));
                break;
            }
            //to rook
            case 2: {
                board.setFigure(targetPos, new Rook(board.getFigure(actualPos).isBlackTeam()));
                break;
            }
            //to queen
            default: {
                board.setFigure(targetPos, new Queen(board.getFigure(actualPos).isBlackTeam()));
                break;
            }
        }

        board.getFigure(targetPos).setAlreadyMoved(true);
        board.setFigure(actualPos, new None());
        notifyObserversForChange(actualPos.getPosX(), actualPos.getPosY(), figureID, board.getFigure(actualPos).isBlackTeam());
    }
}
