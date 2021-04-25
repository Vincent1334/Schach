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
        if(move.size() >= 4){
            Integer posX = move.get("posX");
            Integer posY = move.get("posY");
            Integer newX = move.get("newX");
            Integer newY = move.get("newY");
            if(board.getFigure(posX, posY).getTeam() == activePlayer){
                //check EnPassant
                if(checkEnPassant(posX, posY,newX,newY)){
                    performEnPassantMove(posX, posY,newX,newY);
                    switchPlayer();
                    checkChessMate(activePlayer);
                    return true;
                }
                //check Castling
                if(checkCastling(posX, posY, newX, newY)){
                    performCastlingMove(posX, posY, newX, newY);
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
                if(checkValidDefaultMove(posX, posY, newX, newY)){
                    performDefaultMove(posX, posY, newX, newY);
                    switchPlayer();
                    checkChessMate(activePlayer);
                    return true;
                }
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
     * @param posX, posY, newX, newY
     * @return  Whether move is possible or not
     */
    public boolean checkValidDefaultMove(int posX, int posY, int newX, int newY){

        Figure actualFigure = board.getFigure(posX, posY);
        Figure targetFigure = board.getFigure(newX, newY);

        if(actualFigure.validMove(posX, posY, newX, newY, board)){
            //create a tmpBoard with the new untested figure position
//            Board tmpBoard = new Board();
            Board tmpBoard = board;
            //perform the Figure move on a temporary board. IMPORTANT this move is untested and can be illegal
            tmpBoard.setFigure(posX, posY, new None());
            tmpBoard.setFigure(newX, newY, actualFigure);
            //TODO: mit tmpBoard.setFigure ändern wir das Originalboard, da tmpBoard nur eine Referenz ist und keine richtige Kopie

            //TODO: überprüfen, dass figur auf zielfeld nicht gleich eigener spielfarbe entspricht -> neue Farbe für None einführen

            if(!threatenKing(tmpBoard, actualFigure.getTeam())) {
                return true;
            }
        }
        return false;
    }

    /**
     * makes a standard move on the board.
     * @param posX, posY, newX, newY
     */
    public void performDefaultMove(int posX, int posY, int newX, int newY){

        Figure actualFigure = board.getFigure(posX, posY);
        Figure targetFigure = board.getFigure(newX, newY);

        if(!(targetFigure instanceof None)) {
            beatenFigures.add(targetFigure);
        }

        actualFigure.setAlreadyMoved(true);
        board.setFigure(newX, newY, actualFigure);
        board.setFigure(posX, posY, targetFigure);
    }

    /**
     * <------EnPassant------------------------------------------------------------------------------------------------->
     */

    /**
     * check valid enPassant move
     * @param posX actual x-position for Pawn
     * @param posY actual y-position for Pawn
     * @param newX new input x-position for Pawn
     * @param newY new input y-position for Pawn
     * @return Whether the move is valid or not
     */
    public boolean checkEnPassant(int posX,int posY, int newX, int newY){
        if((board.getFigure(newX,posY) instanceof Pawn) && (board.getFigure(posX,posY) instanceof Pawn)
                && (board.getFigure(newX,posY).getTeam() != board.getFigure(posX,posY).getTeam())
                && (Math.abs(posX - newX) == 1)
                && ((board.getFigure(posX,posY).getTeam() == 0 && newY-posY == 1)
                ||(board.getFigure(posX,posY).getTeam() == 1 && newY-posY == -1))){
            if(((Pawn) board.getFigure(newX,posY)).isEnPassant()){
                return true;
            }
        }
        return false;
    }

    /**
     * makes a enPassant move on the board.
     * @param posX actual x-position for Pawn
     * @param posY actual y-position for Pawn
     * @param newX new input x-position for Pawn
     * @param newY new input y-position for Pawn
     */
    public void performEnPassantMove(int posX,int posY, int newX, int newY){
        beatenFigures.add(board.getFigure(newX,posY));
        board.setFigure(newX,posY,new None());
        board.setFigure(newX,newY,board.getFigure(posX,posY));
    }

    /**
     * <------Castling-------------------------------------------------------------------------------------------------->
     */

    /**
     * check possible castling
     * @return W
     * @param posX, posY, newX, newY
     */
    public boolean checkCastling(int posX, int posY, int newX, int newY){
        //TODO: alle Felder dazwischen dürfen nicht bedroht werden

        Figure actualFigure = board.getFigure(posX, posY);

        // castle left
        if (actualFigure instanceof King && newX == posX-2 && !actualFigure.isAlreadyMoved() && !board.getFigure(0,posY).isAlreadyMoved()) {
            for (int j = 2; j < posX; j++) {                              // check, whether all field between are empty
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

    public void performCastlingMove(int posX, int posY, int newX, int newY){

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
     * @param move the actual move
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
     * @param tmpBoard A temporary board for unchecked figure positions.
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
                    if(tmpBoard.getFigure(x, y).validMove(tmpMove.get("posX"), tmpMove.get("posY"), tmpMove.get("newX"), tmpMove.get("newY"), tmpBoard)) {
                        return true;
                    }
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
        //"a3-b4" or "a3-b4Q"
        if((input.length() == 5 || input.length() == 6) && input.charAt(2) == 45){
            //split "a3-b4Q" to "a3" and "b4Q"
            String[] result = input.split("-");
            if(result.length == 2){
                String[] typ = {"pos", "new"};
                for(int i = 0; i < 2; i++){
                    String[] xyPosition = result[i].split("");
                    //convert letters to numbers with ASCII code
                    if(xyPosition[0].charAt(0) >= 97 && xyPosition[0].charAt(0) <= 104) {
                        pos.put(typ[i] + "X", (int) xyPosition[0].charAt(0)-97);
                    }
                    //convert numbers to numbers
                    if(xyPosition[1].charAt(0) >= 49 && xyPosition[1].charAt(0) <= 56) {
                        pos.put(typ[i] + "Y", Integer.parseInt(xyPosition[1])-1);
                    }
                }
            }
            //split "a7-a8Q" to "a7" and "a8" and "Q" (corresponds to 0)
            if (input.matches("^[a-h][27]-[a-h][18][Q]$")) {
                pos.put("convertPawnTo", 0);
            }
            //split "a7-a8N" to "a7" and "a8" and "N" (corresponds to 1)
            if (input.matches("^[a-h][27]-[a-h][18][N]$")) {
                pos.put("convertPawnTo", 1);
            }
            //split "a7-a8B" to "a7" and "a8" and "B" (corresponds to 2)
            if (input.matches("^[a-h][27]-[a-h][18][B]$")) {
                pos.put("convertPawnTo", 2);
            }
        }
        //if pos is less than 4 then invalid entry
        return pos;
    }
}
