package paxos;

import java.util.Random;

import paxos.Message;
import paxos.Simulator;

public class Proposer implements Runnable {
	final int mRole = Message.PROPOSER;
	int mId;
	int mPropId;
	int acceptorNum;
	int promiseNum;
	int[] acceptorIds;
	boolean majority = false;
	int mLastPropId;
	int mLargestValue;

	public Proposer(int id) {
		this.mId = id;
		this.mPropId = 0;
		this.promiseNum = 0;
		this.mLastPropId = 0;
		this.mLargestValue = 0;
	}

	public void generateProposalAndSend() {
		Random rand = new Random();
		this.mPropId = 1 + rand.nextInt(10) + this.mLastPropId;
		this.mLastPropId = this.mPropId;
		System.out.println("Proposer " + this.mId
				+ "has generate proposal of num " + this.mPropId);
		Message askMsg = new Message(this.mRole, this.mId);
		for (int i = 0; i < this.acceptorNum; i++) {

			askMsg.setAccepterAndData(Message.ACCEPTOR, this.acceptorIds[i],
					this.mPropId, 0);
			askMsg.setRequest("PREPARE");
			Simulator.paxosNet.getMessage(Simulator.simulatorTimeCounter,
					askMsg);
		}
		System.out.println("Proposer " + this.mId
				+ "has send prepare requset to all acceptors with Proposal id "
				+ this.mPropId);
	}

	public void handleMessage(Message message) {
		if (message.request.equals("PROMISE")) {
			this.promiseNum++;
			this.mLargestValue = Math
					.max(message.propValue, this.mLargestValue);
			System.out.println("Proposer " + this.mId + "got the # "
					+ this.promiseNum + " promise from acceptor "
					+ message.sendId + " of " + message.propId + " "
					+ message.propValue);

			if (2 * this.promiseNum >= this.acceptorNum
					&& this.majority == false) {
				Message acceptMsg = new Message(this.mRole, this.mId);
				System.out.println("Proposer " + this.mId + "get majority");
				if (this.mLargestValue == 0) {
					Random rand = new Random();
					this.mLargestValue = rand.nextInt(5) + 1;
				}
				for (int i : this.acceptorIds) {
					acceptMsg.setAccepterAndData(Message.ACCEPTOR, i,
							this.mPropId, this.mLargestValue);
					acceptMsg.setRequest("ACCEPT");
					Simulator.paxosNet.getMessage(
							Simulator.simulatorTimeCounter, acceptMsg);
					this.majority = true;
				}
				System.out.println("Proposer " + this.mId
						+ " has sent accept to all acceptors with Proposal "
						+ this.mPropId + " of value" + this.mLargestValue);
			}
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
		System.out.println("Proposer " + this.mId + "start to work ");

		this.acceptorIds = Simulator.paxosNet.getAcceptor();
		this.acceptorNum = this.acceptorIds.length;
		this.generateProposalAndSend();
		for (;;) {
			try {
				Thread.currentThread().sleep(Simulator.timekit);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (Simulator.end == true) {
				break;
			}
			// listen for messages and handle it
			if (Simulator.paxosNet.socket[this.mRole - 1][this.mId] == 1) {
				Message temp = Simulator.paxosNet
						.putOutMessage(Simulator.simulatorTimeCounter);
				this.handleMessage(temp);
				Simulator.paxosNet.socket[this.mRole - 1][this.mId] = 0;
				Simulator.paxosNet.replyFlag = true;
			}
		}
		System.out.println("Proposer " + this.mId + " ended");

	}
}
