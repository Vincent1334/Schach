package chess.model;

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
        Figure targetFigure = board.getFigure(targetPos);

        if (actualFigure.validMove(actualPos, targetPos, board)) {
            //create a tmpBoard with the new untested figure position
            Board tmpBoard = new Board(board);
            //perform the Figure move on a temporary board. IMPORTANT this move is untested and can be illegal
            tmpBoard.setFigure(actualPos, new None());
            tmpBoard.setFigure(targetPos, actualFigure);

            return !Board.kingInCheck(tmpBoard, actualFigure.getTeam()) && actualFigure.getTeam() != targetFigure.getTeam();
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

        //Set Figure to AlreadyMoved
        //board.getFigure(newX, newY).setAlreadyMoved(true);        kann hier nicht aufgerufen werden, da sonst Fehler bei schachMattPrüfung (siehe ChessMove())
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

        if ((board.getFigure(newX, posY) instanceof Pawn) && (board.getFigure(actualPos) instanceof Pawn)
                && (board.getFigure(newX, posY).getTeam() != board.getFigure(actualPos).getTeam())
                && (Math.abs(posX - newX) == 1)
                && ((board.getFigure(actualPos).getTeam() == 0 && newY - posY == 1)
                || (board.getFigure(actualPos).getTeam() == 1 && newY - posY == -1))) {
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

        int posX = actualPos.getPosX();
        int posY = actualPos.getPosY();
        int newX = targetPos.getPosX();

        Figure actualFigure = board.getFigure(actualPos);

        // castle left
        if (actualFigure instanceof King && !(actualFigure.isAlreadyMoved()) && newX == posX - 2 && !(board.getFigure(0, posY).isAlreadyMoved())) {
            // check, whether all field between are empty and are not threatened
            for (int j = 1; j < posX; j++) {
                if (!(board.getFigure(j, posY) instanceof None) || Board.isThreatened(board, new Position(j, posY), actualFigure.getTeam())) {
                    return 0;
                }
            }
            return 1;
        }
        // castle right
        if (actualFigure instanceof King && !(actualFigure.isAlreadyMoved()) && newX == posX + 2 && !(board.getFigure(7, posY).isAlreadyMoved())) {
            // check, whether all field between are empty and are not threatened
            for (int j = posX + 1; j < 7; j++) {
                if (!(board.getFigure(j, posY) instanceof None) || Board.isThreatened(board, new Position(j, posY), actualFigure.getTeam())) {
                    return 0;
                }
            }
            return 2;
        }
        return 0;
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

        if (actualFigure instanceof Pawn) {
            if (actualFigure.validMove(actualPos, targetPos, board)) {
                return (newY == 7 && actualFigure.getTeam() == 0) || (newY == 0 && actualFigure.getTeam() == 1);
            }
        }
        return false;
    }

    /**
     * executes a pawn conversion on the board
     *
     * @param actualPos current position of the figure you want to move
     * @param targetPos target position of the figure you want to move
     * @param figureID  the number of the figure you want the pawn to convert to (0 for queen, 1 for knight, 2 for bishop)
     * @param board     the current chessboard
     */
    public static void performPawnConversion(Position actualPos, Position targetPos, int figureID, Board board) {

        int newY = targetPos.getPosY();

        Figure actualFigure = board.getFigure(actualPos);

        //convert white pawn
        if (newY == 7 && actualFigure instanceof Pawn && actualFigure.getTeam() == 0) {

            switch (figureID) {
                //to knight
                case 3: {
                    board.setFigure(targetPos, new Knight(0));
                    break;
                }
                //to bishop
                case 4: {
                    board.setFigure(targetPos, new Bishop(0));
                    break;
                }
                //to rook
                case 2: {
                    board.setFigure(targetPos, new Rook(0));
                }
                //to queen
                default: {
                    board.setFigure(targetPos, new Queen(0));
                    break;
                }
            }
        }

        //convert black pawn
        if (newY == 0 && actualFigure instanceof Pawn && actualFigure.getTeam() == 1) {
            switch (figureID) {
                //to knight
                case 3: {
                    board.setFigure(targetPos, new Knight(1));
                    break;
                }
                //to bishop
                case 4: {
                    board.setFigure(targetPos, new Bishop(1));
                    break;
                }
                //to rook
                case 2: {
                    board.setFigure(targetPos, new Rook(1));
                }
                //to queen
                default: {
                    board.setFigure(targetPos, new Queen(1));
                    break;
                }
            }
        }

        board.getFigure(targetPos).setAlreadyMoved(true);
        board.setFigure(actualPos, new None());
    }

}
