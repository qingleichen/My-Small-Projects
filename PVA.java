import java.util.Scanner;


public class PVA {
	public void pva(int role, int lvl, int start, int adv){
		Graph g = new Graph(9);
		if(start>10){
			g.midgame(start);
		}else{
			g.randomInit(start);
		}
		g.displayWithAsix();
		Player AI = new Player(g, 3-role);
		AI.setThinkStep(lvl);
		if(adv==1) AI.advanced = true;
		int round = 1;
		int move = -2;
		Scanner sca = new Scanner(System.in);
		int[] played = new int[81];
		for(;;){
			if(round == role){
				while (true) {
					move = getHumanInput(sca);
					if (move < 0 || move > 80) {
						System.out.println("Player give up, AI win");
						return;
					}
					if (played[move] == 1) {
						System.out
								.println("This cell already has a piece, please re-try:");
					} else {
						played[move] = 1;
						break;
					}
				}
				g.color(move/9, move%9, role);
				g.displayWithAsix();
				round = 3-round;
			} else {
				System.out.println("AI round:");
				move = AI.move(move);
				if (move == -1) {
					System.out.println("AI claim win!");
					break;
				} else if (move == -2) {
					System.out.println("AI give up, you win!");
					break;
				} else {
					if(played[move]==1){
						System.out.println("AI is confused! NEED REST!!");
						return;
					}
					played[move] = 1;
					g.color(move/9, move%9, 3-role);
					System.out.printf("Play %2d %2d\n", move/9, move%9);
					g.displayWithAsix();
					round = 3-round;
					
				}
			}
		}
	}
	
	public int getHumanInput(Scanner sca){
		System.out.printf("Play round: x = ");
		int x = sca.nextInt();
		System.out.printf("            y = ");
		int y = sca.nextInt();
		return x*9+y;
	}
}
