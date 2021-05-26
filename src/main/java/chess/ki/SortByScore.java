package chess.ki;

import chess.model.Move;

import java.util.Comparator;

public class SortByScore implements Comparator<Move> {

    @Override
    public int compare(Move o1, Move o2) {
        if(o1.getScore()-o2.getScore() < 0) return 1;
        if(o1.getScore()-o2.getScore() > 0) return -1;
        return 0;
    }
}
