import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.border.LineBorder;
import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Font;

public class Recur {

	private JFrame frame;
	private Stack stack;
	private JButton btnRunSim;
	private JLabel Stack_Title;
	private JTextArea txtInfo;
	private JLabel Info_Label;
	private JPanel treePanel;
	private InfoCenter mInfoCenter;
	private TreeLayout tree;
	private JLabel Tree_Title;
	private JPanel stackPanel;
	private JLabel Title;
	private JTextArea example;
	private JButton preMes;
	private JButton nextMes;
	private JButton btnRunStep;

	private boolean runMode;
	private Object lock;
	private boolean running;
	private boolean nextStep;
	private int curAct;
	private JButton stopbtn;
	private boolean stopCurSim;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Recur window = new Recur();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Recur() {
		initialize();
	}

	// this part is a runnable task should be implemented in a new thread
	// now it's just for test
	public void runSimulation() {
		new Thread(new Runnable() {
			@Override
			public void run() {

				// always reset before run
				mInfoCenter.reset();
				tree.resetTree();
				stack.resetStack();

				// test stack pull, push, peek
				mInfoCenter.post("Build a stack of size n=6");
				stack.setSize(6);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				mInfoCenter.post("Test for stack push");
				// push
				for (int i = 0; i < stack.getSize(); i++) {
					stack.push(Integer.toString(i));
					// peek
					mInfoCenter.post("This is peek " + stack.peek());
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// poll
				mInfoCenter.post("Test for stack poll");
				while (stack.getCount() > 0) {
					mInfoCenter.post(stack.poll() + " is polled out");
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// clear
				stack.clearStack();

				// test info center
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// post new info into the center
				mInfoCenter.post("test for info center, reset it before start");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mInfoCenter.reset();

				for (int i = 0; i < 5; i++) {
					mInfoCenter.post("This is message #" + Integer.toString(i));
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// recall the previous message if possible
				while (mInfoCenter.preInfo()) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// go to next message if possible
				while (mInfoCenter.nextInfo()) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// test treelayout
				mInfoCenter.post("test for layout");
				// test for list
				// build tree
				mInfoCenter.post("Generate a list of size 5");
				tree.setTree(5, true);

				mInfoCenter.post("Go through the tree and do back track");
				// go forward
				while (tree.forward()) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				/*
				 * //backtrack while(tree.backward()){ try { Thread.sleep(500);
				 * } catch (InterruptedException e) { // TODO Auto-generated
				 * catch block e.printStackTrace(); } }
				 */

				// test for tree
				tree.resetTree();

				mInfoCenter.post("Generate a binary tree of lvl 4");
				tree.setTree(4, false);

				mInfoCenter.post("Tree traversal via DFS");
				// tree traversal, no need to go backward
				while (tree.forward()) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		}).start();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		runMode = true;
		lock = new Object();
		running = false;
		nextStep = false;
		curAct = 0;
		stopCurSim = false;

		frame = new JFrame();
		frame.setBounds(100, 100, 900, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		btnRunSim = new JButton("Run Simulation");
		btnRunSim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnRunSim.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				runMode = true;
				if (!running) {
					display();
					running = true;
				}
				synchronized (lock) {
					lock.notify();
				}
			}
		});
		btnRunSim.setBounds(28, 489, 165, 50);
		frame.getContentPane().add(btnRunSim);

		Stack_Title = new JLabel("Stack View");
		Stack_Title.setHorizontalAlignment(SwingConstants.CENTER);
		Stack_Title.setBounds(743, 225, 95, 33);
		frame.getContentPane().add(Stack_Title);

		Info_Label = new JLabel("Info Center");
		Info_Label.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		Info_Label.setBounds(28, 96, 159, 59);
		Info_Label.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(Info_Label);

		treePanel = new JPanel();
		treePanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		treePanel.setBounds(229, 260, 480, 400);
		frame.getContentPane().add(treePanel);
		treePanel.setLayout(null);

		tree = new TreeLayout(treePanel);

		Tree_Title = new JLabel("Tree_View");
		Tree_Title.setBounds(410, 225, 95, 33);
		Tree_Title.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(Tree_Title);

		stackPanel = new JPanel();
		stackPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		stackPanel.setBounds(728, 260, 151, 400);
		frame.getContentPane().add(stackPanel);
		stackPanel.setLayout(null);

		stack = new Stack(stackPanel);

		txtInfo = new JTextArea();
		txtInfo.setBounds(16, 148, 201, 288);
		frame.getContentPane().add(txtInfo);
		txtInfo.setText("Info center");
		txtInfo.setLineWrap(true);
		txtInfo.setWrapStyleWord(true);

		mInfoCenter = new InfoCenter(txtInfo);

		Title = new JLabel("Recursion Tutor");
		Title.setFont(new Font("Noteworthy", Font.BOLD, 32));
		Title.setHorizontalAlignment(SwingConstants.CENTER);
		Title.setBounds(328, 6, 259, 89);
		frame.getContentPane().add(Title);

		example = new JTextArea();
		example.setText("problem body");
		example.setBounds(229, 96, 650, 133);
		frame.getContentPane().add(example);
		example.setLineWrap(true);
		example.setWrapStyleWord(true);

		preMes = new JButton("<<");
		preMes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mInfoCenter.preInfo();
				tree.dspPre();
				stack.preStep();
				curAct--;
			}
		});
		preMes.setBounds(16, 448, 82, 29);
		frame.getContentPane().add(preMes);

		nextMes = new JButton(">>");
		nextMes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				curAct++;
				mInfoCenter.nextInfo();
				tree.dspNext();
				stack.nextStep();
			}
		});
		nextMes.setBounds(120, 448, 82, 29);
		frame.getContentPane().add(nextMes);

		btnRunStep = new JButton("Run Step");
		btnRunStep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				runMode = false;
				if (!running) {
					display();
					running = true;
				}
				synchronized (lock) {
					lock.notify();
				}
			}
		});
		btnRunStep.setBounds(28, 551, 165, 50);
		frame.getContentPane().add(btnRunStep);

		stopbtn = new JButton("Stop");
		stopbtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				stopCurSim = true;
				synchronized (lock) {
					lock.notify();
				}
			}
		});
		stopbtn.setBounds(28, 622, 165, 50);
		frame.getContentPane().add(stopbtn);
	}

	public void display() {
		MergeSortEx ex = new MergeSortEx();
		final ArrayList<Action> actions = ex.SortAndDisplay();
		final int size = ex.getlvl();

		example.setText(ex.problem);

		new Thread(new Runnable() {

			@Override
			public void run() {
				stack.setSize(size);
				tree.setTree(size, false);
				for (Action act : actions) {
					if (!runMode) {
						synchronized (lock) {
							try {
								lock.wait();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					if (stopCurSim) {
						stack.resetStack();
						tree.resetTree();
						mInfoCenter.reset();
						running = false;
						stopCurSim = false;
						example.setText("");
						return;
					}

					while (mInfoCenter.nextInfo()) {
						tree.dspNext();
						stack.nextStep();
					}

					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (act.stackAct != "") {
						stack.push(act.stackAct);
					} else {
						stack.poll();
					}

					tree.treeDisplay(act.nodeNum, act.treeAct);

					for (String s : act.hints) {
						mInfoCenter.post(s);
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				while (true) {
					synchronized (lock) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (stopCurSim) {
						stack.resetStack();
						tree.resetTree();
						mInfoCenter.reset();
						running = false;
						stopCurSim = false;
						example.setText("");
						break;
					}
				}
			}
		}).start();
	}
}
