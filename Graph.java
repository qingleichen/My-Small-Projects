import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Graph {
	int[][] graph;
	int size;
	int[] blues;
	int[] reds;
	int count;
	
	public Graph(int n){
		graph = new int[n][n];
		blues = new int[n];
		reds = new int[n];
		size = n;	
		count = 0;
	}
	
	public Graph(Graph g){
		size = g.size;
		graph = new int[size][size];
		blues = new int[size];
		reds = new int[size];
		for(int i=0; i<size; i++){
			for(int j=0; j<size; j++){
				graph[i][j] = g.graph[i][j];
			}
			blues[i] = g.blues[i];
			reds[i] = g.reds[i];
		}
		count = g.count;
	}
	
	public void midgame(int n){
		color(1, 1, 1);
		color(7, 7, 2);
		color(1, 7, 2);
		color(7, 1, 1);
	}
	
	public void winStateWithPatternTest(){
		color(0, 0, 1);
		color(1, 2, 1);
		color(2, 4, 1);
		color(3, 6, 1);
		color(4, 8, 1);
		color(5, 0, 2);
		color(5, 1, 2);
		color(5, 2, 2);
		color(7, 0, 2);
		color(7, 1, 2);
	}
	
	//in this graph, 1 for black 2 for white
	public void testInit(){
		for(int i=0; i<size; i++){
			color(i, 3, 2);
			color(5, i, 1);
			//graph[i][3] = 2;
			//graph[i][5] = 1;
		}
	}
	
	public void deadTest(){
		//graph[0][0] = 1;
		//graph[1][0] = 1;
		//graph[2][1] = 1;
		//graph[1][2] = 2;
		color(0, 0, 1);
		color(1, 0, 1);
		color(2, 1, 1);
		color(1, 2, 2);
		//graph[2][3] = 2;
		//graph[2][4] = 2;
		//graph[1][4] = 2;
		//graph[3][5] = 2;
		//graph[4][5] = 2;
		//graph[5][2] = 1;
		//graph[5][3] = 1;
		//graph[3][2] = 2;
		//graph[3][1] = 2;
		color(2, 3, 2);
		color(2, 4, 2);
		color(1, 4, 2);
		color(3, 5, 2);
		color(4, 5, 2);
		color(5, 2, 1);
		color(5, 3, 1);
		color(3, 2, 2);
		color(3, 1, 2);
	}
	
	public void captureTest(){
		color(0, 1, 1); //graph[0][1] = 1;
		color(1, 2, 1); //graph[1][2] = 1;
		color(2, 3, 1); //graph[2][3] = 1;
		color(2, 1, 1); //graph[2][1] = 1;
		color(2, 0, 1); //graph[2][0] = 1;
		color(3, 0, 1); //graph[3][0] = 1;
		color(4, 0, 1); //graph[4][0] = 1;
		color(5, 1, 1); //graph[5][1] = 1;
		color(5, 5, 2); //graph[5][5] = 2;
		color(6, 6, 2); //graph[6][6] = 2;
		color(5, 7, 2); //graph[5][7] = 2;
		color(6, 8, 2); //graph[6][8] = 2;
	}
	
	public void InferiorTest(){
		color(2, 0, 1); //graph[2][0] = 1;
		color(2, 1, 1); //graph[2][1] = 1;
		color(2, 2, 1); //graph[2][2] = 1;
		color(4, 0, 1); //graph[4][0] = 1;
		color(0, 4, 2); //graph[0][4] = 2;
		color(1, 4, 2); //graph[1][4] = 2;
		color(2, 5, 2); //graph[2][5] = 2;
		color(6, 0, 2); //graph[6][0] = 2;
		color(6, 1, 2); //graph[6][1] = 2;
		color(7, 2, 2); //graph[7][2] = 2;
		color(7, 4, 1); //graph[7][4] = 1;
		color(7, 5, 1); //graph[7][5] = 1;
		color(5, 4, 1); //graph[5][4] = 1;
		color(4, 6, 1); //graph[4][6] = 1;
		color(5, 8, 1); //graph[5][8] = 1;
		color(6, 7, 2); //graph[6][7] = 2;
		
		
	}
	
	public void randomInit(int n){
		Random ran = new Random();
		int[][] cells = new int[2][2*n];
		Arrays.fill(cells[0], -1);
		Arrays.fill(cells[1], -1);
		int count = 0;
		while(count<2*n){
			int x = -1, y=-1;
			while(contains(cells, x, y)){
				x = ran.nextInt(size);
				y = ran.nextInt(size);
			}
			cells[0][count] = x;
			cells[1][count] = y;
			count++;
		}
		for(int i=0; i<2*n; i++){
			//graph[cells[0][i]][cells[1][i]] = i>n-1?1:2;
			color(cells[0][i], cells[1][i], i>n-1?1:2);
		}
	}
	
	private boolean contains(int[][] cells, int x, int y){
		for(int i=0; i<cells[0].length; i++){
			if(cells[0][i]==x && cells[1][i]==y){
				return true;
			}
		}
		return false;
	}
	
	public void display(){
		for(int i=0; i<size; i++){
			for(int j=0; j<size-i-1; j++){
				System.out.print("     ");
			}
			for(int j=0; j<i+1; j++){
				String c = getSign(graph[i-j][j]);
				System.out.printf("(%2s )     ", c);
				//System.out.printf("|%d|   ", graph[i-j][j]);
			}
			System.out.println();
		}
		for(int i=size; i<2*size-1; i++){
			for(int j=0; j<i-size+1; j++){
				System.out.print("     ");
			}
			for(int j=0; j<2*size-i-1; j++){
				String c = getSign(graph[size-j-1][i-size+j+1]);
				System.out.printf("(%2s )     ", c);
				//System.out.printf("|%d|   ", graph[size-j-1][i-size+j+1]);
			}
			System.out.println();
		}
	}
	
	public String getSign(int n){
		switch(n){
		case 0:
			return " ";
		case 1:
			return "R";  //red
		case 2:
			return "B";  //blue
		case 4:
			return "D";  //dead
		case 5:
			return "RC"; //red capture
		case 6:
			return "BC"; //blue capture
		case 7:
			return "RI"; //red inferior
		case 8: 
			return "BI"; //blue inferior
		case 9:
			return ".";  //dot
		default:
			return " ";
		}
	}
	
	public int get(int[] pos){
		return graph[pos[0]][pos[1]];
	}
	
	public void displayWithPattern(ArrayList<int[]> p){
		Graph g = new Graph(this);
		for(int[] node: p){
			g.graph[node[0]][node[1]] = node[2];
		}
		g.display();
	}
	
	public void color(int x, int y, int c){
		graph[x][y] = c;
		if(c==1 || c==5){
			reds[y] = 1;
		}else if(c==2 || c==6){
			blues[x] = 1;
		}
		count++;
	}
	
	public void color(int[] pos, int c){
		graph[pos[0]][pos[1]] = c;
		if(c==1 || c==5){
			reds[pos[1]] = 1;
		}else if(c==2|| c==6){
			blues[pos[0]] = 1;
		}
		count++;
	}
	
	public int winTest(ArrayList<int[]> nodes){
		if(winTest(1, nodes)) return 1;
		if(winTest(2, nodes)) return 2;
		return 0;
	}
	
	public boolean winTest(int c, ArrayList<int[]> nodes){
		Graph temp = new Graph(this);
		if(nodes!=null){
			for(int[] node: nodes){
				temp.color(node[0], node[1], node[2]);
			}
		}
		if(c==1){
			for(int i=0; i<size; i++)
				if(temp.reds[i]==0){
					//System.out.println("Red has empty line");
					return false;
				}
			 return temp.searchRed();
		}else if(c==2){
			for(int i=0; i<size; i++)
				if(temp.blues[i]==0) {
					//System.out.println("Blue has empty line");
					return false;
				}
			return temp.searchBlue();
		}else{
			System.out.println("ERROR!!!!!!!!!!!");
			return false;
		}
	}
	
	private boolean searchRed(){
		int[][] explored = new int[size][size];
		for(int i=0; i<size; i++){
			if(graph[i][0]==1){				
				explored[i][0] = 1;
				if(search(i, 0, explored, 1)) return true;
			}
		}
		return false;
	}
	
	private boolean search(int x, int y, int[][] map, int c){
		//System.out.println("searching "+x+" "+y);
		if(c==1){
			if(y==size-1) return true;
		}else{
			if(x==size-1) return true;
		}
		int[] cur = {x, y};
		for(int i=1; i<7; i++){
			int[] next = getNext(cur, i);
			//is next is in borad, and not explored, and is same color, and 
			if(next!=null && map[next[0]][next[1]]==0 && (c==this.get(next) || (c+4 == this.get(next) && this.get(cur)!=c+4))){
				map[next[0]][next[1]]=1;
				if(search(next[0], next[1], map, c)) return true;
			}
		}
		return false;
	}
	
	private boolean searchBlue(){
		int[][] explored = new int[size][size];
		for(int i=0; i<size; i++){
			if(graph[0][i]==2){		
				explored[0][i] = 1;
				if(search(0, i, explored, 2)) return true;
			}
		}
		return false;
	}
	
	private int[] getNext(int[] cur, int dir){
		dir = (dir+5)%6 +1;
		int[] next = new int[2];
		switch(dir){
		case 1: 
			if(cur[0]<size-1){
				next[0] = cur[0]+1;
				next[1] = cur[1];
				return next;
			}
			return null;
		case 2:
			if(cur[0]<size-1 && cur[1]<size-1){
				next[0] = cur[0]+1;
				next[1] = cur[1]+1;
				return next;
			}
			return null;
		case 3:
			if(cur[1]<size-1){
				next[0] = cur[0];
				next[1] = cur[1]+1;
				return next;
			}
			return null;
		case 4:
			if(cur[0]>0){
				next[0] = cur[0]-1;
				next[1] = cur[1];
				return next;
			}
			return null;
		case 5:
			if(cur[0]>0 && cur[1]>0){
				next[0] = cur[0]-1;
				next[1] = cur[1]-1;
				return next;
			}
			return null;
		case 6:
			if(cur[1]>0){
				next[0] = cur[0];
				next[1] = cur[1]-1;
				return next;
			}
			return null;
		default:
			return null;
		}
	}
	
	public void displayWithAsix(){
		for(int i=0; i<size; i++){
			System.out.print("     ");
		}
		System.out.println(" |0|       |0|");
		for(int i=0; i<size; i++){
			for(int j=0; j<size-i-1; j++){
				System.out.print("     ");
			}
			if(i!=8){
				System.out.printf(" |%d|      ", i+1);
			}else{
				System.out.print("     ");
				System.out.print("     ");
			}
			for(int j=0; j<i+1; j++){
				String c = getSign(graph[i-j][j]);
				System.out.printf("(%2s )     ", c);
				//System.out.printf("|%d|   ", graph[i-j][j]);
			}
			if(i!=8)	System.out.printf(" |%d|      ", i+1);
			System.out.println();
		}
		for(int i=size; i<2*size-1; i++){
			for(int j=0; j<i-size+1; j++){
				System.out.print("     ");
			}
			System.out.printf(" |%d|      ", i-9);
			for(int j=0; j<2*size-i-1; j++){
				String c = getSign(graph[size-j-1][i-size+j+1]);
				System.out.printf("(%2s )     ", c);
				//System.out.printf("|%d|   ", graph[size-j-1][i-size+j+1]);
			}
			System.out.printf(" |%d|      ", i-9);
			System.out.println();
		}
		for(int i=0; i<size; i++){
			System.out.print("     ");
		}
		System.out.println(" |8|       |8|");
	}
	
	
}
