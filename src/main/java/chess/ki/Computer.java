package chess.ki;

import chess.figures.Figure;
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
    private Move bestMove;
    private boolean endGame = false;

    //Points
    private final float pawnMaterial = 100;
    private final float rookMaterial = 500;
    private final float bishopMaterial = 320;
    private final float knightMaterial = 325;
    private final float queenMaterial = 975;
    private final float kingMaterial = 32000;

    private final float castlingPoints = 20;
    private final float checkMatePoints = 100000;
    private final float checkPoints = 50;


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

        //set figure bonus
        float figureScore = 0;
        switch(board.getFigure(move.getTargetPosition()).getFigureID()){
            case 1: figureScore = 100; break;
            case 2: figureScore = 500; break;
            case 3: figureScore = 325; break;
            case 4: figureScore = 320; break;
            case 5: figureScore = 0; break;
            case 6: figureScore = 13; break;
        }

        if(board.getFigure(move.getTargetPosition()).isBlackTeam()) figureScore = figureScore *(-1);


        return  //king
                10000*material[0][5]-10000*material[1][5]
                //Queen material
                + 975*material[0][4]-975*material[1][4]
                //Rook material
                + 500*material[0][1]-500*material[1][1]
                //Bishop material
                + 320*material[0][2]-320*material[1][2]
                //knight material
                + 325*material[0][3]-325*material[1][3]
                //pawn material
                + 100*material[0][0]-100*material[1][0]
                //Pawn Table
                + fieldScore[0][0]-fieldScore[1][0]
                //knight table
                + fieldScore[0][2]-fieldScore[1][2]
                //bishop table
                + fieldScore[0][3]-fieldScore[1][3]
                //king table
                + fieldScore[0][5]-fieldScore[1][5];
                //Figure Score

    }

    public float checkFigureScore(Move move){
        float figureScore = 0;
        switch(move.getActualFigure().getFigureID()){
            case 1: figureScore = pawnMaterial; break;
            case 2: figureScore = rookMaterial; break;
            case 3: figureScore = bishopMaterial; break;
            case 4: figureScore = knightMaterial; break;
            case 5: figureScore = queenMaterial; break;
            case 6: figureScore = kingMaterial; break;
        }
        if(move.getActualFigure().isBlackTeam() != playerMax){
            figureScore = figureScore * (-1);
        }
        return figureScore;
    }

    public float checkMaterial(int[][] material){
        return//king
                kingMaterial*material[playerMax ? 1 : 0][5]-kingMaterial*material[playerMin ? 1 : 0][5]
                //Queen material
                + queenMaterial*material[playerMax ? 1 : 0][4]-queenMaterial*material[playerMin ? 1 : 0][4]
                //Rook material
                + rookMaterial*material[playerMax ? 1 : 0][1]-rookMaterial*material[playerMin ? 1 : 0][1]
                //Bishop material
                + bishopMaterial*material[playerMax ? 1 : 0][2]-bishopMaterial*material[playerMin ? 1 : 0][2]
                //knight material
                + knightMaterial*material[playerMax ? 1 : 0][3]-knightMaterial*material[playerMin ? 1 : 0][3]
                //pawn material
                + pawnMaterial*material[playerMax ? 1 : 0][0]-pawnMaterial*material[playerMin ? 1 : 0][0];
    }

    public float checkCastling(){
        return (board.getCastlingFlag(playerMax) ? castlingPoints : 0) - (board.getCastlingFlag(playerMin) ? castlingPoints : 0);
    }

    public float checkChessMate(){
        return (board.getCheckMateFlag(playerMax) ? -checkMatePoints : 0) + (board.getCastlingFlag(playerMin) ? checkMatePoints : 0);
    }

    public float checkChess(){
        return (board.getCheckFlag(playerMax) ? -checkPoints : 0) + (board.getCheckFlag(playerMin) ? checkPoints : 0);
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
     * Generate a list with all possible moves. Also add used figure and attack Figure
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

