package puzzle8;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * Klasse IDFS für iterative deepening depth-first search
 *
 * @author Paul Rueß
 */
public class IDFS {

    private static Deque<Board> dfs(Board curBoard, Deque<Board> path, int limit) {

        List<Board> possibleBoards = curBoard.possibleActions();
        if (curBoard.isSolved()) return path;
        if (limit == 0) return null;

        for (Board child : possibleBoards) {
            if (path.contains(child)) continue;
            path.add(child);
            Deque<Board> res = dfs(child, path, limit - 1);
            if (res != null) return res;
            path.removeLast();
        }
        return null;
    }


    private static Deque<Board> idfs(Board curBoard, Deque<Board> path) {
        for (int limit = 5; limit < Integer.MAX_VALUE; limit++) {
            Deque<Board> result = dfs(curBoard, path, limit);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public static Deque<Board> idfs(Board curBoard) {
        Deque<Board> path = new LinkedList<>();
        path.addLast(curBoard);
        return idfs(curBoard, path);
    }
}
