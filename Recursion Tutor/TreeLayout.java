import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

//this is for display the tree(or list that branches to left only)
//for tree, it is binary tree only
public class TreeLayout {
	//array list to store nodes
    private ArrayList<TreeNode> tree;
    //a panel pased to it for display
	private JPanel mPanel;	
	
	//list to store the display action for recall
	private ArrayList<Command> commands;
	//track the currently active command
	private int curCd;
	
	//panel size for adjust the position and size of label for each node
	private int xSize;
	private int ySize;
	
	//num of nodes
	private int num;
	private TreeNode root;
	//track of current node
	private TreeNode cur;
	
	//label size adjusted according to the panel size and tree size
	private int nodeH;
	private int nodeW;
	
	//true for non-branching, false for branching
	private boolean mode;
	
	//only constructor that pass the panel to it
	public TreeLayout(JPanel pnl){
		tree = new ArrayList<TreeNode>();
		commands = new ArrayList<Command>();
		mPanel = pnl;
		xSize = pnl.getWidth();
		ySize = pnl.getHeight();
		mode = true;
		root = null;
		cur = null;
		num = 0;	
		nodeH = 0;
		nodeW = 0;
		curCd = 0;
	}
	
	public void resetTree(){
		mode = true;
		root = null;
		cur = null;
		num = 0;	
		nodeH = 0;
		nodeW = 0;
	    tree = new ArrayList<TreeNode>();
	    mPanel.removeAll();
		mPanel.revalidate();
		mPanel.repaint();
	}
	
	public void treeDisplay(int index, String act){
		Command cd = new Command(index, act);
		commands.add(cd);
		curCd++;
		if(cd.getContext()==""){
			this.markUsed(index);
		}else{
			this.setNode(index, act);
		}
	}
	
	public void dspPre(){
		if(curCd>0){
			curCd--;
			Command cd = commands.get(curCd);
			if(cd.getContext()==""){
				this.markUsedRe(cd.getIndex());
			}else{
				this.setNodeRe(cd.getIndex(), cd.getContext());
			}
		}
	}
	
	public void dspNext(){
		if(curCd<commands.size()){
			Command cd = commands.get(curCd);
			if(cd.getContext()==""){
				this.markUsed(cd.getIndex());
			}else{
				this.setNode(cd.getIndex(),cd.getContext());
			}
			curCd++;
		}
	}
	
	//build the tree alone the processing of recursion, seems hard, not completed yet
	public boolean addNode(String name, String context, boolean isLeaf){
		TreeNode node = genNode(name, context, isLeaf);
		if(node==null){
			return false;
		}
		
		
		return true;
	}
	
	//set the type tree/list and graph size(how many lvls)
	public void setTree(int lvl, boolean m){
		//m is mode, true for list, false for binary tree
		mode = m;
		if(m){
			genList(lvl);
		}else{
			genTree(lvl);
		}
	}
	
	//be careful, do not set cur to be null
	public boolean forward() {
		return treeTraversal(root);
	}
	
	//be careful, not set cur to be null
	public boolean backward(){
		if (mode) {
			if (cur != null) {
				cur.setColor(Color.GREEN);
				if(cur.parent!=null){
					cur = cur.parent;
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean treeTraversal(TreeNode node){
		if(node == null)
			return false;
		node.setVisible();
		if (node.parent != null) {
			Graphics g = mPanel.getGraphics();
			g.drawLine(node.parent.mLabel.getX()+nodeW/2, node.parent.mLabel.getY()+nodeH,
					node.mLabel.getX()+nodeW/2, node.mLabel.getY());
		}
		if(node.left==null){			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			node.setColor(Color.GREEN);
			
		}else{
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			treeTraversal(node.getLeft());
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (node.getRight() != null) {
				treeTraversal(node.getRight());
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}		
			node.setColor(Color.GREEN);
			
		}
		if(node.mId==0){
			return false;
		}else{
			return true;
		}
	}
	
	//pre-generate the list with list size given
	private void genList(int lvl){
		//generate a list of # of lvl element 
		nodeH = ySize/(2*lvl +1);
		nodeW = xSize/3;
		for(int i=0; i<lvl; i++){
			TreeNode node = new TreeNode();
			//store it in the arraylist in case
			tree.add(node);
			node.setId(i);
			if(i==0){
				root = node;
				cur = node;
			}else{
				cur.branchLeft(node);
				cur = node;
			}
			//hard code the position of the treeNode, for easy use
			mPanel.add(node.mLabel);
			node.mLabel.setBounds((xSize-nodeW)/2, (2*i+1)*nodeH, nodeW, nodeH);
			node.mLabel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
			/*
			 * this line draw when simulation runs
			if(i!=0){
				Graphics g = mPanel.getGraphics();
				g.drawLine(xSize/2, 20+i*60, xSize/2, i*60+50);
			}
			*/
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			node.setInvisible();
		}
		//don't forget point cur to root when finish generation
		cur = root;
	}
	
	//pre-generate the tree with size given 
	private void genTree(int lvl){
		//generate a tree of level of lvl
		num = 0;
		nodeH = ySize/(2*lvl+1);
		nodeW = (int) (xSize*4/(5*(Math.pow(2,(lvl-1)))));
		for(int i=0; i<lvl; i++){
			for(int j=0; j<(int)Math.pow(2, i); j++){
				TreeNode node = new TreeNode();
				tree.add(node);
				node.setId(num);
				if(i==0){
					root = node;
				}else{
					if(num%2==1){
						tree.get((num-1)/2).branchLeft(node);
					}else{
						tree.get((num-2)/2).branchRight(node);
					}
				}
				num++;	
				mPanel.add(node.mLabel);
				node.mLabel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
				node.mLabel.setBounds((xSize*(2*j+1)/(int)(Math.pow(2, i+1)))-(nodeW*(lvl+4)/(i+5))/2, (2*i+1)*nodeH, nodeW*(lvl+4)/(i+5), nodeH*3/2);
				node.mLabel.setFont(new Font("Lucida Grande", Font.PLAIN, nodeW/4));
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				node.setInvisible();
			}
		}
	}
	
	public void setNode(final int i, final String s) {
		tree.get(i).mLabel.setText(s);
		tree.get(i).setVisible();
	}
	
	public void setNodeRe(final int i, final String s){
		tree.get(i).mLabel.setText("");
		tree.get(i).setInvisible();
	}

	public void markUsed(final int i) {
		tree.get(i).setColor(Color.GREEN);
	}
	
	public void markUsedRe(final int i){
		tree.get(i).setColor(new Color(238, 238, 238));
	}
	
	
	//build the tree in a DFS order
	public TreeNode genNode(String name, String context, boolean isLeaf){
		JTextArea lb = new JTextArea();
		TreeNode node = new TreeNode(name, num, lb, context);
		num++;
		if(num==1){
			root = node;
			cur = node;
			return node;
		}else{
			if(mode){
				cur.branchLeft(node);
				cur = node;
				return node;
			}else{
				if(cur.getLeft()==null){
					cur.branchLeft(node);
					if(!isLeaf){
						cur = node;
					}
					return node;
				}else{
					cur.branchRight(node);
					if(!isLeaf){
						cur = node;
						return node;
					}else{
						if(backTrack(cur)){
						    return node;
						}else{
							return null;
						}
					}
				}
			}
		}
	}
	
	private boolean backTrack(TreeNode cur){
		while(cur.parent!=null){
			if(cur.parent.getRight()==null){
				cur = cur.parent;
				return true;
			}
			cur = cur.parent;
		}
		return false;
	}
	
	private class Command{
		private int index;
		private String context;
		
		public Command(int i, String s){
			setIndex(i);
			setContext(s);
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public String getContext() {
			return context;
		}

		public void setContext(String context) {
			this.context = context;
		}
	}
	
	private class TreeNode{
        private String mName;
        private int mId;
    	private JTextArea mLabel;
    	private TreeNode parent;
    	private TreeNode left;
    	private TreeNode right;
    	private String mContext;
    	
    	public TreeNode(String name, int id, JTextArea lb, String ctx){
    		mName = name;
    		mId = id;
    		if(lb!=null){
    			mLabel = lb;
    		}else{
    			mLabel = new JTextArea();
    			mLabel.setOpaque(true);
    			mLabel.setLineWrap(true);
    			mLabel.setWrapStyleWord(true);
    			mLabel.setEditable(false);
    		}
    		mContext = ctx;
    		parent  = null;
    		left = null;
    		right = null;
    	}
    	  	
    	public TreeNode(){
    		//id -1 means error
    		this(null, -1, null, null);
    	}
    	
    	public void setName(String s){
    		mName = s;
    	}
    	
    	public void setId(int n){
    		mId = n;
    	}
    	
    	public void setLB(JTextArea lb){
    		mLabel = lb;
    	}
    	
    	public TreeNode getLeft(){
    		return left;
    	}
    	
    	public TreeNode getRight(){
    		return right;
    	}
    	
    	public void branchLeft(TreeNode lb){
    		left = lb;
    		lb.parent = this;
    	}
    	
    	public void branchRight(TreeNode lb){
    		right = lb;
    		lb.parent = this;
    	}
    	
    	public void setVisible(){
    		mLabel.setVisible(true);
    	}
    	
    	public void setInvisible(){
    		mLabel.setVisible(false);
    	}
    	
    	public void setColor(Color c){
    		mLabel.setBackground(c);		
    	}
    }
}
