package chess.ai;

import chess.figures.None;
import chess.gui.Logic;
import chess.model.Board;
import chess.model.Move;
import chess.model.Position;
import chess.model.Rules;
import java.util.ArrayList;
import java.util.List;

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
    private final boolean PLAYER_MAX;
    private final boolean PLAYER_MIN;

    private Board board;
    private int targetDepth = 5;
    private Move bestMove, lastMove;

    /**
     *the constructor of the computer for the CLI game
     *
     * @param isBlack color of the player
     */
    public Computer(boolean isBlack) {

        //setup Player
        this.PLAYER_MAX = isBlack;
        this.PLAYER_MIN = !isBlack;

        //define default best move
        bestMove = new Move(new Position(0, 0), new Position(0, 0));

        //setup Thread
        thread = new Thread(this);
    }

    /**
     * The constructor of the computer for the GUI game
     *
     * @param isBlack color of the player
     * @param gui the graphical user interface
     */
    public Computer(boolean isBlack, Logic gui){
        //setup Player
        this.PLAYER_MAX = isBlack;
        this.PLAYER_MIN = !isBlack;

        //set GUI controller
        this.gui = gui;

        //define default best move
        bestMove = new Move(new Position(0, 0), new Position(0, 0));

        //setup Thread
        thread = new Thread(this);
    }

    /**
     * calls the alpha beta pruning to determine the best move and
     * saves it in lastMove
     *
     * @param board the current board
     */
    public void makeMove(Board board) {
        this.board = new Board(board);
        changeDepth();
        thread = new Thread(this);
        thread.start();
    }

    /**
     * returns the determined best move
     *
     * @return the best move
     */
    public Move getMove(){
        lastMove = new Move(bestMove.getACTUAL_POSITION(), bestMove.getTARGET_POSITION());
        lastMove.setActualFigure(bestMove.getActualFigure());
        lastMove.setTargetFigure(bestMove.getTargetFigure());
        return bestMove;
    }

    @Override
    public void run(){
        if(!thread.isInterrupted()) max(targetDepth, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, new CutOff(new ArrayList<Move>(), null));
        if(gui != null && !thread.isInterrupted()) gui.computerOrNetworkIsFinish();
    }

    /*
    <---Alpha-Beta-Pruning--------------------------------------------------------------------------------------------->
     */


    /**
     * calculates the max values of the search tree
     *
     * @param depth depth of the search tree
     * @param alpha alpha value
     * @param beta beta value
     * @param ParentCutOff the parent cut off
     * @return maxValue
     */
    private float max(int depth, float alpha, float beta, CutOff ParentCutOff){
        if(thread.isInterrupted()) return 0;
        if(depth == 0) return heuristic(board, ParentCutOff.getLastMove());
        float maxValue = alpha;

        //generate possible moves
        List<Move> possibleMove = generatePossibleMove(PLAYER_MAX);
        sortMove(possibleMove, ParentCutOff.getParentCutOff());

        //Game over?
        if (possibleMove.size() == 0) return Float.NEGATIVE_INFINITY;

        //create CutOff
        ArrayList<Move> cutOff = new ArrayList<Move>();

        Board tmpBoard = new Board(board);
        for (Move move : possibleMove) {
            performMove(move.getACTUAL_POSITION(), move.getTARGET_POSITION(), board);
            float value = min(depth - 1, maxValue, beta, new CutOff(cutOff, move));
            board = new Board(tmpBoard);
            if (value > maxValue) {
                maxValue = value;
                if (depth == targetDepth) {
                    bestMove = move;
                }
                if (maxValue >= beta) {
                    ParentCutOff.getParentCutOff().add(move);
                    break;
                }
            }
        }
        return maxValue;
    }


    /**
     * calculates the min values of the search tree
     *
     * @param depth depth of the search tree
     * @param alpha alpha value
     * @param beta beta value
     * @param ParentCutOff the parent cut off
     * @return minValue
     */
    private float min(int depth, float alpha, float beta, CutOff ParentCutOff){
        if(thread.isInterrupted()) return 0;
        if(depth == 0) return heuristic(board, ParentCutOff.getLastMove());
        float minValue = beta;

        //create Possible Moves
        List<Move> possibleMove = generatePossibleMove(PLAYER_MIN);
        sortMove(possibleMove, ParentCutOff.getParentCutOff());

        //Game over?
        if (possibleMove.size() == 0){
            if(board.isCheckMateFlag(PLAYER_MIN)){
                return Float.POSITIVE_INFINITY;
            }
            return Float.NEGATIVE_INFINITY;
        }

        //create CutOff
        List<Move> cutOff = new ArrayList<Move>();

        Board tmpBoard = new Board(board);
        for (Move move : possibleMove) {
            performMove(move.getACTUAL_POSITION(), move.getTARGET_POSITION(), board);
            float value = max(depth - 1, alpha, minValue, new CutOff(cutOff, move));
            board = new Board(tmpBoard);
            if (value < minValue) {
                minValue = value;
                if (minValue <= alpha) {
                    ParentCutOff.getParentCutOff().add(move);
                    break;
                }
            }
        }
        return minValue;


    }

    /**
     * Interrupt the computer thread
     */
    public void killComputer(){
        thread.interrupt();
        gui.setNotification(false, "");
    }

    /**
     * Check if the computer is calculationg
     * @return true when the computer is calculating
     */
    public boolean isAlive(){
        return thread.isAlive();
    }

    /*
    <---Heuristic------------------------------------------------------------------------------------------------------>
     */

    /**
     * determines the value of the move
     *
     * @param board the current board
     * @return the value of the searched move
     */
    private float heuristic(Board board, Move move) {
        boolean isEndGame;

        //setEndGame
        isEndGame = board.getBeatenFigures().size() >= 26;

        //check Material
        int[][] material = new int[2][6];
        int[][] fieldScore = new int[2][6];

        float score = 0;

        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                if(!(board.getFigure(x, y) instanceof None)){
                    material[board.getFigure(x, y).isBlack() ? 1 : 0][board.getFigure(x, y).getFigureID()-1] ++;
                    fieldScore[board.getFigure(x, y).isBlack() ? 1 : 0][board.getFigure(x, y).getFigureID()-1] += PieceSquareTable.getTable(board.getFigure(x, y).getFigureID(), isEndGame)[board.getFigure(x, y).isBlack() ?  7-x : x][board.getFigure(x, y).isBlack() ? 7-y : y];
                }
            }
        }

        score += Heuristic.checkRepeat(move, lastMove, PLAYER_MAX);
        score += Heuristic.checkFigureScore(move, PLAYER_MAX);
        score += Heuristic.checkMaterial(material, PLAYER_MAX);
        score += Heuristic.checkFieldScore(fieldScore, PLAYER_MAX);
        score += Heuristic.checkCastling(board, PLAYER_MAX);
        score += Heuristic.checkChessMate(board, PLAYER_MAX);
        score += Heuristic.checkChess(board, PLAYER_MAX);
        score += Heuristic.checkPawnChain(board, move, PLAYER_MAX);
        score += Heuristic.checkEndGame(board, PLAYER_MAX, isEndGame);

        return score;
    }

    /*
    <---Change-depth--------------------------------------------------------------------------------------------------->
    */

    /**
     * increases the search depth
     */
    private void changeDepth(){
        if(board.getBeatenFigures().size() % 15 == 0 && board.getBeatenFigures().size() != 0){
            targetDepth = targetDepth + targetDepth / 4;
        }
    }

    /*
    <---Move-Sort------------------------------------------------------------------------------------------------------>
     */

    /**
     * pre-sorts the moves
     *
     * @param moves list of possible moves
     * @param cutOff list of cutoff moves
     */
    private void sortMove(List<Move> moves, List<Move> cutOff){
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
     * Generates a list with all possible moves. Also adds used figure and attacked figure
     *
     * @param player the checked team
     * @return list of possible moves
     */
     private List<Move> generatePossibleMove(boolean player){
         //generate possible moves
         List<Move> possibleMove = new ArrayList<Move>();
         for(int y = 0; y < 8; y++){
             for(int x = 0; x < 8; x++){
                 if(board.getFigure(x, y).isBlack() == player && !(board.getFigure(x, y) instanceof None)){
                     List<Position> tmpPos = Rules.possibleTargetFields(new Position(x, y), board);
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
     * Perform move in board node
     *
     * @param actualPos the actual position
     * @param targetPos the target position
     * @param tmpBoard copy-board
     */
    private void performMove(Position actualPos, Position targetPos, Board tmpBoard){
        if (Rules.checkEnPassant(actualPos, targetPos, tmpBoard)) {
            Rules.performEnPassantMove(actualPos, targetPos, tmpBoard);
            updateCheckFlags(tmpBoard);
            return;
        }
        if (Rules.checkCastling(actualPos, targetPos, tmpBoard)) {
            Rules.performCastlingMove(actualPos, targetPos, tmpBoard);
            tmpBoard.setCastlingFlag(true, tmpBoard.getFigure(actualPos).isBlack());
            updateCheckFlags(tmpBoard);
            return;
        }
        if (Rules.checkPawnConversion(actualPos, targetPos, tmpBoard)) {
            Rules.performPawnConversion(actualPos, targetPos, 5, tmpBoard);
            updateCheckFlags(tmpBoard);
            return;
        }
        if (Rules.checkDefaultMove(actualPos, targetPos, tmpBoard)) {
            Rules.performDefaultMove(actualPos, targetPos, tmpBoard);
            updateCheckFlags(tmpBoard);
        }
    }

    /**
     * updates the check flag in board class
     *
     * @param tmpBoard the temporary board
     */
    private void updateCheckFlags(Board tmpBoard){
        //Check check and set Flags
        Board.kingInCheck(tmpBoard, PLAYER_MAX);
        Board.kingInCheck(tmpBoard, PLAYER_MIN);
    }

    /**
     * Waiting for thread
     *
     * @return thread
     */
    public Thread getThread(){
        return this.thread;
    }

    /**
     * Computer team
     * @return true when computer is black
     */
    public boolean isBlack(){
        return PLAYER_MAX;
    }
}

