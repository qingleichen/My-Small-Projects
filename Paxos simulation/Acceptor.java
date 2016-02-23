package paxos;

import paxos.Net;
import paxos.Message;
import paxos.Simulator;

public class Acceptor implements Runnable {
	final int mRole = Message.ACCEPTOR;
	int mId;

	// state of the acceptor, initialized to 0 before start
	int mState;
	public static final int NULL = 0;
	public static final int PROMISED = 1;
	public static final int ACCEPTED = 2;

	// id of the proposer promised
	int mPropId;

	Net net;

	// value of the proposal it keeps
	private int mValue;

	public Acceptor(int id) {
		this.mId = id;
		this.mState = NULL;
		this.mPropId = 0;
		this.mValue = 0;
		this.net = Simulator.paxosNet;
	}

	private void handleReceivedMessage(Message message) {
		System.out.println("ACCEPTOR" + this.mId + "get " + message.request
				+ " from proposer" + message.sendId + " of " + message.propId
				+ " " + message.propValue);
		if (message.request.equals("PREPARE")) {
			// System.out.println("this is a ask with value = "+message.propId);
			if (message.propId >= this.mPropId) {
				if (this.mState == this.ACCEPTED) {
					makePromise(message, this.mPropId, this.mValue);
					this.mPropId = message.propId;
				} else {
					makePromise(message, message.propId, 0);
					this.mPropId = message.propId;
				}
			}
		} else if (message.request.equals("ACCEPT")) {
			if (message.propId >= this.mPropId) {
				this.mState = ACCEPTED;
				this.mValue = message.propValue;
				noteLearner(this.net, message);
			}
		} else if (message.request.equals("CONSENSUS")) {
			// TODO get consensus, shut down
		}
	}

	private void makePromise(Message message, int a, int b) {
		Message promMsg = new Message(this.mRole, this.mId);
		promMsg.setAccepterAndData(message.sendRole, message.sendId, a, b);
		promMsg.setRequest("PROMISE");
		// send promise to proposer
		Simulator.paxosNet.getMessage(Simulator.simulatorTimeCounter, promMsg);
		System.out.println("ACCEPTOR " + this.mId
				+ "GIVE A PROMISE TO PROPOSER " + this.mPropId
				+ "of Proposer #" + a);
	}

	private void noteLearner(Net net, Message message) {
		Message promMsg = new Message(this.mRole, this.mId);
		// GET THE LEARNER INFO
		int[] learner = net.getLearner();
		for (int i : learner) {
			promMsg.setAccepterAndData(Message.LEARNER, i, message.propId,
					message.propValue);
			promMsg.setRequest("LEARN");
			// SEND MESSAGE TO LEARNER
			Simulator.paxosNet.getMessage(Simulator.simulatorTimeCounter,
					promMsg);
		}
	}

	public void run() {
		Simulator.counter++;
		for (;;) {
			try {
				Thread.currentThread().sleep(Simulator.timekit);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (Simulator.Ready == true) {
				break;
			}
		}
		System.out.println("ACCEPTOR" + this.mId + " start to listen");
		for (;;) {
			try {
				Thread.currentThread().sleep(Simulator.timekit);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (Simulator.end == true) {
				// System.out.println("##########get consensus  ####################");
				break;
			}

			// listen for messages and handle it
			if (Simulator.paxosNet.socket[this.mRole - 1][this.mId] == 1) {
				// System.out.println("ACCEPTOR"+this.mId+" get a message request, start to read");
				Message temp = Message.cloneMessage(Simulator.paxosNet
						.putOutMessage(Simulator.simulatorTimeCounter));

				// test
				// System.out.println("ACCEPT" + this.mId + " get a "
				// + temp.request);
				this.handleReceivedMessage(temp);
				// System.out.println("ACCEPT"+this.mId+"has handled this message");
				Simulator.paxosNet.socket[this.mRole - 1][this.mId] = 0;
				Simulator.paxosNet.replyFlag = true;
				// System.out.println("ACCEPT"+this.mId+"read step 3");
				// System.out.println("acceptor" + this.mId + "get a message");
			}
		}
		System.out.println("ACCEPTOR" + this.mId + " IS ENDED");
	}
}
