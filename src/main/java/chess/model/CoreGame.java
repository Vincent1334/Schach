package chess.model;

import java.util.ArrayList;

public class CoreGame {

    private Board board;
    private int activePlayer = 0;
    private int gameMode = 0;
    private ArrayList<Figure> beatenFigures = new ArrayList<>();

    public CoreGame(int gameMode){
        board = new Board();
        this.gameMode = gameMode;

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
        //check valid move
        if(move.size() == 4){
            Integer posX = move.get(0);
            Integer posY = move.get(1);
            if(board.getFigure(posX, posY).getTeam() == activePlayer){

                Integer newX = move.get(2);
                Integer newY = move.get(3);
                Figure actualFigure = board.getFigure(posX, posY);
                Figure targetFigure = board.getFigure(newX, newY);

                //Check if move is possible
                if(actualFigure.validMove(newX, newY, board)){

                    // check if new field is empty and makeMove
                    if (targetFigure instanceof None) {
                        // set figure
                        board.setFigure(newX, newY, actualFigure);
                        // remove old figure
                        board.setFigure(posX, posY, targetFigure);
                    }
                    // check if figure standing on the target field is of opposite color, makeMove and add targetFigure to beatenFigures
                    if (targetFigure.getTeam() == actualFigure.team) {
                        beatenFigures.add(targetFigure);
                        // set figure
                        board.setFigure(newX, newY, actualFigure);
                        // remove old figure
                        board.setFigure(posX, posY, new None(posX, posY,1));
                    }

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
