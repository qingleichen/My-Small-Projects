import java.util.ArrayList;


public class ListEx {
    private int[] nums;
	private ArrayList<Action> actions = new ArrayList<>();
	private int actionCount;
	private int stackCount;
	private int lvl;
	public String problem;
	
	public ListEx(){
		actionCount = 0;
		stackCount = 0;
		lvl = 0;
		nums = new int[]{0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0};
		problem = "You are in the first carriage of a train, and wanna buy some soft drinks from the closest carriage.Now it is converted to a math model of a list of numbers, 1 means there is a vending machine in that carriage. Find it and return. '0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0'"; 
	}
	
	public ArrayList<Action> SortAndDisplay(){
		return actions;
	}
    
    
    
	private boolean searchList(Node root, int n){
		Action act = new Action();
		act.addHint("Searching for vendering machine in the train from # "+Integer.toString(stackCount) + "carriage.");
		if(root==null)
			return false;
		if(root.val==n){
			return true;
		}else{
			return searchList(root.next, n);
		}
	}
    
    
    
    
    
	private Node convertToList(int[] nums){
		if(nums.length==0)
			return null;
		Node root = null;
		Node cur = null;
		for(int i=0; i<nums.length-1; i++){
			if(i==0){
				root = new Node(nums[i]);
				cur = root;
			}else{
				cur.next = new Node(nums[i]);
				cur = cur.next;
			}			
		}
		return root;  
	}
	
	private class Node{
    	int val;
    	Node next;
    	
    	public Node(int n){
    		val = n;
    		next = null;
    	}
    	
    	public Node next(){
    		return next;
    	}
    	
    	public void setNext(Node n){
    		next = n;
    	}
    }
}
