package paxos;

import java.util.Set;

import paxos.Net;
import paxos.Message;
import paxos.Simulator;

public class Learner implements Runnable {
	final int mRole = Message.LEARNER;
	int mId;
	Net net;
	int numOfAcceptor;
	int acceptNum;
	int mValue;
	boolean consensus = false;

	public Learner(int id) {
		this.mId = id;
		this.net = Simulator.paxosNet;
		this.acceptNum = 0;
		this.mValue = 0;
	}

	public void handleMessage(Message message) {
		if (message.request.equals("LEARN")) {
			if (this.consensus == false) {
				System.out.println("Learner " + this.mId
						+ " get a proprsal of " + message.propId
						+ " with value " + message.propValue
						+ " from Acceptor " + message.sendId
						+ "!!!!!!");
			}
			this.consensus = true;
			Message consensusMsg = new Message(this.mRole, this.mId);
			consensusMsg.request = "CONSENSUS";
			/*
			 * System.out.println("Learner " + this.mId +
			 * " has reached a consensus of value " + message.data);
			 */
			Simulator.paxosNet.getMessage(Simulator.simulatorTimeCounter,
					consensusMsg);
			/*
			 * if (message.data == this.mValue) { this.acceptNum++; }else
			 * if(message.data>this.mValue){ this.acceptNum=1;
			 * this.mValue=message.data; }
			 */

			/*
			 * if (2 * this.acceptNum > this.numOfAcceptor &&
			 * this.consensus==false) { this.consensus=true; Message
			 * consensusMsg = new Message(this.mRole, this.mId);
			 * consensusMsg.request = "CONSENSUS"; System.out.println("Learner "
			 * + this.mId + " has reached a consensus of value " +
			 * message.data);
			 * Simulator.paxosNet.getMessage(Simulator.simulatorTimeCounter,
			 * consensusMsg); }
			 */
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
		System.out.println("Learner " + this.mId + "start to work");
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
	}
}
