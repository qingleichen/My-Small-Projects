import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatternDb {
	// two map to store pattern dp
	// pattern 1, include 0, 1, 2, 4, 5, 6, 8, 9, 10
	// pattern 1, v2: 0, 1, 4, 5, 8, 9, 12, 13
	// pattern 1, v3: 0, 1, 2, 3, 8, 9, 10, 11
	Map<String, Integer> db1;
	int[][] p1 = { { 0, 1, 2, 4, 5, 6, 8, 9, 10 }, { 0, 1, 4, 5, 8, 9, 12, 13 } };
	// pattern 2, include 0, 3, 7, 11, 12, 13, 14, 15
	// pattern 2, v2: 0, 2, 3, 6, 7, 10, 11, 14, 15
	// pattern 2, v3: 0, 4, 5, 6, 7, 12, 13, 14, 15
	Map<String, Integer> db2;
	int[][] p2 = { { 0, 3, 7, 11, 12, 13, 14, 15 },
			{ 0, 2, 3, 6, 7, 10, 11, 14, 15 } };

	int dbNum;

	// a map to store all possible states for first two row, with optimal
	// solution step #
	Map<String, Integer> db3;

	ArrayList<String> cases;

	int[] state;
	boolean gen = false;

	public PatternDb() {
		state = new int[16];
		db1 = new HashMap<String, Integer>();
		db2 = new HashMap<String, Integer>();
		db3 = new HashMap<String, Integer>();
		cases = new ArrayList<String>();
		dbNum = 0;
	}

	public int getHeu(int[] state) {
		if (gen == false) {
			System.out.println("Please generate the pattern database first.");
			return -1;
		}
		int res = 0;
		res = res + db1.get(getP1(getPos(state)));
		// System.out.println("res 1 "+res);
		String s = getP2(getPos(state));
		// System.out.println(s);
		res = res + db2.get(s);
		// res = res + db2.get(getP2(getPos(state)));
		// System.out.println("res 2 "+res);
		return res;
	}

	public int getSolution(int[] state) {
		if (gen == false) {
			System.out.println("Please generate the pattern database first.");
			return -1;
		}
		String s = getP(state);
		if (db3.containsKey(s)) {
			return db3.get(s);
		} else {
			System.out.println("This mutation has not solution");
			return -2;
		}
	}

	public void generateDb(int num) {
		// initialize to target
		dbNum = num;
		for (int i = 0; i < state.length; i++) {
			state[i] = i;
		}

		// test: output the current pattern
		// System.out.println(getP1(getPos(state)));
		// System.out.println(getP2(getPos(state)));

		// this.move(state, 2);
		// System.out.println(getP1(getPos(state)));
		// System.out.println(getP2(getPos(state)));

		List<int[]> frontier = new ArrayList<int[]>();
		frontier.add(state);
		db1.put(getP1(getPos(state)), 0);
		db2.put(getP2(getPos(state)), 0);
		db3.put(getP(state), 0);

		int step = 0;
		while (true) {
			step++;
			int l = frontier.size();

			// store typical cases
			int casenum;
			if (step % 5 == 0) {
				casenum = 0;
			} else {
				casenum = 5;
			}

			// System.out.println("step " + step + " frontier " + l);
			if (l == 0) {
				System.out.println("Max step " + (step - 1));
				break;
			}
			for (int i = 0; i < l; i++) {
				int[] cur = frontier.remove(0);
				for (int j = 1; j < 5; j++) {
					if (validDir(cur, j)) {
						int[] newcur = cur.clone();
						move(newcur, j);

						int[] temp = getPos(newcur);
						// int repeat = 0;
						if (!db1.containsKey(getP1(temp))) {
							db1.put(getP1(temp), step);
						} else {
							// repeat++;
						}
						if (!db2.containsKey(getP2(temp))) {
							db2.put(getP2(temp), step);
						} else {
							// repeat++;
						}
						if (!db3.containsKey(getP(newcur))) {
							db3.put(getP(newcur), step);
							if (casenum < 5) {
								cases.add(getP(newcur));
								casenum++;
							}
							frontier.add(newcur);
						}
						/*
						 * if (repeat != 2) { frontier.add(newcur);
						 * //db3.put(getP(newcur), step); }
						 */
					}
				}
			}
		}
		System.out.println("db1 size " + db1.size());
		System.out.println("db2 size " + db2.size());
		System.out.println("db3 size " + db3.size());
		gen = true;

	}

	// dir: direction to move the blank square to
	// 1 up, 2 down, 3 left, 4 right
	public boolean validDir(int[] state, int dir) {
		int pos = getBlank(state);
		int x = pos / 4;
		int y = pos - x * 4;
		switch (dir) {
		case 1:
			if (x == 0) {
				return false;
			}
			break;
		case 2:
			if (x == 3) {
				return false;
			}
			// add adjustment, stop the bottom two rows
			if (x == 1) {
				return false;
			}
			break;
		case 3:
			if (y == 0) {
				return false;
			}
			break;
		case 4:
			if (y == 3) {
				return false;
			}
			break;
		default:
			return false;
		}
		return true;
	}

	public void move(int[] state, int dir) {
		int pos = getBlank(state);
		switch (dir) {
		case 1:
			state[pos] = state[pos - 4];
			pos = pos - 4;
			state[pos] = 0;
			break;
		case 2:
			state[pos] = state[pos + 4];
			pos = pos + 4;
			state[pos] = 0;
			break;
		case 3:
			state[pos] = state[pos - 1];
			pos = pos - 1;
			state[pos] = 0;
			break;
		case 4:
			state[pos] = state[pos + 1];
			pos = pos + 1;
			state[pos] = 0;
			break;
		default:
			break;
		}
	}

	public String getP1(int[] pos) {
		String s = "";
		for (int i : p1[dbNum]) {
			s = s + Integer.toString(pos[i]);
		}
		return s;
	}

	public String getP2(int[] pos) {
		String s = "";
		for (int i : p2[dbNum]) {
			s = s + Integer.toString(pos[i]);
		}
		return s;
	}

	public String getP(int[] n) {
		String s = "";
		for (int i : n) {
			s = s + Integer.toString(i) + ",";
		}
		return s;
	}

	public int getBlank(int[] state) {
		for (int i = 0; i < state.length; i++) {
			if (state[i] == 0) {
				return i;
			}
		}
		return 0;
	}

	public int[] getPos(int[] state) {
		int[] pos = new int[state.length];
		for (int i = 0; i < state.length; i++) {
			pos[state[i]] = i;
		}
		return pos;
	}
}
