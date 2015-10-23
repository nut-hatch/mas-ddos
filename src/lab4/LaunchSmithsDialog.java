package lab4;

import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LaunchSmithsDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtPlatformaddress;
	private JTextField txtServeraddress;
	private JTextField txtNumberofagents;
	private JTextField txtTimeinterval;
	private JTextField txtPlatformport;
	private GuiAgent agent;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		try {
//			LaunchSmithsDialog dialog = new LaunchSmithsDialog();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public LaunchSmithsDialog(GuiAgent agnt) {
		this.agent = agnt;
		setTitle("Send Smiths");
		setBounds(100, 100, 450, 280);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		JLabel lblPlatformAddress = new JLabel("Platform Address");
		JLabel lblServerAddress = new JLabel("Server Address");
		JLabel lblNoOfSmiths = new JLabel("No. of Smiths");
		JLabel lblTimeInterval = new JLabel("Time Interval");
		txtPlatformaddress = new JTextField();
		txtPlatformaddress.setText("192.168.1.26");
		txtPlatformaddress.setColumns(10);
		txtServeraddress = new JTextField();
		txtServeraddress.setColumns(10);
		txtNumberofagents = new JTextField();
		txtNumberofagents.setColumns(10);
		txtTimeinterval = new JTextField();
		txtTimeinterval.setColumns(10);
		
		JLabel lblPlatformPort = new JLabel("Platform Port");
		
		txtPlatformport = new JTextField();
		txtPlatformport.setText("1099");
		txtPlatformport.setColumns(10);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblPlatformAddress)
						.addComponent(lblPlatformPort)
						.addComponent(lblServerAddress)
						.addComponent(lblNoOfSmiths)
						.addComponent(lblTimeInterval))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
							.addComponent(txtTimeinterval, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(txtNumberofagents, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(txtServeraddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
							.addComponent(txtPlatformport, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(txtPlatformaddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(178, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPlatformAddress)
						.addComponent(txtPlatformaddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPlatformPort)
						.addComponent(txtPlatformport, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblServerAddress)
						.addComponent(txtServeraddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNoOfSmiths)
						.addComponent(txtNumberofagents, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTimeInterval)
						.addComponent(txtTimeinterval, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(29, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						GuiEvent ge = new GuiEvent(this, 1);

				            agent.postGuiEvent(ge);
				            
//				        AgentHelper ah = new AgentHelper(null);
//						ah.createRemoteAgent("lab4.AgentSmith", "Smith", "SmithContainer", txtPlatformaddress.getText(), Integer.parseInt(txtPlatformport.getText()), null);
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
