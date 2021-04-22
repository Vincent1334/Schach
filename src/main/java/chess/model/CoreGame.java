package chess.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CoreGame {

    private Board board;
    private int activePlayer = 0;
    private int gameMode = 0;
    private ArrayList<Figure> beatenFigures = new ArrayList<Figure>();

    //Chess Events
    boolean enPassant = false;

    public CoreGame(int gameMode){
        board = new Board();
        this.gameMode = gameMode;

    }

    public Board getBoard(){
        return board;
    }

    /**
     * Checks valid move and passes move order to figure
     * move.get(0) == posX
     * move.get(1) == posY
     * move.get(2) == newPosX
     * move.get(3) == newPosY
     * @param in User input
     * @return true if valid move
     */
    public boolean chessMove(String in){
        //translate user input to position
        Map<String, Integer> move = parse(in);
        //check valid move
        if(move.size() == 4){
            if(board.getFigure(move.get("posX"), move.get("posY")).getTeam() == activePlayer){
                //check EnPassant
                if(checkEnPassant(move)){
                    performEnPassantMove(move);
                    switchPlayer();
                    checkChessMate(activePlayer);
                    return true;
                }
                //check Castling
                if(checkCastling(move)){
                    performCastlingMove(move);
                    switchPlayer();
                    checkChessMate(activePlayer);
                    return true;
                }
                //check Pawn conversion
                if(checkPawnConversion(move)){
                    performPawnConversion(move);
                    switchPlayer();
                    checkChessMate(activePlayer);
                    return true;
                }
                //checkValidDefaultMove
                if(checkValidDefaultMove(move)){
                    performDefaultMove(move);
                    switchPlayer();
                    checkChessMate(activePlayer);
                    return true;
                }
            }
        }
        if(move.size() == 5) {
            //check Pawn conversion
            if(checkPawnConversion(move)){
                performPawnConversion(move);
                switchPlayer();
                checkChessMate(activePlayer);
                return true;
            }
        }
        //User command fails
        return false;
    }

    /**
     * <------DefaultMove----------------------------------------------------------------------------------------------->
     */

    /**
     * checks whether a standard move is valid or not
     * @param move actual move
     * @return  Whether move is possible or not
     */
    public boolean checkValidDefaultMove(Map <String, Integer> move){

        Figure actualFigure = board.getFigure(move.get("posX"), move.get("posY"));
        Figure targetFigure = board.getFigure(move.get("newX"), move.get("newY"));

        if(actualFigure.validMove(move.get("posX"), move.get("posY"), move.get("newX"), move.get("newY"), board)){
            //create a tmpBoard with the new untested figure position
            Board tmpBoard = board;
            //perform the Figure move on a temporary board. IMPORTANT this move is untested and can be illegal
            tmpBoard.setFigure(move.get("posX"), move.get("posY"), new None());
            tmpBoard.setFigure(move.get("newX"), move.get("newY"), actualFigure);
            if(!threatenKing(tmpBoard, actualFigure.getTeam()) && targetFigure.getTeam() != actualFigure.getTeam()) {
                return true;
            }
        }
        return false;
    }

    /**
     * makes a standard move on the board.
     * @param move actual move
     */
    public void performDefaultMove(Map<String, Integer> move){

        Figure actualFigure = board.getFigure(move.get("posX"), move.get("posY"));
        Figure targetFigure = board.getFigure(move.get("newX"), move.get("newY"));

        if(!(targetFigure instanceof None)) {
            beatenFigures.add(targetFigure);
        }

        actualFigure.setAlreadyMoved(true);
        board.setFigure(move.get("newX"), move.get("newY"), actualFigure);
        board.setFigure(move.get("posX"), move.get("posY"), targetFigure);
    }

    /**
     * <------EnPassant------------------------------------------------------------------------------------------------->
     */

    /**
     * check valid enPassant move
     * @return Whether the move is valid or not
     * @param move
     */
    public boolean checkEnPassant(Map<String, Integer> move){
        //TODO: Finish enPassant
        return false;
    }

    /**
     * makes a enPassant move on the board.
     * @param move Figure position
     */
    public void performEnPassantMove(Map <String, Integer> move){
        //TODO: finish method
    }

    /**
     * <------Castling-------------------------------------------------------------------------------------------------->
     */

    /**
     * check possible castling
     * @return W
     * @param move
     */
    public boolean checkCastling(Map<String, Integer> move){
        //TODO: finish method

        Integer posX = move.get("posX");
        Integer posY = move.get("posY");
        Integer newX = move.get("newX");
        Figure actualFigure = board.getFigure(posX, posY);

        // castle left
        if (actualFigure instanceof King && newX == posX-2 && !actualFigure.isAlreadyMoved() && !board.getFigure(0,posY).isAlreadyMoved()) {
            for (int j = 2; j < posX; j++) {                              // check, whether all field between are empty
                /*board.setFigure(newX+1,newY,board.getFigure(0,posY));   // move rook (right beside king)
                board.setFigure(0,0,new None());                          // remove rook from original position
                actualFigure.setAlreadyMoved(true);*/
                if (!(board.getFigure(j, posY) instanceof None)) {
                    return false;
                }
            }
            return true;
        }
        // castle right
        if (actualFigure instanceof King && !actualFigure.isAlreadyMoved() && newX == posX+2 && !board.getFigure(7,posY).isAlreadyMoved()) {
            for (int j = posX+1; j < 8; j++) {                                      // check, whether all field between are empty
                if (!(board.getFigure(j, posY) instanceof None)) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    public void performCastlingMove(Map <String, Integer> move){
        //TODO: finish method
        Integer posX = move.get("posX");
        Integer posY = move.get("posY");
        Integer newX = move.get("newX");
        Integer newY = move.get("newY");
        Figure actualFigure = board.getFigure(posX, posY);

        // black castle queenside (top left)
        if (newX == posX-2 && actualFigure.getTeam() == 1) {
            board.getFigure(0,posY).setAlreadyMoved(true);
            board.setFigure(newX+1,newY,board.getFigure(0,posY));   // move rook
            board.setFigure(0,0,new None());                        // replace field where rook was standing
        }
        // black castle kingside (top right)
        if (newX == posX+2 && actualFigure.getTeam() == 1) {
            board.getFigure(7,posY).setAlreadyMoved(true);
            board.setFigure(newX-1,newY,board.getFigure(7,posY));   // move rook
            board.setFigure(7,0,new None());                        // replace field where rook was standing
        }
        // white castle kingside (bottom left)
        if (newX == posX-2 && actualFigure.getTeam() == 0) {
            board.getFigure(0,posY).setAlreadyMoved(true);
            board.setFigure(newX+1,newY,board.getFigure(0,posY));   // move rook
            board.setFigure(0,0,new None());                        // replace field where rook was standing
        }
        // white castle queenside (bottom right)
        if (newX == posX+2 && actualFigure.getTeam() == 0) {
            board.getFigure(7,posY).setAlreadyMoved(true);
            board.setFigure(newX-1,newY,board.getFigure(7,posY));   // move rook
            board.setFigure(7,0,new None());                        // replace field where rook was standing
        }

        // move king
        board.setFigure(newX,newY,actualFigure);
        actualFigure.setAlreadyMoved(true);
    }

    /**
     * <------Pawn-conversion------------------------------------------------------------------------------------------->
     */

    /**
     * check possible pawn conversion
     * @return Whether the move is valid or not
     */
    public boolean checkPawnConversion(Map<String, Integer> move){
        //TODO: finish method
        return false;
    }

    /**
     * makes a pawn conversion move on the board.
     * @param move Figure position
     */
    public void performPawnConversion(Map<String, Integer> move){
        //TODO: finish method
        if(move.size() == 5) {

        }
        // Standard: Umwandlung in Dame
        else {

        }
    }

    /**
     * <------Default-commands------------------------------------------------------------------------------------------>
     */

    /**
     * Check if the king is in check
     * @param tmpBoard A temporary Board for unchecked figure positions.
     * @param team The team ID of the checked King
     * @return Whether the king is in check or not
     */
    public boolean threatenKing(Board tmpBoard, int team){
        //create local Variables
        int kingX = 0;
        int kingY = 0;
        //Searching target King position
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                if(tmpBoard.getFigure(x, y) instanceof King && tmpBoard.getFigure(x, y).getTeam() == team){
                    kingX = x;
                    kingY = y;
                    break;
                }
            }
        }
        //Check if any enemy figure can do a valid move to King Position
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                if(tmpBoard.getFigure(x, y).getTeam() != team){
                    Map<String, Integer> tmpMove = new HashMap<String, Integer>();
                    tmpMove.put("posX", x);
                    tmpMove.put("posY", y);
                    tmpMove.put("newX", kingX);
                    tmpMove.put("newY", kingY);
                    if(tmpBoard.getFigure(x, y).validMove(tmpMove.get("posX"), tmpMove.get("posY"), tmpMove.get("newX"), tmpMove.get("newY"), tmpBoard)) return true;
                }
            }
        }
        return false;
    }

    /**
     * Check chessMate
     * @param team Target king
     * @return
     */
    public boolean checkChessMate(int team){
        //TODO: Finish method
        return false;
    }

    /**
     * switch active player
     */
    public void switchPlayer(){
        if(activePlayer == 0) activePlayer = 1;
        else activePlayer = 0;

        // activePlayer = activePlayer == 0 ? 1 : 0;
    }



/**
 * <------System-components--------------------------------------------------------------------------------------------->
 */

    /**
     * Converts user input into coordinates. e.g. a3 == x: 0 y: 2
     * @param input User input
     * @return Move coordinates
     */
    public Map<String, Integer> parse(String input){
        Map<String, Integer> pos = new HashMap<String, Integer>();
        //"a3-b4" max. 5 chars
        if(input.length() == 5 && input.charAt(2) == 45){
            //split "a3-b4" to "a3" and "b4"
            String[] result = input.split("-");
            if(result.length == 2){
                String[] typ = {"pos", "new"};
                for(int i = 0; i < 2; i++){
                    String[] xyPosition = result[i].split("");
                    //convert letters to numbers with ASCII code
                    if(xyPosition[0].charAt(0) >= 97 && xyPosition[0].charAt(0) <= 104) pos.put(typ[i] + "X", (int) xyPosition[0].charAt(0)-97);
                    //convert numbers to numbers
                    if(xyPosition[1].charAt(0) >= 49 && xyPosition[1].charAt(0) <= 56) pos.put(typ[i] + "Y", Integer.parseInt(xyPosition[1])-1);
                }
            }
        }
        //if pos is less than 4 then invalid entry
        return pos;
    }
}
