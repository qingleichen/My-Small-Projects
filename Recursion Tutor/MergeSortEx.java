import java.util.ArrayList;


public class MergeSortEx {
	private int[] nums;
	public String problem;
	private ArrayList<Action> actions = new ArrayList<Action>();
	private int actionCount;
	private int stackCount;
	private int nodeCount;
	private int lvl;
	private boolean sorted;
	
	public MergeSortEx(){
		actionCount = 0;
		stackCount = 0;
		nodeCount = 0;
		lvl = 0;
		sorted = false;
		nums =new int[]{5, 19, 6, 3, 13, 4, 8, 11, 17, 9, 6, 10, 8, 4};
		//5, 12, 15, 31, 6, 2, 9, 25
		problem = "Sort the array of numbers using merge sort: " + this.numarraytostring(nums, 0, nums.length-1);
	}
	
	private String numarraytostring(int[] nums, int h, int t){
		String s = "";
		for(int i=h; i<=t; i++){
			s = s+ Integer.toString(nums[i])+ ", ";
		}
		return s;
	}
	
	public ArrayList<Action> SortAndDisplay(){
		System.out.println(numarraytostring(nums, 0, nums.length-1));
		mergeSort(nums, 0, nums.length-1);
		lvl++;
		sorted = true;
		return  actions;
	}
    
    private void mergeSort(int[] nums, int h, int t){
    	//add to action list: new problem is created
    	Action act = new Action();
    	act.addHint("Current problem is mergesort " + numarraytostring(nums, h, t));
    	act.setStack("Sort: "+numarraytostring(nums, h, t));
    	act.setTree("Sort: "+numarraytostring(nums, h, t), nodeCount);
    	actions.add(actionCount, act);
    	actionCount++;
    	
    	if(t-h<1){
    		//add to action list: problem is solved without divided
    		if(stackCount>lvl){
    			lvl = stackCount;
    		}
    		Action act1 = new Action(); 	
        	act1.addHint("Problems solved, only one element, directly return: " + numarraytostring(nums, h, t));
        	act1.setStack("");
        	stackCount--;
        	act1.setTree("", nodeCount);
        	nodeCount = (nodeCount-1)/2;
        	actions.add(actionCount, act1);
        	actionCount++;
    		return;
    	}else if(t==h+1){
    		if(nums[h]<=nums[t]){
    		}else{
    			System.out.println(numarraytostring(nums, h, t));
    			swap(nums, h, t);
    			System.out.println(numarraytostring(nums, h, t));
    		}
    		//add to action list: problem is solved without divided
    		Action act1 = new Action(); 	
        	act1.addHint("Problems solved, only two element, compare and swap: " + numarraytostring(nums, h, t));
        	act1.setStack("");
        	stackCount--;
        	act1.setTree("", nodeCount);
        	nodeCount = (nodeCount-1)/2;
        	actions.add(actionCount, act1);
        	actionCount++;
        	
    		return;
    	}else{
    		//add to current action hints
    		int mid = (h+t)/2;
    		
    		stackCount++;
    		nodeCount = nodeCount*2+1;
    		
    		mergeSort(nums, h, mid);
    		
    		stackCount++;
    		nodeCount = nodeCount*2+2;
    		mergeSort(nums, mid+1, t);
    		
    		merge(nums, h, t);
    		//add to action list that: problem is solved via merge
    		Action act1 = new Action(); 	
        	act1.addHint("Problems solved by merge two sorted sublist: " + numarraytostring(nums, h, t));
        	act1.setStack("");
        	stackCount--;
        	act1.setTree("", nodeCount);
        	nodeCount = (nodeCount-1)/2;
        	actions.add(actionCount, act1);
        	actionCount++;
    	}
    }
    
    private void merge(int[] nums, int h, int t){
    	int[] res = new int[t-h+1];
    	int mid = (h+t)/2;
    	int p1 = h; int p2 = mid+1;
    	int count = 0;
    	while(count<t-h+1){
    		if(p1<=mid && p2<=t){
    			if(nums[p1]<=nums[p2]){
    				res[count] = nums[p1];
    				p1++;
    			}else{
    				res[count] = nums[p2];
    				p2++;
    			}
    		}else if(p1>mid){
    			res[count]=nums[p2];
    			p2++;
    		}else{
    			res[count]=nums[p1];
    			p1++;
    		}
    		count++;
    	}
    	for(int i=0; i<t-h+1; i++){
    		nums[h+i] = res[i];
    	}
    }

    private void swap(int[] nums, int i, int j){
    	int temp = nums[i];
    	nums[i] = nums[j];
    	nums[j] = temp;
    }
    
    public int getlvl(){
    	if(sorted){
    		return lvl;
    	}else{
    		return -1;
    	}
    }
}
