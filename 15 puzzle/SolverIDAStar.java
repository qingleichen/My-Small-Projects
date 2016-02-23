public class SolverIDAStar {
	int myh;
	PatternDb mydb;
	State solution;
	int countOfGenerateState;
	long runningTime;
	int generatedStates;

	public SolverIDAStar() {
		myh = 0;
		mydb = null;
		solution = null;
		countOfGenerateState = 0;
		runningTime = 0;
		generatedStates = 0;
	}

	public SolverIDAStar(PatternDb db) {
		myh = 1;
		mydb = db;
		solution = null;
		countOfGenerateState = 0;
		runningTime = 0;
		generatedStates = 0;
	}

	public State solve(State s) {
		long startTime = System.currentTimeMillis();
		int bound = hOfS(s);
		countOfGenerateState++;
		// System.out.println("***************Current bound is "+bound);
		while (!searchWithBound(s, bound)) {
			bound++;
			// System.out.println("***************Current bound is "+bound);
		}
		// System.out.println("IDA*, state generated "+countOfGenerateState);
		generatedStates = countOfGenerateState;
		long endTime = System.currentTimeMillis();
		runningTime = endTime - startTime;
		return solution;
	}

	public boolean searchWithBound(State root, int bound) {
		// do a DFS with f upper bound
		// root.display();

		int h = hOfS(root);
		if (root.g * (myh + 1) + h > bound) {
			return false;
		} else if (h == 0) {
			solution = root;
			return true;
		} else {
			for (int i = 1; i < 5; i++) {
				State child = root.generateChild(i);
				if (child != null) {
					countOfGenerateState++;
					if (searchWithBound(child, bound)) {
						return true;
					}
				}
			}
			return false;

		}
	}

	public int hOfS(State s) {
		if (myh == 0) {
			int d = 0;
			for (int i = 0; i < 16; i++) {
				int temp = s.getPuzzle()[i];
				if (temp == 0)
					continue;
				d = d + Math.abs((int) (i / 4) - (int) (temp / 4))
						+ Math.abs(i % 4 - temp % 4);
			}
			return d;
		} else {
			return mydb.getHeu(s.getPuzzle());
		}
	}
}
