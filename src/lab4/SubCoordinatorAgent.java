package lab4;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.mobility.MobilityOntology;
import jade.domain.AMSService;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import jade.wrapper.*;

/**
 * The Sub-Coordinator receives commands from the coordinator and handles all
 * the Agent Smiths in his container.
 * 
 */
public class SubCoordinatorAgent extends Agent {

	/**
	 * Registers Agent, adds behaviours.
	 */
	protected void setup() {

		/** Registration with the DF */
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("SubCoordinator");
		sd.setName(getName());
		getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
		getContentManager().registerOntology(MobilityOntology.getInstance());
		dfd.setName(getAID());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			System.err.println(getLocalName() + " registration with DF unsucceeded. Reason: " + e.getMessage());
			doDelete();
		}

		addBehaviour(new ReceiveMessage());
	}

	/**
	 * Behaviour to receive messages and react accordingly.
	 */
	public class ReceiveMessage extends CyclicBehaviour {

		/**
		 * Parses the received message and either adds agents or sends messages
		 * to existing agents.
		 */
		public void action() {
			ACLMessage msg = receive();
			if (msg != null) {

				String Message_Content = msg.getContent();
				String SenderName = msg.getSender().getLocalName();

				System.out.println(getLocalName() + "received message from " + SenderName + ". Content: " + Message_Content);

				String str = msg.getContent();
				String[] tokens = str.split(";");
				int msgType = Integer.parseInt(tokens[0]);

				switch (msgType) {
				case CoordinatorAgent.ADDAGENT:
					addBehaviour(new CreateAgents(tokens));
					break;
				case CoordinatorAgent.UPDATEPARAMS:
					String TickInterval = tokens[1];
					String TargetHost = tokens[2];
					String Port = tokens[3];
					addBehaviour(new BroadcastToSubAgents(CoordinatorAgent.UPDATEPARAMS + ";" + TickInterval + ";" + TargetHost + ";" + Port));
					break;
				case CoordinatorAgent.STARTATTACK:
					addBehaviour(new BroadcastToSubAgents(CoordinatorAgent.STARTATTACK + ";"));
					break;
				case CoordinatorAgent.STOPATTACK:
					addBehaviour(new BroadcastToSubAgents(CoordinatorAgent.STOPATTACK + ";"));
					break;
				}
			} else {
				block();
			}
		}
	}

	/**
	 * Sends a Message to all agents Smiths belonging to this Sub-Coordinator.
	 */
	public class BroadcastToSubAgents extends OneShotBehaviour {

		/**
		 * Message to be sent.
		 */
		String message;

		/**
		 * List of all the receivers.
		 */
		String[] receivers;

		/**
		 * Constructor.
		 * 
		 * @param msg
		 *            Message content.
		 */
		public BroadcastToSubAgents(String msg) {
			message = msg;
		}

		/**
		 * Gets all the receivers and then sends the ACL Message.
		 */
		public void action() {

			AMSAgentDescription[] agents = null;

			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			try {
				SearchConstraints c = new SearchConstraints();
				c.setMaxResults(new Long(-1));
				agents = AMSService.search(myAgent, new AMSAgentDescription(), c);
				for (int i = 0; i < agents.length; i++) {
					if (agents[i].getName().getLocalName().startsWith("Smith-" + getLocalName())) {
						msg.addReceiver(agents[i].getName());
					}
				}
				msg.setLanguage("English");

				msg.setContent(message);

				send(msg);
				System.out.println("Message sent to all local Smiths: " + msg.getContent());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Behaviour that creates Agent Smith according to the receiver parameters.
	 * 
	 */
	public class CreateAgents extends OneShotBehaviour {
		String[] tokens = null;

		/**
		 * Constructor.
		 * 
		 * @param tkns
		 *            The received parameters.
		 */
		public CreateAgents(String[] tkns) {
			tokens = tkns;
		}

		/**
		 * Creates an agent to the current container and specifies a unique
		 * name.
		 */
		public void action() {
			int numberOfAgents = Integer.parseInt(tokens[1]);
			String TickInterval = tokens[2];
			String TargetHost = tokens[3];
			String Port = tokens[4];
			String doAttack = tokens[5];
			String[] args = { TickInterval, TargetHost, Port, getLocalName(), doAttack.toString() };

			AgentContainer container = getContainerController();
			for (int i = 0; i < numberOfAgents; i++) {
				try {
					String S = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss.SSS").format(new Timestamp(System.currentTimeMillis()));
					String name = "Smith-" + getLocalName() + "-" + S + "-" + i;

					AgentController t1 = container.createNewAgent(name, "lab4.AgentSmith", args);
					t1.start();
					System.out.println(getLocalName() + " created new Smith called: " + name + ". Params: " + args);
				} catch (Exception any) {
					any.printStackTrace();
				}
			}
		}
	}

}