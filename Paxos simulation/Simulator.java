package paxos;

import paxos.Acceptor;
import paxos.Message;
import paxos.Net;

import java.lang.Thread;

public class Simulator {
	public static int nodeNum;
	public static int counter;
	public static boolean Ready = false;
	public static int simulatorTimeCounter = 0;
	public static boolean end = false;
	public static int currentLargestProposalValue = 0;

	static int proposerNum = 3;
	static int acceptorNum = 50;
	static int learnerNum = 1;

	public static long timekit = 20;

	public static Net paxosNet;

	public static void main(String[] args) {
		nodeNum = proposerNum + acceptorNum + learnerNum;
		paxosNet = new Net(nodeNum);

		// TODO for each member in the net, create a thread for listen, handle &
		// reply; create a net thread to deal with the message and do the
		// time counting, and add thread name to the thread dictionary
		new Thread(null, paxosNet, "T").start();

		for (int i = 0; i < Simulator.proposerNum; i++) {
			paxosNet.addNode(Message.PROPOSER, i);
			new Thread(null, new Proposer(i), "P" + Integer.toString(i))
					.start();
		}

		for (int j = 0; j < Simulator.acceptorNum; j++) {
			paxosNet.addNode(Message.ACCEPTOR, j);
			new Thread(null, new Acceptor(j), "A" + Integer.toString(j))
					.start();
		}

		for (int k = 0; k < Simulator.learnerNum; k++) {
			paxosNet.addNode(Message.LEARNER, k);
			new Thread(null, new Learner(k), "L" + Integer.toString(k)).start();
		}
	}
}
