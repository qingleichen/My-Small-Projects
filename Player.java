import java.util.ArrayList;
import java.util.Random;


public class Player {
	//1 for red, 2 for blue 
	int role;
	//graph of the player
	Graph mGraph;
	//All cells are stored here, if is colored, dead or captured, remove it
	//used to play random moves in MCTS
	ArrayList<String> emptyCells;
	//0 means occupied, 1 means empty
	int[] emptys;
	PatternEngine pe;
	//ts steps, default is 1000
	private int mctsStep = 1000;
	boolean advanced = false;
	
	public Player(Graph g, int n){
		role = n;
		mGraph = new Graph(g);
		pe = new PatternEngine();
		pe.initialPatterns();
		emptyCells = new ArrayList<String>();
		emptys = new int[g.size*g.size];
		for(int i=0; i<g.size; i++){
			for(int j=0; j<g.size; j++){
				if(g.graph[i][j]==0){
					//emptyCells.add(Integer.toString(i*9+j));
					emptys[i*9+j] = 1;
				}else{
					emptys[i*9+j] = 0;
				}
			}
		}
		//System.out.println("Graph initial empty size "+emptyCells.size());
	}
	
	public void setThinkStep(int n){
		mctsStep = n;
	}
	
	//move a step, do calculation, return the position of next pieces
	public int move(int n){
		//n is the move of the opponent
		//if n=-2, means no pass in, just time to start
		if(n>=-1){
			mGraph.color(n/mGraph.size, n%mGraph.size, 3-role);
			emptyCells.remove(Integer.toString(n));
			//n is not empty
			emptys[n]=0;
		}
		int test = mGraph.winTest(pe.applyPattern(mGraph));
		if(test == role){
			return -1;
		}else if(test==3-role){
			return -2;
		}else{	
			return mcts();
		}
	}
	
	
	
	private int mcts(){
		int[] eval1 = new int[mGraph.size*mGraph.size];	
		eval1[0] = 0;
		eval1[80] = 0;
		int[] eval2 = new int[mGraph.size*mGraph.size];
		eval2[0] = 0;
		eval2[80] = 0;
		float count = 0;
		for(int i=0; i<mctsStep; i++){			
			int res = trial(eval1, eval2, new Graph(mGraph), emptys, 0);
			if(res==role) count++;
		}
		
		float rate = count/mctsStep;
		
		System.out.println("MCTS winning rate " + rate);
		int[] eval = new int[81];
		int w1=1, w2=1;
		int adjust = 0;
		if (rate > 0.5) {
			adjust = (int) ((rate - 0.5) / 0.02);
			if (role == 1)
				w1 += adjust;
			else
				w2 += adjust;
		} else {
			adjust = (int) ((0.5 - rate) / 0.03);
			if (role == 2)
				w1 += adjust;
			else
				w2 += adjust;
		}

		//System.out.printf("w1 %d  w2  %d\n", w1, w2);
		
		for (int i = 0; i < eval.length; i++) {
			eval[i] += (w1*eval1[i] + w2*eval2[i])/10;
			if(mGraph.count<4 && !notEdge(i)){
				eval[i] /=2;
			}
		}
		
		
		//showmatrix(eval);
		
		int max = 0, pos=0;
		for(int i=0; i<mGraph.size*mGraph.size; i++){
			if(eval[i]>max || (eval[i]==max && notEdge(i) )){
				max = eval[i];
				pos = i;
			}
		}
		if(rate<0.2) return -2;
		
		//Advanced search
		//System.out.println(mGraph.count);
		if (advanced && mGraph.count>1) {
			System.out.printf("Current MCTS result is %d %d of eval %d\n", pos/9, pos%9, max);
			System.out.println("Now doing advanced search");
			int temp = advancedSearch(max, eval);
			if (temp != -1 && eval[temp] > max / 10) {
				pos = temp;
				System.out.printf(
						"Advanced search get result %d %d of eval %d\n",
						pos / 9, pos % 9, eval[pos]);
			}
		}
		mGraph.color(pos/9, pos%9, role);
		emptys[pos] = 0;
		return pos;
	}
	
	
	//According to the evaluation array, do advanced search
	private int advancedSearch(int max, int[] eval){
		//showmatrix(eval);
		Graph g = new Graph(mGraph);
		//g.display();
		int[] empt = new int[g.size*g.size];
		for(int i=0; i<empt.length; i++){
			empt[i] = emptys[i];
		}
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				if(eval[i*9+j]<max/2 && g.graph[i][j]==0){
					empt[i*9+j] = 0;		
				}
			}
		}
		//showmatrix(empt);;
		int[] neweval1 = new int[eval.length]; 
		int[] neweval2 = new int[eval.length]; 
		int[] res = new int[3];
		for(int i = 0; i< mctsStep/2 ; i++){
			int temp = trial(neweval1, neweval2, new Graph(g), empt, 0);
			res[temp]++;
		}
		//showmatrix(neweval);
		int[] neweval = new int[81];
		int w1, w2;
		float r = res[1]/res[1]+res[2];
		if(r>0.5){
			w1 = 1 +(int)((r-0.5)/0.03);
			w2 = 1;
		}else{
			w2 = 1 +(int)((0.5-r)/0.03);
			w1 = 1;
		}
		for(int i=0; i<81; i++){
			neweval[i] += w1* neweval1[i] + w2*neweval2[i];
		}
		
		int newmax = 0, pos=0;
		for(int i=0; i<mGraph.size*mGraph.size; i++){
			if(neweval[i]>newmax || (neweval[i]==newmax && notEdge(i))){
				newmax = neweval[i];
				pos = i;
			}
		}
		if(newmax < mctsStep/10) return -1;
		return pos;
	}
	
	
	private int trial(int[] eval1, int[] eval2, Graph g, int[] emp, int prefer){
		ArrayList<Integer> reds = new ArrayList<Integer>();
		ArrayList<Integer> blues = new ArrayList<Integer>();
		int[] trialEmpty = new int[g.size*g.size];
		for(int i=0; i<trialEmpty.length; i++){
			trialEmpty[i] = emp[i];
		}
		//showEmpty(trialEmpty);
		
		Random rand = new Random();
		int cur  = role;
		while(true){
			ArrayList<int[]> nodes = pe.applyPattern(g);
			int test = g.winTest(nodes);
			if(test==0){

				if (nodes != null) {
					for (int[] node : nodes) {
						trialEmpty[node[0] * 9 + node[1]] = 0;
					}
				}

				int size = 0;
				for(int n: trialEmpty){
					size += n;
				}
				if(size==0){
					//System.out.println("No cell to play");
					return 0;
				}
				int pos = rand.nextInt(size);

				size =-1;
				for(int i=0;i<trialEmpty.length; i++){
					size +=trialEmpty[i];
					if(size == pos){
						pos = i;
						break;
					}
				}
				trialEmpty[pos] = 0;
				if (nodes != null) {
					for (int[] node : nodes) {
						trialEmpty[node[0] * 9 + node[1]] = 1;
					}
				}
				
			    g.color(pos/g.size, pos%g.size, cur);
			    
			    if(cur==1) reds.add(pos);
			    if(cur==2) blues.add(pos);
			    cur = 3 - cur;
			}else{

				if (prefer == 0) {
					if (test == 1) {
						int l = reds.size();
						for (int n : reds) {
							eval1[n] = eval1[n] + 40 - l;
						}
						return 1;
					}else if (test == 2) {
						int l = blues.size();
						for (int n : blues) {
							eval2[n] = eval2[n] + 40 - l;
						}
						return 2;
					}
				} else {

					if (test == 1 && role == 1) {
						for (int n : reds) {
							eval1[n]++;
						}
					}
					if (test == 2 && role == 2) {
						//System.out.println("Blue win");
						for (int n : blues) {
							eval2[n]++;
						}
					}
					return 0;
				}
				break;
				
			}
		}
		return 0;
	}
	
	public void showEmpty(){
		System.out.print("Empty cells: ");
		for(int n: emptys){
			System.out.printf("%2d;", n);
		}
		System.out.println();
	}
	
	public void showmatrix(int[] num){
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				if(mGraph.graph[i][j]==0){
				    System.out.printf("%5d;", num[i*9+j]);
				}else{
					if(mGraph.graph[i][j]==1){
						System.out.print("  R;");
					}else{
						System.out.print("  B;");
					}
				}
			}
			System.out.println();
		}
	}
	
	private boolean notEdge(int i){
		int x = i/9, y = i%9;
		if(x<2 || x>6 || y<2 || y>6) return false;
		return true;
	}
}
