import java.util.ArrayList;

public class Action {
	String stackAct;
	int stackNum;
	String treeAct;
	int nodeNum;
	ArrayList<String> hints;

	public Action() {
		stackAct = "";
		stackNum = 0;
		treeAct = "";
		nodeNum = 0;
		hints = new ArrayList<String>();
	}

	public void setStack(String s) {
		stackAct = s;
	}

	public void setTree(String s, int n) {
		treeAct = s;
		nodeNum = n;
	}

	public void addHint(String s) {
		hints.add(s);
	}
}