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
     * move.get(0) == PosX
     * move.get(1) == PosY
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
                //checkValidDefaultMove
                if(checkValidDefaultMove(move)){
                    performDefaultMove(move);
                    return true;
                }





                    //Switch active player
                    if(activePlayer == 0) activePlayer = 1;
                    else activePlayer = 0;
                    return true;
                }
            }
        //Check whether the player wants to play his own figure

        //User command fails
        return false;
    }

    /**
     * checks whether a standard move is valid or not
     * @param move Move information
     * @return  Wether is possible or not
     */
    public boolean checkValidDefaultMove(Map <String, Integer> move){
        if(board.getFigure(move.get("posX"), move.get("posY")).validMove(move, board)){
            //create a tmpBoard with the new untested figure position
            Board tmpBoard = board;
            //perform the Figure move an a temporary board. IMPORTANT this move is untested an can be illegal
            tmpBoard.setFigure(move.get("posX"), move.get("posY"), new None());
            tmpBoard.setFigure(move.get("newX"), move.get("newY"), board.getFigure(move.get("posX"), move.get("posY")));
            if(!checkChess(tmpBoard, board.getFigure(move.get("posX"), move.get("posY")).getTeam())) return true;
        }
        return false;
    }

    /**
     * Check if the king is in chess
     * @param tmpBoard A Temporary Board for unchecked figure positions.
     * @param team The team ID of the checked King
     * @return Whether the king is in chess or not
     */
    public boolean checkChess(Board tmpBoard, int team){
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
        //Check if all enemy figures can do a valid move to King Position
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                if(tmpBoard.getFigure(x, y).getTeam() != team){
                    Map<String, Integer> tmpMove = new HashMap<String, Integer>();
                    tmpMove.put("posX", x);
                    tmpMove.put("posY", y);
                    tmpMove.put("newX", kingX);
                    tmpMove.put("newY", kingY);
                    if(tmpBoard.getFigure(x, y).validMove(tmpMove, tmpBoard)) return true;
                }
            }
        }
        return false;
    }

    /**
     * makes a standard move on the board.
     * @param move Figure position
     */
    public void performDefaultMove(Map<String, Integer> move){
        if(!(board.getFigure(move.get("newX"), move.get("newY")) instanceof None)) beatenFigures.add(board.getFigure(move.get("newX"), move.get("newY")));
        board.setFigure(move.get("newX"), move.get("newY"), board.getFigure(move.get("posX"), move.get("posY")));
        board.setFigure(move.get("posX"), move.get("posY"), new None());
    }

    public void updateEnPassant(Figure actualFigure){
        if(actualFigure instanceof Pawn){
            for(int y = 0; y < 8; y++){
                for(int x = 0; x < 8; x++){
                    if(board.getFigure(x, y) instanceof Pawn && board.getFigure(x, y) != actualFigure) ((Pawn) board.getFigure(x, y)).clearEnPassant();
                }
            }
        }
    }

    public void checkCastling(){

    }

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
