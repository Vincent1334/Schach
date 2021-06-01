package chess.ki;

import chess.figures.None;
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

public class Computer {

    private boolean playerMax, playerMin;
    private Board board;
    private int targetDepth = 4;
    private Move bestMove;
    private Move lastMove;

    private int[] mobility = new int[2];

    /**
     *the construcot of the computer
     * @param isBlack
     */
    public Computer(boolean isBlack) {
        this.playerMax = isBlack;
        this.playerMin = !isBlack;

        bestMove = new Move(new Position(0, 0), new Position(0, 0));
        lastMove = new Move(new Position(1, 1), new Position(1, 1));

    }

    /**
     * calls the alpha beta pruning to determine the best move and executes it
     * saves the move in lastMove
     * @param board
     * @return best move for the computer
     */
    public Move makeMove(Board board) {

        this.board = new Board(board);
        changeDepth();
        max(targetDepth, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, new ArrayList<Move>());
        System.out.println(bestMove.toString());
        lastMove = new Move(bestMove.getActualPosition(), bestMove.getTargetPosition());
        return bestMove;
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
        ArrayList<Move> possibleMove = getPossibleMoves(board, playerMax);
        sortMove(possibleMove, parentCutOff, playerMax);

        //create CutOff
        ArrayList<Move> cutOff = new ArrayList<Move>();

        Board tmpBoard = new Board(board);
        for(int i = 0; i < possibleMove.size(); i++){
            performMove(possibleMove.get(i).getActualPosition(), possibleMove.get(i).getTargetPosition(), board);
            float value = min(depth-1, maxValue, beta, cutOff);
            mobility[0] = possibleMove.size();
            board = new Board(tmpBoard);
            if(value > maxValue){
                maxValue = value;
                if(depth == targetDepth){
                    bestMove = possibleMove.get(i);
                }
                if(maxValue >= beta){
                    parentCutOff.add(possibleMove.get(i));
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
        //generate possible moves
        ArrayList<Move> possibleMove = getPossibleMoves(board, playerMin);
        sortMove(possibleMove, parentCutOff, playerMin);

        //create CutOff
        ArrayList<Move> cutOff = new ArrayList<Move>();

        Board tmpBoard = new Board(board);
        for(int i = 0; i < possibleMove.size(); i++){
            performMove(possibleMove.get(i).getActualPosition(), possibleMove.get(i).getTargetPosition(), board);
            float value = max(depth-1, alpha, minValue, cutOff);
            mobility[1] = possibleMove.size();
            board = new Board(tmpBoard);
            if(value < minValue){
                minValue = value;
                if(minValue <= alpha){
                    parentCutOff.add(possibleMove.get(i));
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

        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                if(!(board.getFigure(x, y) instanceof None)){
                    switch(board.getFigure(x, y).getFigureID()){
                        case 1: material[board.getFigure(x, y).isBlackTeam() ? 1 : 0][0] ++; break;
                        case 2: material[board.getFigure(x, y).isBlackTeam() ? 1 : 0][1] ++; break;
                        case 3: material[board.getFigure(x, y).isBlackTeam() ? 1 : 0][2] ++; break;
                        case 4: material[board.getFigure(x, y).isBlackTeam() ? 1 : 0][3] ++; break;
                        case 5: material[board.getFigure(x, y).isBlackTeam() ? 1 : 0][4] ++; break;
                        case 6: material[board.getFigure(x, y).isBlackTeam() ? 1 : 0][5] ++; break;
                    }
                }
            }
        }


        //King material
        return 200*(material[isBlack ? 1 : 0][5]-material[isBlack ? 0 : 1][5])
                //Queen material
                + 9*(material[isBlack ? 1 : 0][4]-material[isBlack ? 0 : 1][4])
                //Rook material
                + 5*(material[isBlack ? 1 : 0][1]-material[isBlack ? 0 : 1][1])
                //Bishop and knight material
                + 3*((material[isBlack ? 1 : 0][2]-material[isBlack ? 0 : 1][2]) + (material[isBlack ? 1 : 0][3]-material[isBlack ? 0 : 1][3]))
                //pawn material
                + (material[isBlack ? 1 : 0][0]-material[isBlack ? 0 : 1][0])
                //mobility
                + 0.01f*(mobility[isBlack ? 1 : 0] - mobility[!isBlack ? 1 : 0])
                //repeat
                - 0.5f*((bestMove.getActualPosition() == lastMove.getTargetPosition() && bestMove.getTargetPosition() == lastMove.getActualPosition()) ? 1 : 0)
                //castling
                + 10*((board.getCastlingFlag(isBlack) ? 1 : 0) - (board.getCastlingFlag(!isBlack) ? 1 : 0))
                //check Chess and StaleMate
                -300*(Board.checkChessAndStaleMate(board, isBlack) ? 1 : 0)
                //check chess
                -3*((board.getCheckFlag(isBlack) ? 1 : 0));
    }

    /*
    <---Change-depth--------------------------------------------------------------------------------------------------->
    */

    /**
     * increases the search depth
     */
    private void changeDepth(){
        if(board.getBeatenFigures().size() % 10 == 0 && board.getBeatenFigures().size() != 0){
            targetDepth = targetDepth + targetDepth / 2;
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
        for(int i = 0; i < cutOff.size(); i++){
            if(moves.contains(cutOff.get(i))){
                moves.remove(cutOff.get(i));
                moves.add(0, cutOff.get(i));
            }
        }
    }

    /*
    <---Generate-possible-moves---------------------------------------------------------------------------------------->
     */

    private ArrayList<Move> getPossibleMoves(Board board, boolean blackTeam){
        ArrayList<Move> possibleMove = new ArrayList<Move>();

        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                if(!(board.getFigure(x, y) instanceof  None) && board.getFigure(x, y).isBlackTeam() == blackTeam){
                    checkPossibleTarget(board, possibleMove, x, y, blackTeam);
                }
            }
        }
        return possibleMove;
    }

    private void checkPossibleTarget(Board board, ArrayList<Move> possibleMove, int x, int y, boolean blackTeam){
        for (int newX = 0; newX < 8; newX++) {                      // test all possible moves to every possible targetField
            for (int newY = 0; newY < 8; newY++) {
                Board tmpBoard = new Board(board);      // on a copy of the board

                if (possibleSolution(new Position(x, y), new Position(newX, newY), tmpBoard, blackTeam)) {
                    possibleMove.add(new Move(new Position(x, y), new Position(newX, newY)));
                }
            }
        }
    }

    private boolean possibleSolution(Position actualPos, Position targetPos, Board tmpBoard, boolean blackTeam) {
        if(!performMove(actualPos, targetPos, tmpBoard)) return false;
        return !Board.kingInCheck(tmpBoard, blackTeam);
    }

    private boolean performMove(Position actualPos, Position targetPos, Board tmpBoard){
        if (Rules.checkEnPassant(actualPos, targetPos, tmpBoard)) {
            Rules.performEnPassantMove(actualPos, targetPos, tmpBoard);
            return true;
        }
        if (Rules.checkCastling(actualPos, targetPos, tmpBoard)) {
            Rules.performCastlingMove(actualPos, targetPos, tmpBoard);
            return true;
        }
        if (Rules.checkPawnConversion(actualPos, targetPos, tmpBoard)) {
            Rules.performPawnConversion(actualPos, targetPos, 5, tmpBoard);
            return true;
        }
        if (Rules.checkDefaultMove(actualPos, targetPos, tmpBoard)) {
            Rules.performDefaultMove(actualPos, targetPos, tmpBoard);
            return true;
        }
        return false;
    }
}

