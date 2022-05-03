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
        for (KalahBoard candidate : possibleActions) {
            int temp = v;
            size++;
            if(candidate.isBonus())
                v = Integer.max(v, max(candidate, Integer.MAX_VALUE, Integer.MIN_VALUE,horizon - 1, heuristic));
            else
                v = Integer.max(v, min(candidate, Integer.MAX_VALUE, Integer.MIN_VALUE,horizon - 1, heuristic));
            if (temp != v) action = candidate;
        }
        int indexOfAction = possibleActions.indexOf(action);
        System.out.println("size: " + size);
        System.out.println("action: " + indexOfAction);
        return indexOfAction;
    }


    private static int min(KalahBoard board, int alpha, int beta, int depth, boolean heuristic) {
        if (depth == 0 || board.isFinished()) return board.getAKalah();
        int v = Integer.MAX_VALUE;

        List<KalahBoard> possibleActions;
        if (heuristic) possibleActions = heuristic(board, true);
        else possibleActions = board.possibleActions();

        for (KalahBoard candidate : possibleActions) {
            size++;
            if(candidate.isBonus())
                v = Integer.min(v, min(candidate, alpha, beta, depth - 1, heuristic));
            else
                v = Integer.min(v, max(candidate, alpha, beta, depth - 1, heuristic));

            if (v <= alpha) return v; // Alpha-Cutoff

            beta = Integer.min(beta, v);
        }
        return v;
    }

    private static int max(KalahBoard board, int alpha, int beta, int depth, boolean heuristic) {

        if (depth == 0 || board.isFinished()) return board.getBKalah();
        int v = Integer.MIN_VALUE;

        List<KalahBoard> possibleActions;
        if (heuristic) possibleActions = heuristic(board, true);
        else possibleActions = board.possibleActions();

        for (KalahBoard candidate : possibleActions) {
            size++;
            if(candidate.isBonus())
                v = Integer.max(v, max(candidate, alpha, beta,depth - 1, heuristic));
            else
                v = Integer.max(v, min(candidate, alpha, beta,depth - 1, heuristic));

            // unnötige Rekursion vermeiden
            if (v >= beta) return v; // Beta-Cutoff
            alpha = Integer.max(alpha, v);
        }
        return v;
    }

    private static List<KalahBoard> heuristic(KalahBoard board, boolean maximizedPlayer) {
        List<KalahBoard> possibleBoardsLeft = board.possibleActions();
        List<KalahBoard> sortedBoards = new LinkedList<>();
        while (true) {
            if (possibleBoardsLeft.size() == 0) {
                return sortedBoards;
            }
            KalahBoard currentBoard = possibleBoardsLeft.get(0);

            for (KalahBoard candidate : possibleBoardsLeft) {
                if(maximizedPlayer){
                    if (candidate.getBKalah() >= currentBoard.getBKalah()) {
                        currentBoard = candidate;
                    }
                } else {
                    if (candidate.getAKalah() <= currentBoard.getAKalah()) {
                        currentBoard = candidate;
                    }
                }
            }
            possibleBoardsLeft.remove(currentBoard);
            sortedBoards.add(currentBoard);
        }
    }
}
