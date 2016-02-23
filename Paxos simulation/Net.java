package paxos;

import paxos.Message;
import paxos.Simulator;

import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.lang.Math;

public class Net implements Runnable {
	int[][] Nodes;
	int numOfNodes;
	int numOfProposer;
	int numOfAcceptor;
	int numOfLearner;
	Map<Integer, Message> messageDict;
	public static final Object current = new Object();
	Message currentMessage;
	int counterNode; // counter for nodes add for initialize
	int[][] socket;

	public boolean replyFlag = false;
	public final Object reply = new Object();

	public Net(int num) {
		this.numOfNodes = num;
		this.numOfLearner = 0;
		this.numOfProposer = 0;
		this.numOfAcceptor = 0;
		this.Nodes = new int[num][2];
		this.counterNode = 0;
		messageDict = new HashMap<Integer, Message>();
		this.currentMessage = new Message();

	}

	public void addNode(int role, int id) {
		this.counterNode++;
		if (this.counterNode > this.numOfNodes) {
			throw new ArrayIndexOutOfBoundsException("More nodes added error");
		}
		switch (role) {
		case Message.ACCEPTOR:
			this.Nodes[counterNode - 1][0] = Message.ACCEPTOR;
			this.Nodes[counterNode - 1][1] = id;
			this.numOfAcceptor++;
			break;
		case Message.PROPOSER:
			this.Nodes[counterNode - 1][0] = Message.PROPOSER;
			this.Nodes[counterNode - 1][1] = id;
			this.numOfProposer++;
			break;
		case Message.LEARNER:
			this.Nodes[counterNode - 1][0] = Message.LEARNER;
			this.Nodes[counterNode - 1][1] = id;
			this.numOfLearner++;
			break;
		}
	}

	public int[] getLearner() {
		int[] learners = new int[this.numOfLearner];
		int count = 0;
		for (int i = 0; i < this.numOfNodes; i++) {
			if (this.Nodes[i][0] == Message.LEARNER) {
				learners[count] = this.Nodes[i][1];
				count++;
			}
		}
		return learners;
	}

	public int[] getAcceptor() {
		int[] acceptors = new int[this.numOfAcceptor];
		int count = 0;
		for (int i = 0; i < this.numOfNodes; i++) {
			if (this.Nodes[i][0] == Message.ACCEPTOR) {
				acceptors[count] = this.Nodes[i][1];
				count++;
			}
		}
		return acceptors;
	}

	public void getMessage(int timeCounter, Message message) {
		synchronized (this.messageDict) {
			Random rand = new Random();
			int delay = rand.nextInt(100) + 5;
			int outPutTime = delay + timeCounter;
			// TODO put the message into the queue with outPutTime
			Message temp = Message.cloneMessage(message);
			for (;;) {
				if (this.messageDict.get(outPutTime) == null) {
					this.messageDict.put(outPutTime, temp);
					// System.out.println("new message arrive, and the outputtime"
					// + outPutTime);
					break;
				} else {
					outPutTime++;
				}
			}
			// System.out.println("new added message num"
			// + this.messageDict.size() +
			// "of value"+this.messageDict.get(outPutTime).data+"at outcome time"+outPutTime);
		}

	}

	public Message putOutMessage(int timeCounter) {
		// TODO find and put out the messages with output time equals the
		// timecounter

		if (this.messageDict.get(timeCounter) == null) {
			return null;
		} else {
			Message temp = Message.cloneMessage(this.messageDict
					.get(timeCounter));
			// System.out.println(temp.data+"for test!!!!!!!");
			return temp;
		}

	}

	public void run() {
		// start a loop for counter
		for (;;) {
			try {
				Thread.currentThread().sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (Simulator.counter == Simulator.nodeNum) {

				int width = Math.max(
						Math.max(this.numOfAcceptor, this.numOfLearner),
						this.numOfProposer);
				this.socket = new int[3][width];
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < width; j++) {
						socket[i][j] = 0;
					}
				}
				Simulator.Ready = true;
				System.out
						.println("Net built successfully, all member ready of member num "
								+ Simulator.counter);
				break;
			}
		}

		/*
		 * // FOR TEST int m, n; for (m = 0; m < this.numOfNodes; m++) { for (n
		 * = 0; n < 2; n++) { System.out.print(this.Nodes[m][n] + " "); }
		 * System.out.print("\n"); }
		 */

		for (;;) {
			try {
				Thread.currentThread().sleep(Simulator.timekit);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println("Time counter "+Simulator.simulatorTimeCounter);
			if (this.messageDict.get(Simulator.simulatorTimeCounter) != null) {

				this.currentMessage = this.messageDict
						.get(Simulator.simulatorTimeCounter);

				if (this.currentMessage.request.equals("CONSENSUS")) {
					// TODO learner get value, end
					Simulator.end = true;
					System.out
							.println("Net is shut down after learner got value !");
					break;
				} else {
					// System.out.println("Net get a message and is delivering"+(currentMessage.acceptRole
					// - 1)+" "+currentMessage.acceptId);
					this.socket[currentMessage.acceptRole - 1][currentMessage.acceptId] = 1;
					System.out.println("Time counter "
							+ Simulator.simulatorTimeCounter);
					for (;;) {
						if (this.replyFlag == true) {
							this.replyFlag = false;
							break;
						} else {
							try {
								Thread.currentThread().sleep(Simulator.timekit);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
			Simulator.simulatorTimeCounter++;
			if (Simulator.simulatorTimeCounter > 1000) {
				System.out.println("Time out end !");
				break;
			}
		}
	}
}