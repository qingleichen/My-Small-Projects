
public class Pattern {
	//connection direction  
	//    1,         2,          3,          4,        5,          6 
	//    down-left, down,       down-right, up-right, up,         up-left
	//    (x+1, y)   (x+1, y+1)  (x, y+1)   (x-1, y)   (x-1, y-1)  (x, y-1)
	// when define and detect patterns, consider the area only from dir 1 to 3. This make it easier and avoid some redundant
	
	// and a pattern becomes a array store the connection actions
	int[] positions;
	int[] colors;
	
	// 1 for dead pattern, 2 for capture pattern, 3 for inferior pattern, 0 for bridge pattern 
	int type;
	
	public Pattern(int[] p, int[] c, int t){
		positions = p;
		colors = c;
		type = t;
	}
	
	public String toString(){
		String res = "";
		for(int n: positions){
			res += n;
		}
		return res;
	}
}
