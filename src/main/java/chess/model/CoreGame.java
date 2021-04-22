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
                if(checkEnPassant(move.get("posX"), move.get("posY"),move.get("newX"),move.get("newY"))){
                    performEnPassantMove(move.get("posX"), move.get("posY"),move.get("newX"),move.get("newY"));
                    switchPlayer();
                    checkChessMate(activePlayer);
                    return true;
                }
                //check Castling
                if(checkCastling()){
                    performCastlingMove(move);
                    switchPlayer();
                    checkChessMate(activePlayer);
                    return true;
                }
                //check Pawn conversion
                if(checkPawnConversion()){
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
            if(checkPawnConversion()){
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
        if(board.getFigure(move.get("posX"), move.get("posY")).validMove(move.get("posX"), move.get("posY"), move.get("newX"), move.get("newY"), board)){
            //create a tmpBoard with the new untested figure position
            Board tmpBoard = board;
            //perform the Figure move an a temporary board. IMPORTANT this move is untested and can be illegal
            tmpBoard.setFigure(move.get("posX"), move.get("posY"), new None());
            tmpBoard.setFigure(move.get("newX"), move.get("newY"), board.getFigure(move.get("posX"), move.get("posY")));
            if(!threatenKing(tmpBoard, board.getFigure(move.get("posX"), move.get("posY")).getTeam())) return true;
        }
        return false;
    }

    /**
     * makes a standard move on the board.
     * @param move actual move
     */
    public void performDefaultMove(Map<String, Integer> move){
        if(!(board.getFigure(move.get("newX"), move.get("newY")) instanceof None)) beatenFigures.add(board.getFigure(move.get("newX"), move.get("newY")));
        board.setFigure(move.get("newX"), move.get("newY"), board.getFigure(move.get("posX"), move.get("posY")));
        board.setFigure(move.get("posX"), move.get("posY"), new None());
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
     */
    public boolean checkCastling(){
        //TODO: finish method
        return false;
    }

    public void performCastlingMove(Map <String, Integer> move){
        //TODO: finish method
    }

    /**
     * <------Pawn-conversion------------------------------------------------------------------------------------------->
     */

    /**
     * check possible pawn conversion
     * @return Whether the move is valid or not
     */
    public boolean checkPawnConversion(){
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
        //Searching target King position
        int[] kingPos = tmpBoard.getKing(team);
        //Check if any enemy figure can do a valid move to King Position
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                if(tmpBoard.getFigure(x, y).getTeam() != team){
                    Map<String, Integer> tmpMove = new HashMap<String, Integer>();
                    tmpMove.put("posX", x);
                    tmpMove.put("posY", y);
                    tmpMove.put("newX", kingPos[0]);
                    tmpMove.put("newY", kingPos[1]);
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
