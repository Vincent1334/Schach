package chess.ki;

import chess.figures.None;
import chess.model.Board;
import chess.model.Move;
import chess.model.Position;
import chess.model.Rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class contains the information about the processes when the computer does a chess move.
 * It will determine the best move with alpha beta pruning and return the move
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-01
 */

public class Computer implements Runnable{

    private Thread thread;

    private boolean playerMax, playerMin;
    private boolean isTerminate = false;
    private boolean isThinking = false;
    private Board board;
    private int targetDepth = 5;
    private Move bestMove;

    /**
     *the constructor of the computer
     * @param isBlack
     */
    public Computer(boolean isBlack) {

        //setup Player
        this.playerMax = isBlack;
        this.playerMin = !isBlack;

        //define default best move
        bestMove = new Move(new Position(0, 0), new Position(0, 0));

        //setup Thread
        thread = new Thread(this);
    }

    /**
     * calls the alpha beta pruning to determine the best move and executes it
     * saves the move in lastMove
     * @param board
     * @return best move for the computer
     */
    public void makeMove(Board board) {
        if(!isThinking){
            this.board = new Board(board);
            changeDepth();
            isThinking = true;
            thread = new Thread(this);
            thread.start();
        }
    }

    public Move getMove(){
        return bestMove;
    }

    @Override
    public void run(){
        max(targetDepth, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, new ArrayList<Move>());
        isThinking = false;
    }

    /*
    <---Alpha-Beta-Pruning--------------------------------------------------------------------------------------------->
     */

    /**
     * calculates the max values of the search tree
     * @param depth, alpha, beta, parentCutOff
     * @return maxValue
     */
    private float max(int depth, float alpha, float beta, ArrayList<Move> parentCutOff){

        if(depth == 0) return heuristic(board, playerMax);
        float maxValue = alpha;

        //generate possible moves
        ArrayList<Move> possibleMove = generatePossibleMove(playerMax);
        sortMove(possibleMove, parentCutOff, playerMax);

        //Game over?
        if (possibleMove.size() == 0) return 0;

        //create CutOff
        ArrayList<Move> cutOff = new ArrayList<Move>();

        Board tmpBoard = new Board(board);
        for (Move move : possibleMove) {
            performMove(move.getActualPosition(), move.getTargetPosition(), board);
            float value = min(depth - 1, maxValue, beta, cutOff);
            board = new Board(tmpBoard);
            if (value > maxValue) {
                maxValue = value;
                if (depth == targetDepth) {
                    bestMove = move;
                }
                if (maxValue >= beta) {
                    parentCutOff.add(move);
                    break;
                }
            }
        }
        return maxValue;
    }

    /**
     * calculates the min values of the search tree
     * @param depth, alpha, beta, parentCutOff
     * @return minValue
     */
    private float min(int depth, float alpha, float beta, ArrayList<Move> parentCutOff){

        if(depth == 0) return heuristic(board, playerMin);
        float minValue = beta;

        //create Possible Moves
        ArrayList<Move> possibleMove = generatePossibleMove(playerMin);
        sortMove(possibleMove, parentCutOff, playerMin);

        //Game over?
        if (possibleMove.size() == 0) return 0;





        //create CutOff
        ArrayList<Move> cutOff = new ArrayList<Move>();

        Board tmpBoard = new Board(board);
        for (Move move : possibleMove) {
            performMove(move.getActualPosition(), move.getTargetPosition(), board);
            float value = max(depth - 1, alpha, minValue, cutOff);
            board = new Board(tmpBoard);
            if (value < minValue) {
                minValue = value;
                if (minValue <= alpha) {
                    parentCutOff.add(move);
                    break;
                }
            }
        }
        return minValue;
    }

    /*
    <---Heuristic------------------------------------------------------------------------------------------------------>
     */

    /**
     * determines the value of the move
     * @param board, isBlack
     * @return the value of the searched move
     */
    private float heuristic(Board board, boolean isBlack) {

        //check Material
        int[][] material = new int[2][6];
        int[][] fieldScore = new int[2][6];

        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                if(!(board.getFigure(x, y) instanceof None)){
                    switch(board.getFigure(x, y).getFigureID()){
                        //pawn
                        case 1:{
                            material[board.getFigure(x, y).isBlackTeam() ? 1 : 0][0] ++;
                            fieldScore[board.getFigure(x, y).isBlackTeam() ? 1 : 0][0] += PieceSquareTable.pawnTable[board.getFigure(x, y).isBlackTeam() ?  7-x : x][board.getFigure(x, y).isBlackTeam() ? 7-y : y];
                             break;
                        }
                        //rook
                        case 2:{
                            material[board.getFigure(x, y).isBlackTeam() ? 1 : 0][1] ++;
                             break;
                        }
                        //knight
                        case 3:{
                            material[board.getFigure(x, y).isBlackTeam() ? 1 : 0][2] ++;
                            fieldScore[board.getFigure(x, y).isBlackTeam() ? 1 : 0][2] += PieceSquareTable.knightTable[board.getFigure(x, y).isBlackTeam() ?  7-x : x][board.getFigure(x, y).isBlackTeam() ? 7-y : y];
                             break;
                        }
                        //bishop
                        case 4:{
                            material[board.getFigure(x, y).isBlackTeam() ? 1 : 0][3] ++;
                            fieldScore[board.getFigure(x, y).isBlackTeam() ? 1 : 0][3] += PieceSquareTable.bishopTable[board.getFigure(x, y).isBlackTeam() ?  7-x : x][board.getFigure(x, y).isBlackTeam() ? 7-y : y];
                            break;
                        }
                        //queen
                        case 5:{
                            material[board.getFigure(x, y).isBlackTeam() ? 1 : 0][4] ++;
                            break;
                        }
                        //king
                        case 6:{
                            material[board.getFigure(x, y).isBlackTeam() ? 1 : 0][5] ++;
                            fieldScore[board.getFigure(x, y).isBlackTeam() ? 1 : 0][5] += PieceSquareTable.kingTable[board.getFigure(x, y).isBlackTeam() ?  7-x : x][board.getFigure(x, y).isBlackTeam() ? 7-y : y];
                            break;
                        }
                    }
                }
            }
        }


        return
                //Queen material
                 975*(material[isBlack ? 1 : 0][4]-material[isBlack ? 0 : 1][4])
                //Rook material
                + 500*(material[isBlack ? 1 : 0][1]-material[isBlack ? 0 : 1][1])
                //Bishop material
                + 320*((material[isBlack ? 1 : 0][2]-material[isBlack ? 0 : 1][2])
                //knight material
                + 325*(material[isBlack ? 1 : 0][3]-material[isBlack ? 0 : 1][3]))
                //pawn material
                + 100*(material[isBlack ? 1 : 0][0]-material[isBlack ? 0 : 1][0])
                //Pawn Table
                + (fieldScore[isBlack ? 1 : 0][0])
                //knight table
                + (fieldScore[isBlack ? 1 : 0][2])
                //bishop table
                + (fieldScore[isBlack ? 1 : 0][3])
                //king table
                + (fieldScore[isBlack ? 1 : 0][5])
                //check Mate
                +10000*((board.getCheckMateFlag(!isBlack) ? 1 : 0)-(board.getCheckMateFlag(isBlack) ? 1 : 0));
    }

    /*
    <---Change-depth--------------------------------------------------------------------------------------------------->
    */

    /**
     * increases the search depth
     */
    private void changeDepth(){
        if(board.getBeatenFigures().size() % 12 == 0 && board.getBeatenFigures().size() != 0){
            targetDepth = targetDepth + targetDepth / 4;
        }
    }

    /*
    <---Move-Sort------------------------------------------------------------------------------------------------------>
     */

    /**
     * pre-sorts the moves
     * @param moves, cutOff, isBlack
     */
    private void sortMove(ArrayList<Move> moves, ArrayList<Move> cutOff, boolean isBlack){
        for (Move move : cutOff) {
            if (moves.contains(move)) {
                moves.remove(move);
                moves.add(0, move);
            }
        }
    }

    /*
    <---Generate-possible-moves---------------------------------------------------------------------------------------->
     */

     private ArrayList<Move> generatePossibleMove(boolean player){
         //generate possible moves
         ArrayList<Move> possibleMove = new ArrayList<Move>();
         for(int y = 0; y < 8; y++){
             for(int x = 0; x < 8; x++){
                 if(board.getFigure(x, y).isBlackTeam() == player && !(board.getFigure(x, y) instanceof None)){
                     ArrayList<Position> tmpPos = Rules.possibleTargetFields(new Position(x, y), board);
                     for (Position tmpPo : tmpPos) {
                         possibleMove.add(new Move(new Position(x, y), tmpPo));
                     }
                 }
             }
         }
         return possibleMove;
     }


    private void performMove(Position actualPos, Position targetPos, Board tmpBoard){
        if (Rules.checkEnPassant(actualPos, targetPos, tmpBoard)) {
            Rules.performEnPassantMove(actualPos, targetPos, tmpBoard);
            return;
        }
        if (Rules.checkCastling(actualPos, targetPos, tmpBoard)) {
            Rules.performCastlingMove(actualPos, targetPos, tmpBoard);
            return;
        }
        if (Rules.checkPawnConversion(actualPos, targetPos, tmpBoard)) {
            Rules.performPawnConversion(actualPos, targetPos, 5, tmpBoard);
            return;
        }
        if (Rules.checkDefaultMove(actualPos, targetPos, tmpBoard)) {
            Rules.performDefaultMove(actualPos, targetPos, tmpBoard);
        }
    }

    public boolean isFinish(){
         return !isThinking;
    }
}

