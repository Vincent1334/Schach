package chess.model;

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
    public static boolean checkValidDefaultMove(Position actualPos, Position targetPos, Board board) {

        Figure actualFigure = board.getFigure(actualPos);

        if (actualFigure.validMove(actualPos, targetPos, board)) {
            //create a tmpBoard with the new untested figure position
            Board tmpBoard = new Board(board);
            //perform the Figure move on a temporary board. IMPORTANT this move is untested and can be illegal
            tmpBoard.setFigure(actualPos, new None());
            tmpBoard.setFigure(targetPos, actualFigure);

            return !Board.kingInCheck(tmpBoard, actualFigure.getTeam());
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

        int posX = actualPos.getPosX();
        int posY = actualPos.getPosY();
        int newX = targetPos.getPosX();
        int newY = targetPos.getPosY();

        Figure actualFigure = board.getFigure(actualPos);
        Figure targetFigure = board.getFigure(targetPos);

        if (board.getFigure(newX, posY) instanceof Pawn && board.getFigure(actualPos) instanceof Pawn
                && board.getFigure(newX, posY).getTeam() != board.getFigure(actualPos).getTeam()
                && Math.abs(posX - newX) == 1
                && (board.getFigure(actualPos).getTeam() == false && newY - posY == 1
                || board.getFigure(actualPos).getTeam() == true && newY - posY == -1)) {
            return ((Pawn) board.getFigure(newX, posY)).isEnPassant();
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

        int posY = actualPos.getPosY();
        int newX = targetPos.getPosX();

        board.getBeatenFigures().add(board.getFigure(newX, posY));
        board.setFigure(newX, posY, new None());
        board.setFigure(targetPos, board.getFigure(actualPos));
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
     * @return 0 if castling is not possible, 1 if a queenside castling is possible, 2 if a kingside castling is possible
     */
    public static int checkCastling(Position actualPos, Position targetPos, Board board) {
        if (checkCastlingLeft(actualPos, targetPos, board)){
            return 1;
        }
        if (checkCastlingRight(actualPos, targetPos,board)){
            return 2;
        }
        return 0;
    }

    /**
     * checks valid castling left
     *
     * @param actualPos current position of the figure you want to move
     * @param targetPos target position of the figure you want to move
     * @param board     the current chessboard
     * @return false if castling is not possible
     */
    private static boolean checkCastlingLeft(Position actualPos, Position targetPos, Board board){
        // castle left
        if (board.getFigure(actualPos) instanceof King && !(board.getFigure(actualPos).isAlreadyMoved()) && targetPos.getPosX() == actualPos.getPosX() - 2 && !(board.getFigure(0, actualPos.getPosY()).isAlreadyMoved()) && board.getFigure(0, actualPos.getPosY()).getTeam() == board.getFigure(actualPos).getTeam()) {
            // check, whether all field between are empty and are not threatened

            for (int j = 1; j < actualPos.getPosX(); j++) {
                if (!(board.getFigure(j, actualPos.getPosY()) instanceof None) || Board.isThreatened(board, new Position(j, actualPos.getPosY()), !board.getFigure(actualPos).getTeam())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * checks valid castling right
     *
     * @param actualPos current position of the figure you want to move
     * @param targetPos target position of the figure you want to move
     * @param board     the current chessboard
     * @return false if castling is not possible
     */
    private static boolean checkCastlingRight(Position actualPos, Position targetPos, Board board){
        // castle right
        if (board.getFigure(actualPos) instanceof King && !(board.getFigure(actualPos).isAlreadyMoved()) && targetPos.getPosX() == actualPos.getPosX() + 2 && !(board.getFigure(7, actualPos.getPosY()).isAlreadyMoved()) && board.getFigure(7, actualPos.getPosY()).getTeam() == board.getFigure(actualPos).getTeam()) {

            // check, whether all field between are empty and are not threatened
            for (int j = actualPos.getPosX()+1; j < 7; j++) {
                if (!(board.getFigure(j, actualPos.getPosY()) instanceof None) || Board.isThreatened(board, new Position(j, actualPos.getPosY()), !board.getFigure(actualPos).getTeam())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }



    /**
     * executes a queenside (left) castling move on the board
     *
     * @param actualPos current position of the figure you want to move
     * @param targetPos target position of the figure you want to move
     * @param board     the current chessboard
     */
    public static void performCastlingMoveLeft(Position actualPos, Position targetPos, Board board) {

        int posY = actualPos.getPosY();
        int newX = targetPos.getPosX();
        int newY = targetPos.getPosY();

        Figure actualFigure = board.getFigure(actualPos);
        Figure targetFigure = board.getFigure(targetPos);

        board.setFigure(newX + 1, newY, board.getFigure(0, posY));    // move rook
        board.setFigure(0, newY, new None());                            // replace field where rook was standing

        board.setFigure(targetPos, actualFigure);                          // move king
        board.setFigure(actualPos, targetFigure);                          // replace field where king was standing

    }

    /**
     * executes a kingside (right) castling move on the board
     *
     * @param actualPos current position of the figure you want to move
     * @param targetPos target position of the figure you want to move
     * @param board     the current chessboard
     */
    public static void performCastlingMoveRight(Position actualPos, Position targetPos, Board board) {

        int posY = actualPos.getPosY();
        int newX = targetPos.getPosX();
        int newY = targetPos.getPosY();

        Figure actualFigure = board.getFigure(actualPos);
        Figure targetFigure = board.getFigure(targetPos);

        board.setFigure(newX - 1, newY, board.getFigure(7, posY));      // move rook
        board.setFigure(7, newY, new None());                              // replace field where rook was standing

        board.setFigure(targetPos, actualFigure);                            // move king
        board.setFigure(actualPos, targetFigure);                            // replace field where king was standing

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

        int newY = targetPos.getPosY();

        Figure actualFigure = board.getFigure(actualPos);

        if (actualFigure instanceof Pawn && actualFigure.validMove(actualPos, targetPos, board)) {
            return newY == 7 && actualFigure.getTeam() == false || newY == 0 && actualFigure.getTeam() == true;
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

        convertWhitePawn(actualPos, targetPos, figureID, board);
        convertBlackPawn(actualPos, targetPos, figureID, board);

        board.getFigure(targetPos).setAlreadyMoved(true);
        board.setFigure(actualPos, new None());
    }

    /**
     * executes a white pawn conversion on the board
     *
     * @param actualPos current position of the figure you want to move
     * @param targetPos target position of the figure you want to move
     * @param figureID  the number of the figure you want the pawn to convert to (2 for rook, 3 for knight, 4 for bishop, 5 for queen)
     * @param board     the current chessboard
     */
    private static void convertWhitePawn(Position actualPos, Position targetPos, int figureID, Board board){
        //convert white pawn
        if (targetPos.getPosY() == 7 && board.getFigure(actualPos) instanceof Pawn && board.getFigure(actualPos).getTeam() == false) {

            switch (figureID) {
                //to knight
                case 3: {
                    board.setFigure(targetPos, new Knight(false));
                    break;
                }
                //to bishop
                case 4: {
                    board.setFigure(targetPos, new Bishop(false));
                    break;
                }
                //to rook
                case 2: {
                    board.setFigure(targetPos, new Rook(false));
                    break;
                }
                //to queen
                default: {
                    board.setFigure(targetPos, new Queen(false));
                    break;
                }
            }
        }
    }

    /**
     * executes a black pawn conversion on the board
     *
     * @param actualPos current position of the figure you want to move
     * @param targetPos target position of the figure you want to move
     * @param figureID  the number of the figure you want the pawn to convert to (2 for rook, 3 for knight, 4 for bishop, 5 for queen)
     * @param board     the current chessboard
     */
    private static void convertBlackPawn(Position actualPos, Position targetPos, int figureID, Board board){
        //convert black pawn
        if (targetPos.getPosY() == 0 && board.getFigure(actualPos) instanceof Pawn && board.getFigure(actualPos).getTeam() == true) {
            switch (figureID) {
                //to knight
                case 3: {
                    board.setFigure(targetPos, new Knight(true));
                    break;
                }
                //to bishop
                case 4: {
                    board.setFigure(targetPos, new Bishop(true));
                    break;
                }
                //to rook
                case 2: {
                    board.setFigure(targetPos, new Rook(true));
                    break;
                }
                //to queen
                default: {
                    board.setFigure(targetPos, new Queen(true));
                    break;
                }
            }
        }
    }
}
