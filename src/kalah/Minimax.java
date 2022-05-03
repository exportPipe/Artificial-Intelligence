package kalah;

import java.util.List;

public class Minimax {
    private static int size = 0;

    public static int minimax(KalahBoard board, int horizon) {
        int v = Integer.MIN_VALUE;
        List<KalahBoard> possibleActions = board.possibleActions();
        KalahBoard action = null;
        for (KalahBoard candidate : possibleActions) {
            int tmp = v;
            size++;
            // nächster Zug ist maximizing Spieler nochmal dran
            if (candidate.isBonus()) v = Integer.max(v, max(candidate, horizon - 1));
            // nächster Zug wird vom minimizing Player geführt
            else v = Integer.max(v, min(candidate, horizon - 1));

            if (tmp != v) action = candidate;
        }
        int indexOfAction = possibleActions.indexOf(action);
        System.out.println("size: " + size);
        System.out.println("action: " + indexOfAction);
        size = 0;
        return indexOfAction;
    }


    private static int min(KalahBoard board, int horizon) {
        // Unterste Iterationstiefe erreicht oder Game Over
        if (horizon == 0 || board.isFinished()) return board.getAKalah();

        // Initialisiere static evaluation
        int v = Integer.MAX_VALUE;

        for (KalahBoard candidate : board.possibleActions()) {
            size++;
            // Minimizing Player ist nochmal dran
            if (board.isBonus()) v = Integer.min(v, min(candidate, horizon - 1));
            // Maximizing Player ist nächster
            else v = Integer.min(v, max(candidate, horizon - 1));
        }
        // static evaluation of pos
        return v;
    }


    private static int max (KalahBoard board, int horizon) {
        if (horizon == 0 || board.isFinished()) return board.getBKalah();
        int v = Integer.MIN_VALUE;
        for (KalahBoard candidate : board.possibleActions()) {
            size++;
            if (board.isBonus()) v = Integer.max(v, max(candidate, horizon - 1));
            else v = Integer.max(v, min(candidate, horizon - 1));

        }
        // static evaluation of pos
        return v;
    }
}

