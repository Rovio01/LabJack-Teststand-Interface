package co.jensrud.sac.teststandclient;

import com.sun.jmx.snmp.tasks.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class GUI extends JFrame implements ActionListener {
	JLabel connection_status, arm_status;
	JButton ignition_button, arm_button, disarm_button;
	JToggleButton record_toggle;


	GUI(String title) {
		super(title);
		setSize(300,300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setLayout(new FlowLayout());
		connection_status = new JLabel("Disconnected");
		add(connection_status);

		arm_status = new JLabel("Disarmed");

		ignition_button = new JButton("FIRE");
		ignition_button.setHorizontalTextPosition(AbstractButton.CENTER);
		ignition_button.setActionCommand("fire");
		ignition_button.setEnabled(false);

		arm_button = new JButton("ARM");
		arm_button.setHorizontalTextPosition(AbstractButton.CENTER);
		arm_button.setActionCommand("arm");

		disarm_button = new JButton("DISARM");
		disarm_button.setHorizontalTextPosition(AbstractButton.CENTER);
		disarm_button.setActionCommand("disarm");
		disarm_button.setEnabled(false);

		ignition_button.addActionListener(this);
		arm_button.addActionListener(this);
		disarm_button.addActionListener(this);

		add(arm_status);
		add(ignition_button);
		add(arm_button);
		add(disarm_button);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Action Performed: " + e.getActionCommand());
		if ("arm".equals(e.getActionCommand())) {
			arm_button.setEnabled(false);
			disarm_button.setEnabled(true);
			ignition_button.setEnabled(true);
		} else if ("disarm".equals(e.getActionCommand())){
			arm_button.setEnabled(true);
			disarm_button.setEnabled(false);
			ignition_button.setEnabled(false);
		} else if ("fire".equals(e.getActionCommand())) {
			Main.setFiringPin(true);
			ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
			executorService.schedule(new Task() {
				@Override
				public void cancel() {

				}

				@Override
				public void run() {
					Main.setFiringPin(false);
				}
			}, 10, TimeUnit.SECONDS);
		}
	}

	public void setConnectionStatus(String status) {
		connection_status.setText(status);
	}
}
