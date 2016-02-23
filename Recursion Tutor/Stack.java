import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;


public class Stack{
		private ArrayList<JTextArea> stacks;
		private int size;
		private int counter;
		private JPanel mPanel;
		private int xSize;
		private int ySize;
		private ArrayList<Command> commands;
		private int curCd;
		
		public Stack(int l, JPanel panel){
			stacks = new ArrayList<JTextArea>();
			size = l;
			counter = 0;
			mPanel = panel;
			xSize = panel.getWidth();
			ySize = panel.getHeight();
			genStacks();	
			commands = new ArrayList<Command>();
			curCd = 0;
		}
		
		public Stack(JPanel panel){
			this(0, panel);
		}
		
		private void genStacks(){
			if(size!=0 && stacks.size()==0){
				for(int i=0; i<size; i++){
					JTextArea lb = new JTextArea();
					lb.setOpaque(true);
					lb.setBorder(new LineBorder(new Color(0, 0, 0), 2));
					lb.setForeground(Color.BLACK);
					lb.setBounds(0, ySize-ySize*(i+1)/size, xSize, ySize/size);
					lb.setVisible(true);
					//lb.setEditable(false);
					lb.setLineWrap(true);
					lb.setWrapStyleWord(true);
					mPanel.add(lb);
					stacks.add(lb);
				}
			}
		}
		
		public void clearStack(){
			for(JTextArea lb: stacks){
				lb.setText("");
			}
			curCd = 0;
			commands = new ArrayList<Command>();
		}
		
		public void setStack(int i, String s){
			stacks.get(i).setText(s);
		}
		
		public void resetStack(){
			curCd = 0;
			commands = new ArrayList<Command>();
			size = 0;
			counter = 0;
			for(JTextArea temp: stacks){
				temp.invalidate();
			}
			stacks = new ArrayList<JTextArea>();
		    mPanel.removeAll();
			mPanel.revalidate();
			mPanel.repaint();
			
		}
		
		public void setSize(int n){
			size = n;
			genStacks();
			//this.clearStack();
		}
		
		public int getSize(){
			return size;
		}
		
		public String peek(){
			if(counter == 0){
				return "";
			}else if(counter<=size){
				return stacks.get(counter-1).getText();
			}else{
				//RETURN SOMETHING MEANINGS ERROR
				return "+";
			}		
		}
		public void preStep(){
			if(curCd>0){
				curCd--;
				Command cd = commands.get(curCd);
				if(cd.getAct()==0){
					dePush(cd);
				}else{
					dePoll(cd);
				}
			}
		}
		
		public void nextStep(){
			if(curCd<commands.size()){
				Command cd = commands.get(curCd);
				if(cd.getAct()==0){
					push(cd.getContext());
					curCd--;
					commands.remove(commands.size()-1);
				}else{
					poll();
					curCd--;
					commands.remove(commands.size()-1);
				}
				curCd++;
			}
		}
		
		public String poll(){
			if(counter<=0){
				return "+";
			}else{
				counter--;
				String res = stacks.get(counter).getText();
				stacks.get(counter).setText("");
				Command cd = new Command(1, res);
				commands.add(cd);
				curCd++;
				return res;
			}
		}
		
		public boolean push(String s){
			if(0<=counter && counter< size){
				Command cd = new Command(0, s);
				curCd++;
				commands.add(cd);
				JTextArea lb = stacks.get(counter);
				lb.setText(s);
				counter++;
				return true;
			}
			return false;
		}
		
		public void dePush(Command cd){
			curCd--;
			poll();
			commands.remove(commands.size()-1);
		}
		
		public void dePoll(Command cd){
			curCd--;
			push(cd.getContext());
			commands.remove(commands.size()-1);
		}
		
		public int getCount(){
			return counter;
		}
		
		private class Command{
			private int act;
			private String context;
			public Command(int i, String s){
				setAct(i);
				setContext(s);
			}
			public String getContext() {
				return context;
			}
			public void setContext(String context) {
				this.context = context;
			}
			public int getAct() {
				return act;
			}
			public void setAct(int act) {
				this.act = act;
			}
		}
		
		private int getStackId(JTextArea lb){
			return stacks.indexOf(lb);
		}
	}