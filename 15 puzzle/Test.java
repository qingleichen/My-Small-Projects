import java.util.ArrayList;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PatternDb db = new PatternDb();
		db.generateDb(0);

		PatternDb db2 = new PatternDb();
		db2.generateDb(1);
		/*
		 * State s = new State(); s.display(); //display the pattern db
		 * heuristic
		 * System.out.println("heu of origin state "+db.getHeu(s.getPuzzle()));
		 * 
		 * //try random walk to generate test cases s.randomWalk(200);
		 * s.display(); //display the pattern db heuristic
		 * System.out.println("heu of random state "+db.getHeu(s.getPuzzle()));
		 */

		ArrayList<String> cases = db.cases;

		/*
		 * //this part is used to test different optimal solution find by
		 * different algorithm State s = new State(toArray(cases.get(22)));
		 * 
		 * SolverAStar solver1 = new SolverAStar(db); State solution1 =
		 * solver1.solve(s.clone()); solution1.showSolution();
		 * 
		 * State s1 = s.clone(); for(int i: solution1.steps){ s1.move(i);
		 * s1.display(); System.out.println(); }
		 * 
		 * SolverIDAStar solver2 = new SolverIDAStar(db); State solution2 =
		 * solver2.solve(s.clone()); solution2.showSolution(); State s2 =
		 * s.clone(); for(int i: solution2.steps){ s2.move(i); s2.display();
		 * System.out.println(); }
		 */

		// a complete test with results
		long[] times = new long[210];
		int[] counts = new int[210];
		int count = 0;

		for (String temp : cases) {
			State ex = new State(toArray(temp));
			System.out.println("Example problem:");
			ex.display();

			SolverAStar solver = new SolverAStar();
			State solution = solver.solve(ex.clone());
			System.out.println("Result of A* with Manhattan Distance");
			solution.showSolution();
			times[count] = solver.runningTime;
			counts[count] = solver.searchedState;
			count++;

			SolverAStar solver2 = new SolverAStar(db);
			State solution2 = solver2.solve(ex.clone());
			System.out.println("Result of A* with Pattern DataBase 1");
			solution2.showSolution();
			times[count] = solver2.runningTime;
			counts[count] = solver2.searchedState;
			count++;

			SolverAStar solver22 = new SolverAStar(db2);
			State solution22 = solver22.solve(ex.clone());
			System.out.println("Result of A* with Pattern DataBase 2");
			solution22.showSolution();
			times[count] = solver22.runningTime;
			counts[count] = solver22.searchedState;
			count++;

			SolverIDAStar solver3 = new SolverIDAStar();
			State solution3 = solver3.solve(ex.clone());
			System.out.println("Result of IDA* with Manhattan Distance");
			solution3.showSolution();
			times[count] = solver3.runningTime;
			counts[count] = solver3.generatedStates;
			count++;

			SolverIDAStar solver4 = new SolverIDAStar(db);
			State solution4 = solver4.solve(ex.clone());
			System.out.println("Result of IDA* with Pattern DataBase 1");
			solution4.showSolution();
			times[count] = solver4.runningTime;
			counts[count] = solver4.generatedStates;
			count++;

			SolverIDAStar solver44 = new SolverIDAStar(db2);
			State solution44 = solver44.solve(ex.clone());
			System.out.println("Result of IDA* with Pattern DataBase 2");
			solution44.showSolution();
			times[count] = solver44.runningTime;
			counts[count] = solver44.generatedStates;
			count++;
		}
		System.out.printf("\n\n\n");
		System.out
				.println("Example No.              A*               A*P1              A*P2              IDA*             IDA*P1           IDA*P2");
		for (int i = 0; i < 7; i++) {
			System.out.println("Solution: " + (i + 1) * 5);
			for (int j = 0; j < 5; j++) {
				System.out.printf("Example %d: ", j);
				for (int k = 0; k < 6; k++) {
					System.out.printf("%6d (%8d) |", times[i * 30 + j * 6 + k],
							counts[i * 30 + j * 6 + k]);
				}
				System.out.println();
			}
		}

		// old version of initial test
		// int[] n = {7, 0, 4, 2, 1, 3, 5, 6, 8, 9, 10, 11, 12, 13, 14, 15};
		// int[] n = {5, 6, 4, 7, 3, 0, 2, 1, 8, 9,10,11,12,13,14,15};
		/*
		 * int[] n = {3, 4, 5, 7, 2, 0, 6, 1, 8, 9,10,11,12,13,14,15}; State s1
		 * = new State(n); State s2 = new State(s1); State s3 = new State(s1);
		 * State s4 = new State(s1); s1.display();
		 * System.out.println("heu of s1 "+db.getHeu(s1.getPuzzle()));
		 * 
		 * //directly get the solution from db System.out.println(
		 * "*********According to a complete BFS search of first two row, the solution need "
		 * + db.getSolution(n)+" steps.");
		 * 
		 * for(String temp: db.db3.keySet()){ System.out.println(temp); for(int
		 * i:toArray(temp)){ System.out.printf("%2d,", i); }
		 * System.out.println();
		 * System.out.println("Solution is "+db.getSolution(toArray(temp)));
		 * 
		 * break; }
		 * 
		 * SolverAStar solver = new SolverAStar(); State solution =
		 * solver.solve(s1); solution.showSolution();
		 * 
		 * SolverAStar solver2 = new SolverAStar(db); State solution2 =
		 * solver2.solve(s2); solution2.showSolution();
		 * 
		 * SolverIDAStar solver3 = new SolverIDAStar();
		 * //solver3.searchWithBound(s3, 24); State solution3 =
		 * solver3.solve(s3); solution3.showSolution();
		 * 
		 * SolverIDAStar solver4 = new SolverIDAStar(db);
		 * //solver3.searchWithBound(s3, 24); State solution4 =
		 * solver4.solve(s4); solution4.showSolution();
		 */

		// s1.display();
		/*
		 * for(int i: solution.steps){ s1.move(i); s1.display();
		 * System.out.println(); }
		 */
	}

	static int[] toArray(String s) {
		int num = 0;
		int[] res = new int[16];
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != ',') {
				num = num * 10 + s.charAt(i) - '0';
			} else {
				res[count] = num;
				num = 0;
				count++;
			}
		}
		return res;
	}

}
