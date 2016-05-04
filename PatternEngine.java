import java.util.ArrayList;


public class PatternEngine {
	ArrayList<Pattern> deadP;
	ArrayList<Pattern> captureP;
	ArrayList<Pattern> inferiorP;
	
	public void initialPatterns(){
		int[] p1 = {0, 1, 1, 2}, c1 = {1, 1, 1, 4};
		deadP.add(new Pattern(p1, c1, 1));
		int[] p2 = {0, 2, 0, -2}, c2 = {1, 4, 2, 2};
		deadP.add(new Pattern(p2, c2, 1));
		int[] p3 = {0, 1, -2, -1}, c3 = {4, 2, 2, 2};
		deadP.add(new Pattern(p3, c3, 1));
		int[] p4 = {0, 2, -1, -2, 0}, c4 = {5, 5, 1, 1, 1};
		captureP.add(new Pattern(p4, c4, 2));
		int[] p5 = {0, 1, 0, 1, 2, 0}, c5 = {1, 1, 1, 1, 5, 5};
		captureP.add(new Pattern(p5, c5, 2));
		int[] p6 = {0, 2, -2, 2, -2}, c6 = {1, 5, 5, 1, 1};
		captureP.add(new Pattern(p6, c6, 2));
		int[] p7 = {0, 0, 2, 1, -2}, c7 = {1, 1, 7, 7, 9};
		inferiorP.add(new Pattern(p7, c7, 3));
		int[] p8 = {0, 2, -2, 2, -2}, c8 = {1, 7, 7, 9, 1};
		inferiorP.add(new Pattern(p8, c8, 3));
		int[] p9 = {0, 1, 1, 2}, c9 = {1, 1, 9, 8};
		inferiorP.add(new Pattern(p9, c9, 3));
		int[] p10 = {0, 1, 2, -1}, c10 = {1, 1, 7, 9};
		inferiorP.add(new Pattern(p10, c10, 3));
		int[] p11 = {0, 0, 2, 1, -1}, c11 = {7, 1, 1, 7, 9};
		inferiorP.add(new Pattern(p11, c11, 3));
		//5 and -5 is a special exception here
		int[] p12 = {0, -1, 2, 1, -2, -1}, c12 = {8, 2, 8, 2, 3, 9};
		inferiorP.add(new Pattern(p12, c12, 3));
		int[] p13 = {0, -1, -2, 1, -2, 1}, c13 = {3, 0, 5, 1, 5, 0};
		captureP.add(new Pattern(p13, c13, 2));
		deadP.addAll(captureP);
	}
	
	public PatternEngine(){
		deadP = new ArrayList<Pattern>();
		captureP = new ArrayList<Pattern>();
		inferiorP = new ArrayList<Pattern>();
	}
	
	public ArrayList<int[]> applyPattern(Graph g){
		ArrayList<int[]> res = new ArrayList<int[]>();
		for(int i=0; i<g.size; i++){
			for(int j=0; j<g.size; j++){
				if(g.graph[i][j]!=0){
					int[] pos = {i, j};
					applyToCell(g, pos, res);
				}
			}
		}
		return res;
	}
	
	private void applyToCell(Graph g, int[] pos, ArrayList<int[]> res){
		int c = g.get(pos);
		//System.out.println("Currently test pattern from position" + pos[0]+":"+pos[1]);
		for(Pattern p: deadP){
			//System.out.println("  Try Pattern "+p.toString());
			for(int i=1; i<7; i++){
				int[] pos1 = getNext(pos, i, g.size);				
				if(pos1!=null){
					for (int r = 1; r > -2; r = r - 2) {
						//System.out.println("    test direction " + i);
						boolean matched = true;
						pos1 = pos;
						int dir = i;
						for (int j = 0; j < p.positions.length; j++) {
							dir = dir + r * p.positions[j];
							pos1 = getNext(pos1, dir, g.size);
							if (pos1 == null) {
								//System.out.println("         out of board");
								matched = false;
								break;
							} else {
								//System.out.println("         reaches pos "
										//+ pos1[0] + " " + pos1[1]);

								if (g.get(pos1) != getPatternColor(c, p, j) && getPatternColor(c, p, j)!=3) {
									/*
									System.out
											.println("           Mismatch: expect "
													+ getPatternColor(c, p, j)
													+ " is " + g.get(pos1));
													*/
									matched = false;
									break;
								}
							}
						}
						if (matched) {
							pos1 = pos;
							dir = i;
							for (int j = 0; j < p.positions.length; j++) {
								dir = dir + r * p.positions[j];
								pos1 = getNext(pos1, dir, g.size);
								if (p.colors[j] > 3) {
									int[] temp = new int[3];
									temp[0] = pos1[0];
									temp[1] = pos1[1];
									if (p.colors[j] == 4) {
										temp[2] = 4;
									} else if (p.colors[j] == 5) {
										temp[2] = 4 + c;
									} else if (p.colors[j]==6){
										temp[2] = 7 - c;
									} else if(p.colors[j] == 7){
										temp[2] = 6+c;
									} else if(p.colors[j] == 8){
										temp[2] = 9-c;
									} else if(p.colors[j] == 9){
										temp[2] = 9;
									}
									res.add(temp);
									/*
									System.out
											.println("***get a dead of position"
													+ pos[0]
													+ " "
													+ pos[1]
													+ " when apply "
													+ p.toString()
													+ " at direction " + i);
													*/
								}
							}
						}
					}
				}
				
			}
		}
	}
	
	private int getPatternColor(int c, Pattern p, int i){
		switch(p.colors[i]){
		case 1:
			return c;
		case 2:
			return 3-c;
		case 3:
			return 3;
		default:
			return 0;
		}
	}
	
	private int[] getNext(int[] cur, int dir, int size){
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
}
