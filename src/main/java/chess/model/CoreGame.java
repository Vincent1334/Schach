package chess.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CoreGame {

    private Board board;
    private int activePlayer = 0;
    private int gameMode = 0;
    private ArrayList<Figure> beatenFigures = new ArrayList<>();

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

                Figure actualFigure = board.getFigure(move.get("posX"), move.get("posY"));
                Figure targetFigure = board.getFigure(move.get("newX"), move.get("newY"));

                //Check if move is possible
                if(actualFigure.validMove(move.get("newX"), move.get("newY"), board)){

                    // check if new field is empty and makeMove
                    if (targetFigure instanceof None) {
                        // set figure
                        board.setFigure(move.get("newX"), move.get("newY"), actualFigure);
                        // remove old figure
                        board.setFigure(move.get("posX"), move.get("posY"), targetFigure);
                    }
                    // check if figure standing on the target field is of opposite color, makeMove and add targetFigure to beatenFigures
                    else if (targetFigure.getTeam() != actualFigure.team) {
                        beatenFigures.add(targetFigure);
                        // set figure
                        board.setFigure(move.get("newX"), move.get("newY"), actualFigure);
                        // remove old figure
                        board.setFigure(move.get("posX"), move.get("posY"), new None(move.get("posX"), move.get("posY"),2));
                    }
                    // check if move is en passant
                    updateEnPassant(actualFigure);
                    // check if player is castling
                    checkCastling();

                    //Switch active player
                    if(activePlayer == 0) activePlayer = 1;
                    else activePlayer = 0;
                    return true;
                }
            }
        }
        //Check whether the player wants to play his own figure

        //User command fails
        return false;
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
