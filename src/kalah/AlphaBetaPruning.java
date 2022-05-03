package kalah;

import java.util.LinkedList;
import java.util.List;

public class AlphaBetaPruning {
    private static int size = 0;
    public static int minimax(KalahBoard board, int horizon, boolean heuristic) {

        int v = Integer.MIN_VALUE;
        List<KalahBoard> possibleActions;
        if (heuristic) possibleActions = heuristic(board, true);
        else possibleActions = board.possibleActions();
        KalahBoard action = possibleActions.get(0);
        for (KalahBoard b : possibleActions) {
            int temp = v;
            size++;
            if(b.isBonus())
                v = Integer.max(v, max(b, Integer.MAX_VALUE, Integer.MIN_VALUE,horizon - 1));
            else
                v = Integer.max(v, min(b, Integer.MAX_VALUE, Integer.MIN_VALUE,horizon - 1));
            if (temp != v) action = b;
        }
        int indexOfAction = possibleActions.indexOf(action);
        System.out.println("size: " + size);
        System.out.println("action: " + indexOfAction);
        return indexOfAction;
    }


    private static int min(KalahBoard board, int alpha, int beta, int depth) {
        if (depth == 0 || board.isFinished()) return board.getAKalah();
        int v = Integer.MAX_VALUE;
        for (KalahBoard b : heuristic(board, false)) {
            size++;
            if(b.isBonus())
                v = Integer.min(v, min(b, alpha, beta, depth - 1));
            else
                v = Integer.min(v, max(b, alpha, beta, depth - 1));

            if (v <= alpha) return v; // Alpha-Cutoff

            beta = Integer.min(beta, v);
        }
        return v;
    }

    private static int max(KalahBoard board, int alpha, int beta, int depth) {

        if (depth == 0 || board.isFinished()) return board.getBKalah();
        int v = Integer.MIN_VALUE;
        for (KalahBoard b : heuristic(board, true)) {
            size++;
            if(b.isBonus())
                v = Integer.max(v, max(b, alpha, beta,depth - 1));
            else
                v = Integer.max(v, min(b, alpha, beta,depth - 1));

            if (v >= beta) return v; // Beta-Cutoff
            alpha = Integer.max(alpha, v);
        }
        return v;
    }

    private static List<KalahBoard> heuristic(KalahBoard board, boolean maximizedPlayer) {
        List<KalahBoard> tmp_list = board.possibleActions();
        List<KalahBoard> sorted_list = new LinkedList<>();
        while (true) {
            if (tmp_list.size() == 0) {
                return sorted_list;
            }
            KalahBoard tmp_board = tmp_list.get(0);

            for (KalahBoard x : tmp_list) {
                if(maximizedPlayer){
                    if (x.getBKalah() >= tmp_board.getBKalah()) {
                        tmp_board = x;
                    }
                } else {
                    if (x.getAKalah() <= tmp_board.getAKalah()) {
                        tmp_board = x;
                    }
                }
            }
            tmp_list.remove(tmp_board);
            sorted_list.add(tmp_board);
        }
    }
}
