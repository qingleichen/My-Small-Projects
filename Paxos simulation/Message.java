package paxos;

public class Message {
	//
	public int sendRole, sendId;
	public int acceptRole, acceptId;
	public String request;
	public int propId;
	public int propValue;

	public static final int PROPOSER = 1;
	public static final int ACCEPTOR = 2;
	public static final int LEARNER = 3;

	public Message(int a, int a1, int b, int b1, String c, int d, int e) {
		this.sendRole = a;
		this.sendId = a1;
		this.acceptRole = b;
		this.acceptId = b1;
		this.request = c;
		this.propId = d;
		this.propValue = e;
	}

	public Message() {
		this(0, 0, 0, 0, "", 0, 0);
	}

	public static Message cloneMessage(Message msg) {
		Message temp = new Message(msg.sendRole, msg.sendId, msg.acceptRole,
				msg.acceptId, msg.request, msg.propId, msg.propValue);
		return temp;
	}

	public Message(int a, int a1) {
		this(a, a1, 0, 0, "", 0, 0);
	}

	public void setRequest(String c) {
		this.request = c;
	}

	public void setAccepterAndData(int a, int b, int c, int d) {
		this.acceptRole = a;
		this.acceptId = b;
		this.propId = c;
		this.propValue = d;
	}
}
