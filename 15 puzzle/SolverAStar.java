import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolverAStar {
	int myh;
	PatternDb mydb;
	long runningTime;
	int searchedState;

	public SolverAStar() {
		myh = 0;
		mydb = null;
		runningTime = 0;
		searchedState = 0;
	}

	public SolverAStar(PatternDb db) {
		myh = 1;
		mydb = db;
		runningTime = 0;
		searchedState = 0;
	}

	public State solve(State problem) {

		long startTime = System.currentTimeMillis();

		Map<Integer, ArrayList<State>> frontier = new HashMap<Integer, ArrayList<State>>();
		List<String> searched = new ArrayList<String>();
		State start = problem;

		// get initial state, and put it into the hashmap according to f = g +
		// h;
		ArrayList<State> l = new ArrayList<State>();
		l.add(start);
		frontier.put(hOfS(start), l);
		// System.out.println("start state f: "+hOfS(start));

		while (true) {
			// choose one from frontier with smallest f to expand
			if (frontier.size() == 0) {
				System.out.println("No solution!!!");
				break;
			}
			int min = 500;
			for (Integer i : frontier.keySet()) {
				if (i < min) {
					min = i;
				}
			}
			// System.out.println("current min f: "+min);
			State cur = frontier.get(min).remove(0);

			if (min == cur.g * (myh + 1)) {
				// System.out.println("A* final searched # of states: "+searched.size());
				searchedState = searched.size();
				long endTime = System.currentTimeMillis();
				runningTime = endTime - startTime;
				return cur;
			}

			if (frontier.get(min).size() == 0) {
				frontier.remove(min);
			}

			// store the state chosen to expand into searched
			searched.add(cur.getState());

			// generate its children into frontiers
			for (int i = 1; i < 5; i++) {
				State child = cur.generateChild(i);
				if (child != null) {
					// System.out.println("Get a child in dir "+i);
					// child.display();
					String temp = child.getState();
					if (!searched.contains(temp)) {
						// System.out.println("child is not searched");
						int f = hOfS(child) + child.g * (myh + 1);
						// System.out.println("Child h: "+hOfS(child)+" g: "+child.g);
						if (!frontier.containsKey(f)) {
							frontier.put(f, new ArrayList<State>());
						}
						frontier.get(f).add(child);
					}
				}
			}

		}
		return null;
	}

	// h1
	public int hOfS(State s) {
		if (myh == 0) {
			int d = 0;
			for (int i = 0; i < 16; i++) {
				int temp = s.getPuzzle()[i];
				if (temp == 0) {
					continue;
				}
				d = d + Math.abs((int) (i / 4) - (int) (temp / 4))
						+ Math.abs(i % 4 - temp % 4);
			}
			return d;
		} else {
			return mydb.getHeu(s.getPuzzle());
		}
	}
}
