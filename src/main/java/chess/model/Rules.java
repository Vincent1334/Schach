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

        //check target field is valid
        if(!(board.getFigure(targetPos) instanceof None) && board.getFigure(targetPos).getTeam() == actualFigure.getTeam()) return false;

        //check move is possible
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

        Figure actualFigure = board.getFigure(actualPos);
        Figure targetFigure = board.getFigure(targetPos.getPosX(), actualPos.getPosY());

        //check target field is valid
        if(!(board.getFigure(targetPos) instanceof None) && board.getFigure(targetPos).getTeam() == actualFigure.getTeam()) return false;

        //check EnPassant is possible
        if(actualFigure instanceof Pawn && targetFigure instanceof Pawn && actualFigure.getTeam() != targetFigure.getTeam() && ((Pawn)board.getFigure(targetPos)).isEnPassant()){
            //check right movement
            if(Math.abs(actualPos.getPosX()- targetPos.getPosX()) == 1 && Math.abs(actualPos.getPosY()- targetPos.getPosY()) == 1){
                //check right direction
                if(((Pawn) actualFigure).checkRightDirection(actualPos, targetPos)){

                    //create a tmpBoard with the new untested figure position
                    Board tmpBoard = new Board(board);
                    //perform the Figure move on a temporary board. IMPORTANT this move is untested and can be illegal
                    tmpBoard.setFigure(targetPos, tmpBoard.getFigure(actualPos));
                    tmpBoard.setFigure(actualPos, new None());
                    tmpBoard.setFigure(targetPos.getPosX(), actualPos.getPosY(), new None());

                    return !(Board.kingInCheck(tmpBoard, actualFigure.getTeam()));
                }
            }
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
     * @return 0 if castling is not possible, 1 if a queenside castling is possible, 2 if a kingside castling is possible
     */
    public static boolean checkCastling(Position actualPos, Position targetPos, Board board) {

        Figure actualFigure = board.getFigure(actualPos);

        //check chess
        if(Board.kingInCheck(board, actualFigure.getTeam())) return false;

        if(actualFigure instanceof King && !(actualFigure.isAlreadyMoved())){
            //check short castling
            if(targetPos.getPosX() == 6 && board.getFigure(7, actualPos.getPosY()) instanceof Rook && !(board.getFigure(7, actualPos.getPosY()).isAlreadyMoved())){
                //check, whether all field between are empty and are not threatened
                for (int j = 5; j < 7; j++) {
                    if (!(board.getFigure(j, actualPos.getPosY()) instanceof None && Board.isThreatened(board, new Position(j, actualPos.getPosY()), !actualFigure.getTeam()))) return false;
                }
                //check Rook is not threatened
                if(Board.isThreatened(board, new Position(7, actualPos.getPosY()), !actualFigure.getTeam())) return false;
                //Castling is possible
                return true;
            }

            //check long castling
            if(targetPos.getPosX() == 2 && board.getFigure(0, actualPos.getPosY()) instanceof Rook && !(board.getFigure(0, actualPos.getPosY()).isAlreadyMoved())){
                //check, whether all field between are empty and are not threatened
                for (int j = 5; j > 0; j--) {
                    if (!(board.getFigure(j, actualPos.getPosY()) instanceof None && Board.isThreatened(board, new Position(j, actualPos.getPosY()), !actualFigure.getTeam()))) return false;
                }
                //check Rook is not threatened
                if(Board.isThreatened(board, new Position(0, actualPos.getPosY()), !actualFigure.getTeam())) return false;
                //Castling is possible
                return true;
            }
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
    public static void performCastlingMove(Position actualPos, Position targetPos, Board board) {

        Figure actualFigure = board.getFigure(actualPos);

        //perform short castling
        if(targetPos.getPosX() == 6){
            //set King
            board.setFigure(targetPos, actualFigure);
            board.setFigure(actualPos, new None());
            board.getFigure(targetPos).setAlreadyMoved(true);

            //set Rook
            board.setFigure(5, actualPos.getPosY(), board.getFigure(7, actualPos.getPosY()));
            board.setFigure(7, actualPos.getPosY(), new None());
            board.getFigure(5, actualPos.getPosY()).setAlreadyMoved(true);
        }

        //perform short castling
        if(targetPos.getPosX() == 2){
            //set King
            board.setFigure(targetPos, actualFigure);
            board.setFigure(actualPos, new None());
            board.getFigure(targetPos).setAlreadyMoved(true);

            //set Rook
            board.setFigure(3, actualPos.getPosY(), board.getFigure(0, actualPos.getPosY()));
            board.setFigure(0, actualPos.getPosY(), new None());
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

        int newY = targetPos.getPosY();

        Figure actualFigure = board.getFigure(actualPos);

        //create a tmpBoard with the new untested figure position
        Board tmpBoard = new Board(board);
        //perform the Figure move on a temporary board. IMPORTANT this move is untested and can be illegal
        tmpBoard.setFigure(actualPos, new None());
        tmpBoard.setFigure(targetPos, actualFigure);
        //check chess
        if(Board.kingInCheck(tmpBoard, actualFigure.getTeam())) return false;

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
