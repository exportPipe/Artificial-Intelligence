package puzzle8;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author Paul Rueß
 */
public class A_Star {
    // cost ordnet jedem Board die Aktuellen Pfadkosten (g-Wert) zu.
    // pred ordnet jedem Board den Elternknoten zu. (siehe Skript S. 2-25).
    // In cost und pred sind genau alle Knoten der closedList und openList enthalten!
    // Nachdem der Zielknoten erreicht wurde, lässt sich aus cost und pred der Ergebnispfad ermitteln.
    private static final HashMap<Board, Integer> cost = new HashMap<>();
    private static final HashMap<Board, Board> pred = new HashMap<>();

    // openList als Prioritätsliste.
    // Die Prioritätswerte sind die geschätzen Kosten f = g + h (s. Skript S. 2-66) (g = aktuelle kosten, h = kosten von heuristik)
    private static final IndexMinPQ<Board, Integer> openList = new IndexMinPQ<>();

    public static Deque<Board> aStar(Board startBoard) {
        cost.clear();
        pred.clear();
        openList.clear();

        if (startBoard.isSolved()) {
            Deque<Board> solvingPath = new LinkedList<>();
            solvingPath.add(startBoard);
            return solvingPath;
        }

        openList.add(startBoard, startBoard.h2());
        cost.put(startBoard, startBoard.h2());

        final LinkedList<Board> closedList = new LinkedList();

        while (!openList.isEmpty()) {
            Board currBoard = openList.removeMin();
            if (currBoard.isSolved()) {
                Deque<Board> solvedPath = new LinkedList<>();
                solvedPath.add(currBoard);
                while ((currBoard = pred.get(currBoard)) != null) solvedPath.addFirst(currBoard);
                return solvedPath;
            }

            closedList.add(currBoard);

            for (Board child : currBoard.possibleActions()) {
                int costs = cost.get(currBoard) + child.h2() + 1;

                if ((openList.get(child) == null) && !closedList.contains(child)) {
                    openList.add(child, costs);
                    pred.put(child, currBoard);
                    cost.put(child, cost.get(currBoard) + 1);
                } else if (openList.get(child) != null) {
                    if (cost.get(currBoard) + 1 < cost.get(pred.get(child)) + 1) {
                        openList.change(child, costs);
                        pred.put(child, currBoard);
                        cost.put(child, cost.get(currBoard) + 1);
                    }
                }
            }
        }
        return null; // Keine Lösung
    }
}
