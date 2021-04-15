package chess.model;

import java.util.ArrayList;

public class CoreGame {

    private Board board;
    private int activePlayer = 0;

    public CoreGame(){
        board = new Board();

    }

    public Board getBoard(){
        return board;
    }

    /**
     * Checks valid move and passes move order to figure
     * @param in User input
     * @return true if valid move
     */
    public boolean chessMove(String in){
        //translate user input to position
        ArrayList<Integer> move = parse(in);
        //Check whether the player wants to play his own figure
        if(move.size() == 4 && board.getFigure(move.get(0), move.get(1)).getTeam() == activePlayer){
            //Check if move is possible
            if(board.getFigure(move.get(0), move.get(1)).makeMove(move.get(2), move.get(3), board)){
                //Switch active player
                if(activePlayer == 0) activePlayer = 1;
                else activePlayer = 0;
                return true;
            }
        }
        //User command fails
        return false;
    }

    /**
     * Converts user input into coordinates. e.g. a3 == x: 0 y: 2
     * @param input User input
     * @return Move coordinates
     */
    public ArrayList<Integer> parse(String input){
        ArrayList<Integer> pos = new ArrayList<Integer>();
        //"a3-b4" max. 5 chars
        if(input.length() == 5 && input.charAt(2) == 45){
            //split "a3-b4" to "a3" and "b4"
            String[] result = input.split("-");
            if(result.length == 2){
                for(int i = 0; i < 2; i++){
                    String[] xyPosition = result[i].split("");
                    //convert letters to numbers with ASCII code
                    if(xyPosition[0].charAt(0) >= 97 && xyPosition[0].charAt(0) <= 104) pos.add((int) xyPosition[0].charAt(0)-97);
                    //convert numbers to numbers
                    if(xyPosition[1].charAt(0) >= 49 && xyPosition[1].charAt(0) <= 56) pos.add(Integer.parseInt(xyPosition[1])-1);
                }
            }
        }
        //if pos is less than 4 then invalid entry
        return pos;
    }
}
