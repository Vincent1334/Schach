package chess.ki;

import chess.figures.None;
import chess.gui.Logic;
import chess.model.Board;
import chess.model.Move;
import chess.model.Position;
import chess.model.Rules;

import java.util.ArrayList;

/**
 * This class contains the information about the processes when the computer does a chess move.
 * It will determine the best move with alpha beta pruning and return the move
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-01
 */

public class Computer implements Runnable{

    //System
    private Thread thread;
    private Logic gui;

    //a-b pruning
    private boolean playerMax, playerMin;
    private boolean isThinking = false;
    private Board board;
    private int targetDepth = 5;
    private Move bestMove, lastMove;
    private boolean endGame = false;


    /**
     *the constructor of the computer
     * @param isBlack color of the player
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

    public Computer(boolean isBlack, Logic gui){
        //setup Player
        this.playerMax = isBlack;
        this.playerMin = !isBlack;

        //set GUI controller
        this.gui = gui;

        //define default best move
        bestMove = new Move(new Position(0, 0), new Position(0, 0));

        //setup Thread
        thread = new Thread(this);
    }

    /**
     * calls the alpha beta pruning to determine the best move and executes it
     * saves the move in lastMove
     * @param board the current board
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
        lastMove = new Move(bestMove.getActualPosition(), bestMove.getTargetPosition());
        lastMove.setActualFigure(bestMove.getActualFigure());
        lastMove.setTargetFigure(bestMove.getTargetFigure());
        return bestMove;
    }

    @Override
    public void run(){
        max(targetDepth, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, new ArrayList<Move>(), null);
        isThinking = false;
        if(gui != null) gui.computerIsFinish();
    }

    /*
    <---Alpha-Beta-Pruning--------------------------------------------------------------------------------------------->
     */

    /**
     * calculates the max values of the search tree
     * @param depth depth of the search tree
     * @param alpha alpha value
     * @param beta beta value
     * @param parentCutOff the parent cut off
     * @return maxValue
     */
    private float max(int depth, float alpha, float beta, ArrayList<Move> parentCutOff, Move lastMove){

        if(depth == 0) return heuristic(board, lastMove);
        float maxValue = alpha;

        //generate possible moves
        ArrayList<Move> possibleMove = generatePossibleMove(playerMax);
        sortMove(possibleMove, parentCutOff, playerMax);

        //Game over?
        if (possibleMove.size() == 0) return Float.NEGATIVE_INFINITY;

        //create CutOff
        ArrayList<Move> cutOff = new ArrayList<Move>();

        Board tmpBoard = new Board(board);
        for (Move move : possibleMove) {
            performMove(move.getActualPosition(), move.getTargetPosition(), board);
            float value = min(depth - 1, maxValue, beta, cutOff, move);
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
     * @param depth depth of the search tree
     * @param alpha alpha value
     * @param beta beta value
     * @param parentCutOff the parent cut off
     * @return minValue
     */
    private float min(int depth, float alpha, float beta, ArrayList<Move> parentCutOff, Move lastMove){

        if(depth == 0) return heuristic(board, lastMove);
        float minValue = beta;

        //create Possible Moves
        ArrayList<Move> possibleMove = generatePossibleMove(playerMin);
        sortMove(possibleMove, parentCutOff, playerMin);

        //Game over?
        if (possibleMove.size() == 0) return Float.POSITIVE_INFINITY;

        //create CutOff
        ArrayList<Move> cutOff = new ArrayList<Move>();

        Board tmpBoard = new Board(board);
        for (Move move : possibleMove) {
            performMove(move.getActualPosition(), move.getTargetPosition(), board);
            float value = max(depth - 1, alpha, minValue, cutOff, move);
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
     * @param board the current board
     * @return the value of the searched move
     */
    private float heuristic(Board board, Move move) {

        //check Material
        int[][] material = new int[2][6];
        int[][] fieldScore = new int[2][6];

        float score = 0;

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

        score += Heuristic.checkRepeat(move, lastMove);
        score += Heuristic.checkFigureScore(move, playerMax);
        score += Heuristic.checkMaterial(material, playerMax, playerMin);
        score += Heuristic.checkCastling(board, playerMax);
        score += Heuristic.checkChessMate(board, playerMax, playerMin);
        score += Heuristic.checkChess(board, playerMax, playerMin);

        return score;
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
     * @param moves
     * @param cutOff
     * @param isBlack
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

    /**
     * Generate a list with all possible moves. Also add used figure and attacked Figure
     * @param player the checked team
     * @return
     */
     private ArrayList<Move> generatePossibleMove(boolean player){
         //generate possible moves
         ArrayList<Move> possibleMove = new ArrayList<Move>();
         for(int y = 0; y < 8; y++){
             for(int x = 0; x < 8; x++){
                 if(board.getFigure(x, y).isBlackTeam() == player && !(board.getFigure(x, y) instanceof None)){
                     ArrayList<Position> tmpPos = Rules.possibleTargetFields(new Position(x, y), board);
                     for (Position tmpPo : tmpPos) {
                         Move move = new Move(new Position(x, y), tmpPo);
                         move.setActualFigure(board.getFigure(x, y));
                         move.setTargetFigure(board.getFigure(tmpPo));
                         possibleMove.add(move);
                     }
                 }
             }
         }
         return possibleMove;
     }


    /**
     *
     * @param actualPos
     * @param targetPos
     * @param tmpBoard
     */
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

        //update Flags
        Board.kingInCheck(board, playerMax);
        Board.kingInCheck(board, playerMin);
    }

    public boolean isFinish(){
         return !isThinking;
    }
}

