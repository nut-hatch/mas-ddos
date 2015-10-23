package lab4;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;

/**
 * Main Coordinator organizing the attack.
 * 
 */
public class CoordinatorAgent extends GuiAgent {

	/**
	 * The GUI object.
	 */
	private GUI gui;
	
	/**
	 * Current state of the attack.
	 */
	private Boolean doAttack = false;

	/**
	 * Registers the agent, creates the GUI and adds the behaviours.
	 */
	protected void setup() {

		/** Registration with the DF */
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("SenderAgent");
		sd.setName(getName());
		sd.setOwnership("AnarAshrafNiklas");
		sd.addOntologies("CoordinatorAgent");
		dfd.setName(getAID());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			System.err.println(getLocalName() + " registration with DF unsucceeded. Reason: " + e.getMessage());
			doDelete();
		}

		gui = new GUI(this);

		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription templateSd = new ServiceDescription();
		templateSd.setType("SubCoordinator");
		template.addServices(templateSd);

		addBehaviour(new ReceiverAgentSubscription(this, DFService.createSubscriptionMessage(this, getDefaultDF(), template, new SearchConstraints())));
		addBehaviour(new DiscoverAgents(this, 5000, "Smith"));
		// addBehaviour(new DiscoverAgents(this, 5000, "SubCoordinator"));
	}

	final static int ADDAGENT = 2;
	final static int UPDATEPARAMS = 3;
	final static int STARTATTACK = 4;
	final static int STOPATTACK = 5;
	final static int SEARCHWITHDF = 6;

	/**
	 * Reacts to certain GUI events.
	 */
	protected void onGuiEvent(GuiEvent ev) {
		switch (ev.getType()) {
		case ADDAGENT:
			System.out.println("A" + gui.getAmountOfAgents());
			System.out.println("B" + gui.getNumberOfSubCoordinators());
			int amountOfAgents = Integer.parseInt(gui.getAmountOfAgents());
			int amountofSubs = Integer.parseInt(gui.getNumberOfSubCoordinators());
			if (amountOfAgents > 0 && amountofSubs > 0) {
				int amountOfAgentsPerSub = amountOfAgents / amountofSubs;
				addBehaviour(new SendSubCoordinatorMessage(this.ADDAGENT + ";" + amountOfAgentsPerSub + ";" + gui.getTickinterval() + ";"
						+ gui.getTargetHost() + ";" + gui.getTargetPort() + ";" + doAttack.toString()));
			}
			break;
		case UPDATEPARAMS:
			addBehaviour(new SendSubCoordinatorMessage(this.UPDATEPARAMS + ";" + gui.getTickinterval() + ";" + gui.getTargetHost() + ";"
					+ gui.getTargetPort()));
			break;
		case STARTATTACK:
			doAttack = true;
			addBehaviour(new SendSubCoordinatorMessage(this.STARTATTACK + ";"));
			break;
		case STOPATTACK:
			doAttack = false;
			addBehaviour(new SendSubCoordinatorMessage(this.STOPATTACK + ";"));
			break;
		}
	}

	/**
	 * Sends a ACLMessage to all registered Sub-Coordinators.
	 */
	public class SendSubCoordinatorMessage extends OneShotBehaviour {

		String content = "";

		/**
		 * Sets the message content.
		 * 
		 * @param cntnt
		 */
		public SendSubCoordinatorMessage(String cntnt) {
			this.content = cntnt;
		}

		/**
		 * Gets all receivers from the GUI and sends the message.
		 */
		public void action() {
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			Enumeration<String> receivers = gui.getSubCoordinators();
			while (receivers.hasMoreElements()) {
				msg.addReceiver(new AID(receivers.nextElement(), AID.ISLOCALNAME));
			}
			msg.setLanguage("English");
			msg.setContent(this.content);
			send(msg);
			System.out.println("****I Sent a Message to all SubCoordinators. *****" + "\n" + "The Content of My Message is::>" + msg.getContent());
		}
	}

	/**
	 * Detects when a new Sub-Coordinator registers to the DF.
	 */
	public class ReceiverAgentSubscription extends SubscriptionInitiator {

		public ReceiverAgentSubscription(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		/**
		 * Adds a Sub-Coordinator to the GUI list when discovered.
		 */
		protected void handleInform(ACLMessage inform) {
			try {
				DFAgentDescription[] results = DFService.decodeNotification(inform.getContent());
				if (results.length > 0) {
					for (int i = 0; i < results.length; ++i) {
						AID provider = results[i].getName();
						gui.addReceiver(provider.getLocalName());
						System.out.println("Sub-Coordinator " + provider.getLocalName() + " discovered.");
					}
				}
			} catch (FIPAException fe) {
				fe.printStackTrace();
			}
		}
	}

	/**
	 * Discovers all agents of a certain type registered to the platform.
	 * 
	 */
	public class DiscoverAgents extends TickerBehaviour {

		/**
		 * The agent type to look for.
		 */
		String agentType;

		public DiscoverAgents(GuiAgent agent, long p, String at) {
			super(agent, p);
			agentType = at;
		}

		/**
		 * Send request to the DF to ask for registered Agents of a certain
		 * type.
		 */
		public void onTick() {
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription templateSd = new ServiceDescription();
			templateSd.setType(agentType);
			template.addServices(templateSd);
			DFAgentDescription[] results = null;
			SearchConstraints sc = new SearchConstraints();
			sc.setMaxResults(new Long(-1));

			try {
				results = DFService.search(this.myAgent, template, sc);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (results.length > 0) {
				if (agentType.equals("Smith")) {
					// System.out.println("Total Smiths: " + results.length);
					gui.setAmountOfRunningSmiths(results.length);
				}
				if (agentType.equals("SubCoordinator")) {
					List<String> subs = new ArrayList<String>();
					for (int i = 0; i < results.length; ++i) {
						DFAgentDescription dfd = results[i];
						AID provider = dfd.getName();
						subs.add(provider.getLocalName());
						System.out.println("Sub-Coordinator " + provider.getLocalName() + " discovered.");
					}
					gui.updateListValues(subs);

				}
			}
		}
	}
}
