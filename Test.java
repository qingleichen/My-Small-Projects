import java.util.ArrayList;
import java.util.Scanner;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//oldTest();
		
		/*
		Graph g = new Graph(9);
		Player p = new Player(g, 1); p.setThinkStep(1);
		p.showEmpty();
		p.move(-2);
		*/
		
		
		/*
		//AI vs AI test
		int times =1;
		int start = 11;
		int lvl1 = 200;
		int lvl2 = 200;	
		boolean adv1 = false, adv2 = false;
		ava(times, start, lvl1, lvl2, adv1, adv2);
		*/
		
		
		//Graph g = new Graph(9);
		//g.displayWithAsix();
		
		
		
		
		//person vs ai play
		Scanner scan = new Scanner(System.in);
		System.out.printf("Please select a role, 1 for red, 2 for blue : ");
		int role = scan.nextInt();
		System.out.printf("Please select AI Level, 100~2000 ");
		int lvl = scan.nextInt();
		System.out.printf("Please select start cell number: 0~10 ");
		int start = scan.nextInt();
		System.out.printf("Do you need advanced AI: 1 true, 0 false ");
		int adv = scan.nextInt();
		PVA newgame = new PVA();
		newgame.pva(role, lvl, start, adv);
		scan.close();
		
		
		
		
		
		
	}
	
	public static void ava(int times, int start, int lvl1, int lvl2, boolean adv1, boolean adv2){
		int red=0, blue=0;
		for(int i=0; i<times; i++){
			System.out.printf("******Game %2d*******\n", i);
			if(run(start, lvl1, lvl2, adv1, adv2)){
				red++;
				//System.out.printf("Winner Red\n");
			}else{
				blue++;
				//System.out.printf("Winner Blue\n");
			}
		}
		float r = (float)red/(float)times, b = (float)blue/(float)times;
		System.out.println("Game Test Result: total test times "+times);
		System.out.println("|   Color   |   Level   |     Win   |  Win Rate |");
		System.out.printf( "|   Red     |    %4d   |     %3d   |    %.3f  |\n", lvl1, red, r );
		System.out.printf( "|   Blue    |    %4d   |     %3d   |    %.3f  |\n", lvl2, blue, b );
	}
	
	
	
	public static void oldTest(){
		Graph g = new Graph(9);
		// g.testInit();
		// g.randomInit(0);
		// g.deadTest();
		// g.captureTest();
		// g.InferiorTest();
		g.winStateWithPatternTest();
		g.display();
		System.out.println(g.winTest(null));
		PatternEngine pe = new PatternEngine();
		pe.initialPatterns();	
		g.displayWithPattern(pe.applyPattern(g));
		System.out.println(g.winTest(1,pe.applyPattern(g)));
		/*
		Player red = new Player(g, 1);
		red.setThinkStep(200);
		Player blue = new Player(g, 2);
		blue.setThinkStep(200);	
		int cur = 1, res = -2;
		// red.move(res);
		for (;;) {
			if (cur == 1) {
				res = red.move(res);
				if (res == -1) {
					System.out.println("Red  Win!!!!!!!!");
					red.mGraph.display();
					break;
				} else {
					g.color(res / g.size, res % g.size, cur);
					System.out.printf("Red  play %d %d\n", res / g.size, res
							% g.size);
					g.display();
					cur = 3 - cur;
				}

			} else {
				res = blue.move(res);
				if (res == -1) {
					System.out.println("Blue Win!!!!!!!!");
					blue.mGraph.display();
					break;
				} else {
					g.color(res / g.size, res % g.size, cur);
					System.out.printf("Blue play %d %d\n", res / g.size, res
							% g.size);
					g.display();
					cur = 3 - cur;
				}
			}
		}
		*/
	}

	public static boolean run(int start, int lvl1, int lvl2, boolean adv1, boolean adv2) {
		Graph g = new Graph(9);
		g.midgame(start);
		//g.display();
		// System.out.println(g.winTest(null));
		Player red = new Player(g, 1);
		red.setThinkStep(lvl1);
		red.advanced = adv1;
		Player blue = new Player(g, 2);
		blue.setThinkStep(lvl2);
		blue.advanced = adv2;
		PatternEngine pe = new PatternEngine();
		pe.initialPatterns();
		int cur = 1, res = -2;
		ArrayList<Integer> played= new ArrayList<Integer>();
		for (;;) {
			if (cur == 1) {
				res = red.move(res);
				if(played.contains(res)){
					System.out.println("Repeated Moves!!!!!!!!"+res);
					return false;
				}else{
					played.add(res);
				}
				if (res == -1) {
					System.out.println("Red Win!!!!!!!!");
					g.display();
					return true;
				}else if(res == -2){
					System.out.println("BLue Win!!!!!!!!");
					g.display();
					return false;
				}else {
					g.color(res / g.size, res % g.size, cur);
					//System.out.println("Red play "+res);
					System.out.printf("Red  play %d %d\n", res / g.size, res
							% g.size);
					g.display();
					cur = 3 - cur;
				}

			} else {
				res = blue.move(res);
				if(played.contains(res)){
					System.out.println("Repeated Moves!!!!!!!! "+res);
					return false;
				}else{
					played.add(res);
				}
				if (res == -1) {
					System.out.println("Blue Win!!!!!!!!");
					g.display();
					return false;
				}else if(res == -2) {
					System.out.println("Red Win!!!!!!!!");
					g.display();
					return true;
				}else {
					g.color(res / g.size, res % g.size, cur);
					//System.out.println("Blue play "+res);
					System.out.printf("Blue play %d %d\n", res / g.size, res
						  	% g.size);
					g.display();
					cur = 3 - cur;
				}
			}
		}
	}
}
