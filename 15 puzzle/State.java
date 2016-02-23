import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class State {
	int[] puzzle;
	List<Integer> steps;
	int pos;
	int g;

	State() {
		puzzle = new int[16];
		for (int i = 0; i < 16; i++) {
			puzzle[i] = i;
		}
		steps = new ArrayList<Integer>();
		pos = 0;
		g = 0;
	}

	public State(int[] n) {
		puzzle = n;
		for (int i = 0; i < n.length; i++) {
			if (n[i] == 0) {
				pos = i;
				break;
			}
		}
		steps = new ArrayList<Integer>();
		g = 0;
	}

	State(State s) {
		puzzle = s.puzzle.clone();
		steps = new ArrayList<Integer>(s.steps);
		pos = s.pos;
		g = s.g;
	}

	// convert state to a string for easier store
	// store state as a matrix is too expensive
	String getState() {
		String str = "";
		for (int i = 0; i < puzzle.length; i++) {
			str = str + Integer.toString(puzzle[i]);
		}
		return str;
	}

	// move the blank, given a direction 1-up 2-down 3-left 4-right
	public boolean isValidDir(int dir) {
		// get the blank position
		int x = pos / 4;
		int y = pos - 4 * x;
		// check direction, and get the position of the square that will move
		// and check whether this move is valid
		switch (dir) {
		case 1:
			if (x < 1)
				return false;
			break;
		case 2:
			// here restrict blank to go below the second row
			if (x > 0)
				return false;
			break;
		case 3:
			if (y < 1)
				return false;
			break;
		case 4:
			if (y > 2)
				return false;
			break;
		default:
			return false;
		}
		return true;
	}

	// move blank given a direction 1-up 2-down 3-left 4-right
	public boolean move(int dir) {
		// check direction, and get the position of the square that will move
		// and check whether this move is valid
		g++;
		switch (dir) {
		case 1:
			puzzle[pos] = puzzle[pos - 4];
			pos = pos - 4;
			puzzle[pos] = 0;
			break;
		case 2:
			puzzle[pos] = puzzle[pos + 4];
			pos = pos + 4;
			puzzle[pos] = 0;
			break;
		case 3:
			puzzle[pos] = puzzle[pos - 1];
			pos = pos - 1;
			puzzle[pos] = 0;
			break;
		case 4:
			puzzle[pos] = puzzle[pos + 1];
			pos = pos + 1;
			puzzle[pos] = 0;
			break;
		default:
			g--;
			return false;
		}
		steps.add(dir);
		return true;
	}

	// cautious, this can be used to generate a random correct state or do
	// annealing
	public void randomWalk(int steps) {
		Random r = new Random();
		for (int i = 0; i < r.nextInt(steps); i++) {
			int dir = r.nextInt(4) + 1;
			if (isValidDir(dir)) {
				move(dir);
			}
		}
		this.g = 0;
		this.steps.clear();
	}

	public State generateChild(int dir) {
		if (!isValidDir(dir)) {
			return null;
		}
		if (!steps.isEmpty()) {
			int s = steps.get(steps.size() - 1);
			switch (dir) {
			case 1:
				if (s == 2)
					return null;
				break;
			case 2:
				if (s == 1)
					return null;
				break;
			case 3:
				if (s == 4)
					return null;
				break;
			case 4:
				if (s == 3)
					return null;
				break;
			default:
				break;
			}
		}
		State child = new State(this);
		// System.out.println("g compare parent "+g+" child "+child.g);
		child.move(dir);
		// System.out.println("g compare after move parent "+g+" child "+child.g);
		return child;
	}

	public void display() {
		for (int i = 0; i < puzzle.length; i++) {
			System.out.printf("%4d,", puzzle[i]);
			if (i % 4 == 3) {
				System.out.println();
			}
		}
		System.out.println();
	}

	public int[] getPuzzle() {
		return puzzle;
	}

	public void showSolution() {
		if (steps.size() == 0) {
			System.out.println("This is a state without solution");
		}
		for (int i : steps) {
			System.out.printf("%2d,", i);
		}
		System.out.println();
	}

	public State clone() {
		return new State(this);
	}
}
