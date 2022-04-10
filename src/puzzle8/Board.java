package puzzle8;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Klasse Board für 8-Puzzle-Problem
 *
 * @author Paul Rueß
 */
public class Board {

    /**
     * Problmegröße
     */
    public static final int N = 8;

    /**
     * Board als Feld.
     * Gefüllt mit einer Permutation von 0,1,2, ..., 8.
     * 0 bedeutet leeres Feld.
     */
    protected Integer[] board;

    /**
     * Generiert ein zufälliges Board.
     */
    public Board() {
        Integer[] intArray = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        List<Integer> intList = Arrays.asList(intArray);
        Collections.shuffle(intList);
        board = intList.toArray(intArray);

        while (getParity() % 2 != 0) {
            Collections.shuffle(intList);
            board = intList.toArray(intArray);
        }
    }

    /**
     * Generiert ein Board und initialisiert es mit board.
     *
     * @param board Feld gefüllt mit einer Permutation von 0,1,2, ..., 8.
     */
    public Board(Integer[] board) {
        this.board = board;
    }

    @Override
    public String toString() {
        return "Puzzle{" + "board=" + Arrays.toString(board) + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Board other = (Board) obj;
        return Arrays.equals(this.board, other.board);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Arrays.hashCode(this.board);
        return hash;
    }

    public int getParity() {
        int falsePairs = 0;
        for (int i = 0; i < board.length - 1; i++) {
            int left = board[i];
            if (left == 0) continue;

            for (int j = i + 1; j < board.length; j++) {
                int right = board[j];
                if (right == 0) continue;

                if (left > right) falsePairs++;
            }
        }
        return falsePairs;
    }

    /**
     * Paritätsprüfung.
     *
     * @return Parität.
     */
    public boolean parity() {
        return getParity() % 2 == 0;
    }

    /**
     * Heurstik h1. (siehe Aufgabenstellung)
     *
     * @return Heuristikwert.
     */
    public int h1() {
        int falsePositions = 0;
        for (int i = 0; i < board.length; i++)
            if (!(board[i] == i) && board[i] != 0) falsePositions++;
        return falsePositions;
    }

    /**
     * Heurstik h2. (siehe Aufgabenstellung)
     *
     * @return Heuristikwert.
     */
    public int h2() {
        int manhattanDistance = 0;
        int rowColumnSize = (int) Math.sqrt(board.length);
        for (int i = 0; i < board.length; i++) {
            if (!(board[i] == i) && board[i] != 0) {
                int currRow = i / rowColumnSize;
                int currColumn = i % rowColumnSize;
                int targetRow = board[i] / rowColumnSize;
                int targetColumn = board[i] % rowColumnSize;
                manhattanDistance += Math.abs(currRow - targetRow) + Math.abs(currColumn - targetColumn);
            }
        }
        return manhattanDistance;
    }

    /**
     * Liefert eine Liste der möglichen Aktion als Liste von Folge-Boards zurück.
     *
     * @return Folge-Boards.
     */
    public List<Board> possibleActions() {
        List<Board> boardList = new LinkedList<>();
        int rowColumnSize = (int) Math.sqrt(board.length);
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) {
                int row = i / rowColumnSize;
                int column = i % rowColumnSize;
                int up = row - 1;
                int down = row + 1;
                int left = column - 1;
                int right = column + 1;
                if (up >= 0) boardList.add(doBoardMove(i, i - rowColumnSize));
                if (down < rowColumnSize) boardList.add(doBoardMove(i, i + rowColumnSize));
                if (left >= 0) boardList.add(doBoardMove(i, i - 1));
                if (right < rowColumnSize) boardList.add(doBoardMove(i, i + 1));
            }
        }
        return boardList;
    }

    private Board doBoardMove(int from, int to) {
        Integer[] newBoard = Arrays.copyOf(board, board.length);
        int tmp = newBoard[from];
        newBoard[from] = newBoard[to];
        newBoard[to] = tmp;
        return new Board(newBoard);
    }


    /**
     * Prüft, ob das Board ein Zielzustand ist.
     *
     * @return true, falls Board Ziestzustand (d.h. 0,1,2,3,4,5,6,7,8)
     */
    public boolean isSolved() {
        return Arrays.equals(board, new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8});
    }


    public static void main(String[] args) {
        Board b = new Board(new Integer[]{7, 2, 4, 5, 0, 6, 8, 3, 1});        // abc aus Aufgabenblatt
        Board goal = new Board(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8});

        System.out.println(b);
        System.out.println(b.parity());
        System.out.println(b.h1());
        System.out.println(b.h2());

        for (Board child : b.possibleActions())
            System.out.println(child);

        System.out.println(goal.isSolved());
    }
}

