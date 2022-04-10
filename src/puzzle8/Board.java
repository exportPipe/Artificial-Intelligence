package puzzle8;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Klasse puzzle8.Board für 8-Puzzle-Problem
 * @author Paul Rueß
 */
public class Board {
	/**
	 * Random-Generator für Generierung eines Boards
	 */
	Random rand = new Random();

	/**
	 * Problmegröße
	 */
	public static final int N = 8;

	/**
	 * puzzle8.Board als Feld.
	 * Gefüllt mit einer Permutation von 0,1,2, ..., 8.
	 * 0 bedeutet leeres Feld.
	 */
	protected int[] board = new int[N+1];

	/**
	 * Generiert ein zufälliges puzzle8.Board.
	 */
	public Board() {
		for (int i = 0; i < N + 1; i++) {
			board[i] = i;
		}
		for (int i = 0; i < board.length; i++) {
			int randIdx = rand.nextInt(board.length);
			int tmp = board[randIdx];
			board[randIdx] = board[i];
			board[i] = tmp;
		}
	}
	
	/**
	 * Generiert ein puzzle8.Board und initialisiert es mit board.
	 * @param board Feld gefüllt mit einer Permutation von 0,1,2, ..., 8.
	 */
	public Board(int[] board) {
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
		// von links nach rechts durchlaufen
		for (int i = 0; i < board.length - 1; i++) {
			int left = board[i];
			if (left == 0) continue;
			// derzeitiger linker Wert mit allen Werten rechts davon vergleichen
			for (int j = i + 1; j < board.length; j++) {
				int right = board[j];
				if (right == 0) continue;
				if (left > right) falsePairs++;
			}
		}
//		System.out.println(falsePairs);
		return falsePairs;
	}
	/**
	 * Paritätsprüfung.
	 * @return Parität.
	 */
	public boolean parity() {
		return getParity() % 2 == 0;
	}
	
	/**
	 * Heurstik h1. (siehe Aufgabenstellung)
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
				if (down < rowColumnSize) boardList.add(doBoardMove(i, i - rowColumnSize));
				if (left >= 0) boardList.add(doBoardMove(i, i - -1));
				if (right < rowColumnSize) boardList.add(doBoardMove(i, i + 1));
			}
		}
		return boardList;
	}

	private Board doBoardMove(int from, int to) {
		int[] newBoard = Arrays.copyOf(board, board.length);
		int tmp = newBoard[from];
		newBoard[from] = newBoard[to];
		newBoard[to] = tmp;
		return new Board(newBoard);
	}
	
	
	/**
	 * Prüft, ob das puzzle8.Board ein Zielzustand ist.
	 * @return true, falls puzzle8.Board Ziestzustand (d.h. 0,1,2,3,4,5,6,7,8)
	 */
	public boolean isSolved() {
		return Objects.equals(board, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8});
	}
	
	
	public static void main(String[] args) {
		Board b = new Board(new int[]{7,2,4,5,0,6,8,3,1});		// abc aus Aufgabenblatt
		Board goal = new Board(new int[]{0,1,2,3,4,5,6,7,8});
				
		System.out.println(b);
		System.out.println(b.parity());
		System.out.println(b.h1());
		System.out.println(b.h2());
		
		for (Board child : b.possibleActions())
			System.out.println(child);
		
		System.out.println(goal.isSolved());
	}
}
	
