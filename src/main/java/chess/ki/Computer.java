package chess.ki;

import chess.figures.Figure;
import chess.figures.None;
import chess.model.Board;
import chess.model.Move;
import chess.model.Position;
import chess.model.Rules;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Computer {

    private boolean playerMax, playerMin;
    private Board board;
    private int targetDepth = 4;
    private Move bestMove;

    public Computer(boolean isBlack) {
        this.playerMax = isBlack;
        this.playerMin = !isBlack;
    }

    public Move makeMove(Board board) {
        this.board = new Board(board);

        int score = max(targetDepth, -10000, 10000);
        System.out.println(bestMove.toString());
        return bestMove;
    }

    private int max(int depth, int alpha, int beta){
        if(depth == 0) return heuristic(board, playerMax);
        int maxValue = alpha;
        //generate possible moves
        ArrayList<Move> possibleMove = getPossibleMoves(board, playerMax);
        Board tmpBoard = new Board(board);
        for(int i = 0; i < possibleMove.size(); i++){
            performMove(possibleMove.get(i).getActualPosition(), possibleMove.get(i).getTargetPosition(), board);
            int value = min(depth-1, maxValue, beta);
            board = new Board(tmpBoard);
            if(value > maxValue){
                maxValue = value;
                if(depth == targetDepth){
                    bestMove = possibleMove.get(i);
                }
                if(maxValue >= beta){
                    break;
                }
            }
        }
        return maxValue;
    }

    private int min(int depth, int alpha, int beta){
        if(depth == 0) return heuristic(board, playerMin);
        int minValue = beta;
        //generate possible moves
        ArrayList<Move> possibleMove = getPossibleMoves(board, playerMin);
        Board tmpBoard = new Board(board);
        for(int i = 0; i < possibleMove.size(); i++){
            performMove(possibleMove.get(i).getActualPosition(), possibleMove.get(i).getTargetPosition(), board);
            int value = max(depth-1, alpha, minValue);
            board = new Board(tmpBoard);
            if(value < minValue){
                minValue = value;
                if(minValue <= alpha){
                    break;
                }
            }
        }
        return minValue;
    }

    private int heuristic(Board board, boolean isBlack) {

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
                + (material[isBlack ? 1 : 0][0]-material[isBlack ? 0 : 1][0]);
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

