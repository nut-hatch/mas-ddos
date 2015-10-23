package lab4;

import jade.core.*;
import jade.domain.AMSService;
import jade.domain.DFService;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class AgentHelper {
	
	private Agent coordinater;
	
	public AgentHelper (Agent coordinaterAgent) {
		coordinater = coordinaterAgent;
	}
	
	
	public void createXAgents(String agentClass, int amountOfAgents, String basename, String containerName) {
		for (int i = 0; i < amountOfAgents; i++) {
			createAgent(agentClass, basename+"-"+i, containerName);
		}
	}
	
	public void createAgent(String agentClass, String agentName, String containerName) {
//		System.out.println(agentClass);
//		System.out.println(agentName);
//		System.out.println(containerName);
		//jade.core.Runtime rt = jade.core.Runtime.instance()();
//		ProfileImpl p = new ProfileImpl(false);
		//ProfileImpl rp = new ProfileImpl("", 1099, "agentAttack", false);
		//AgentContainer remoteContainer = rt.createAgentContainer(rp);
		try {
			// create agent t1 on the same container of the creator agent
			AgentContainer container = (AgentContainer)coordinater.getContainerController(); // get a container controller for creating new agents
			AgentController t1 = container.createNewAgent(agentName, agentClass, null);
			t1.start();
			System.out.println(coordinater.getLocalName()+" CREATED AND STARTED NEW RECEIVER:"+agentName + " ON CONTAINER "+container.getContainerName());
		} catch (Exception any) {
			any.printStackTrace();
		}
		
	}
	
	public void createRemoteAgent(String agentClass, String agentName, String containerName, String host, int port, Object[] args) {
		jade.core.Runtime rt = jade.core.Runtime.instance();
//		ProfileImpl p = new ProfileImpl(false);
		System.out.println(host);
		System.out.println(port);
		ProfileImpl rprofile = new ProfileImpl(host, port, null, true);
		//rp.setParameter("hostID", hostID);
		rprofile.setParameter(Profile.CONTAINER_NAME, containerName);

		Profile pClient = new ProfileImpl();
		pClient.setParameter(Profile.CONTAINER_NAME, containerName);
		pClient.setParameter(Profile.MAIN_HOST, "192.168.1.71");
		pClient.setParameter(Profile.LOCAL_HOST, "192.168.1.77");
		AgentContainer container = rt.createAgentContainer(pClient);
		/*rp.setParameter(Profile.MAIN_HOST, host);
		rp.setParameter(Profile.MAIN_PORT, Integer.toString(port));
		rp.setParameter(Profile.LOCAL_HOST, "192.168.1.32");
		rp.setParameter(Profile.LOCAL_PORT, "1099");
		rp.setParameter(Profile.CONTAINER_NAME, containerName);
		rp.setParameter(Profile.NO_MTP, "false");
		rp.setParameter(Profile.MTPS, "null");*/
		
		//AgentContainer remoteContainer = rt.createMainContainer(rprofile);
		try {
			AgentController remoteAgent = container.createNewAgent(agentName, agentClass, args);
			remoteAgent.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
