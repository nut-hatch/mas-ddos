package lab4;

import jade.core.Agent;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.AbstractButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JToggleButton;

public class GUI {

	private CoordinatorAgent agent;
	private JFrame frmAgentCommunication;
	private JList receiverList;
	private DefaultListModel listModelReceiver = new DefaultListModel<>();
	private DefaultComboBoxModel comboModelPerformative = new DefaultComboBoxModel(ACLMessage.getAllPerformativeNames());
	private JTextField txtTickinterval;
	private JTextField txtTargethost;
	private JTextField txtTargetport;
	private JLabel lblNumberofsubcoordinators = null;
	private JTextField txtNumberofagents;
	private JToggleButton tglbtnAttack = null;
	JLabel lblRunningsmiths;

	/**
	 * Create the application.
	 */
	public GUI(CoordinatorAgent agent) {
		this.agent = agent;
		initialize();
//		listModelReceiver.addElement("R");
//		listModelReceiver.addElement("T");
		this.frmAgentCommunication.setVisible(true);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAgentCommunication = new JFrame();
		frmAgentCommunication.setTitle("The Rise Of Agents");
		frmAgentCommunication.setBounds(100, 100, 538, 404);
		frmAgentCommunication.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmAgentCommunication.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mntmClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mnFile.add(mntmClose);
		
		JMenu mnTools = new JMenu("Tools");
		mnTools.setEnabled(false);
		menuBar.add(mnTools);
		
		JMenuItem mntmCreateReceivers = new JMenuItem("Create Receivers");
		mntmCreateReceivers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LaunchSmithsDialog dialog = new LaunchSmithsDialog(agent);
			    dialog.setModal(true);
			    dialog.setVisible(true);
			}
		});
		mnTools.add(mntmCreateReceivers);
		
		JLabel lblReceiver = new JLabel("Sub Coordinators");
		
		JScrollPane scrollPane = new JScrollPane();
		
		JLabel lblAgntName = new JLabel("Coordinator Agent:");
		
		JLabel lblAgentname = new JLabel(this.agent.getAID().getLocalName());
		
		JLabel lblAmountOfAgents = new JLabel("No. Agents");
		
		JLabel lblTickInterval = new JLabel("Tick interval");
		
		txtTickinterval = new JTextField();
		txtTickinterval.setText("5000");
		txtTickinterval.setColumns(10);
		
		JLabel lblTargetHost = new JLabel("Target host");
		
		txtTargethost = new JTextField();
		txtTargethost.setColumns(10);
		
		JLabel lblTargetPort = new JLabel("Target port");
		
		txtTargetport = new JTextField();
		txtTargetport.setText("4050");
		txtTargetport.setColumns(10);
		
		JButton btnUpdateAmountOfAgents = new JButton("Add Agents");
		btnUpdateAmountOfAgents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GuiEvent ge = new GuiEvent(this, agent.ADDAGENT);
	            agent.postGuiEvent(ge);
			}
		});
		
		JLabel lblSummary = new JLabel("Attack Summary");
		
		JLabel lblNumberOfAttacking = new JLabel("No. of Sub-Coordinators:");
		
		JLabel lblNoOfSmith = new JLabel("Running Smith Agents:");
		
		JLabel lblAttackConfiguration = new JLabel("Attack Configuration");
		
		JButton btnSetArguments = new JButton("Update Params");
		btnSetArguments.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GuiEvent ge = new GuiEvent(this, agent.UPDATEPARAMS);
	            agent.postGuiEvent(ge);
			}
		});
		
		tglbtnAttack = new JToggleButton("Start Attack");
		tglbtnAttack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean selected = tglbtnAttack.getModel().isSelected();
				if (selected) {
					tglbtnAttack.setText("Stop Attack");
					GuiEvent ge = new GuiEvent(this, agent.STARTATTACK);
		            agent.postGuiEvent(ge);
				} else {
					tglbtnAttack.setText("Start Attack");
					GuiEvent ge = new GuiEvent(this, agent.STOPATTACK);
		            agent.postGuiEvent(ge);
				}
			}
		});
		
		lblNumberofsubcoordinators = new JLabel("0");
		
		txtNumberofagents = new JTextField();
		txtNumberofagents.setText("2");
		txtNumberofagents.setColumns(10);
		
		lblRunningsmiths = new JLabel("0");
		GroupLayout groupLayout = new GroupLayout(frmAgentCommunication.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblReceiver))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblAttackConfiguration)
						.addComponent(lblSummary)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(12)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblNoOfSmith)
									.addGap(18)
									.addComponent(lblRunningsmiths))
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
									.addGroup(groupLayout.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
											.addComponent(tglbtnAttack, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
											.addGroup(groupLayout.createSequentialGroup()
												.addComponent(btnUpdateAmountOfAgents, GroupLayout.PREFERRED_SIZE, 139, GroupLayout.PREFERRED_SIZE)
												.addGap(18)
												.addComponent(btnSetArguments))
											.addGroup(groupLayout.createSequentialGroup()
												.addComponent(lblTickInterval)
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(txtTickinterval))
											.addGroup(groupLayout.createSequentialGroup()
												.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
													.addComponent(lblTargetHost)
													.addComponent(lblTargetPort))
												.addGap(20)
												.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
													.addComponent(txtTargetport, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
													.addComponent(txtTargethost, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)))))
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(lblAgntName)
										.addGap(18)
										.addComponent(lblAgentname))
									.addGroup(groupLayout.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(lblAmountOfAgents)
										.addGap(25)
										.addComponent(txtNumberofagents, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE))
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(lblNumberOfAttacking)
										.addGap(18)
										.addComponent(lblNumberofsubcoordinators, GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE))))))
					.addGap(76))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(33)
							.addComponent(lblReceiver)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblSummary)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblAgntName)
								.addComponent(lblAgentname))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNumberOfAttacking)
								.addComponent(lblNumberofsubcoordinators, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE))
							.addGap(8)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNoOfSmith)
								.addComponent(lblRunningsmiths))
							.addGap(17)
							.addComponent(lblAttackConfiguration)
							.addGap(12)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblAmountOfAgents)
								.addComponent(txtNumberofagents, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(24)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblTargetHost)
								.addComponent(txtTargethost, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(8)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblTargetPort)
								.addComponent(txtTargetport, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(10)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblTickInterval)
								.addComponent(txtTickinterval, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(17)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnUpdateAmountOfAgents)
								.addComponent(btnSetArguments))
							.addGap(10)
							.addComponent(tglbtnAttack)))
					.addContainerGap())
		);
		
		receiverList = new JList(listModelReceiver);
		scrollPane.setViewportView(receiverList);
		frmAgentCommunication.getContentPane().setLayout(groupLayout);
		
//		performativeComboBox.addItem();
	}
	
    public String getAmountOfAgents() {
    	if (txtNumberofagents.getText().equals("")) {
    		return "0";
    	}
        return txtNumberofagents.getText();
    }
    
    public String getSubCoordinator() {
        return receiverList.getSelectedValue().toString();
    }
    
    public Enumeration<String> getSubCoordinators() {
    	return listModelReceiver.elements();
    }

    public String getTargetHost() {
    	return txtTargethost.getText();
    }
    
    public String getTargetPort() {
    	return txtTargetport.getText();
    }
    
    public String getTickinterval() {
    	return txtTickinterval.getText();
    }
    
    public void addReceiver(String rcv) {
    	System.out.println("ADD:" + rcv);
    	listModelReceiver.addElement(rcv);
    	lblNumberofsubcoordinators.setText(Integer.toString(listModelReceiver.getSize()));
    }
    
    public String getNumberOfSubCoordinators() {
    	System.out.println("nosub:" + lblNumberofsubcoordinators.getText());
    	return lblNumberofsubcoordinators.getText();
    }

    public void setAmountOfRunningSmiths(int j) {
    	lblRunningsmiths.setText(Integer.toString(j));
    }
    
    public void updateListValues(List<String> strList) {
    	for (Object element : listModelReceiver.toArray()) {
			if (!strList.contains(element.toString())) {
				listModelReceiver.removeElement(element);
			}
		}
    }
}
