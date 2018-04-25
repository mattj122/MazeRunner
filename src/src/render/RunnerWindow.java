package render;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import objects.Maze;
import objects.OutConsole;

public class RunnerWindow {

	protected JFrame frmDisplayWindow;
	public boolean paused = false;
	public int startX, startY;
	private String fileName, fileDir;
	static JTextPane consoleData = new JTextPane();
	static OutConsole mainConsole = new OutConsole();
	static JTextPane aIConsole = new JTextPane();
	static Agent ai;

	/**
	 * Create the application.
	 */
	public RunnerWindow(File f) {
		fileDir = ".\\.mazerunner\\" + f.getParentFile().getName();
		fileName = f.getName();
		initialize(f);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initialize(File f) {
		consoleData.setFont(new Font("Courier New", Font.PLAIN, 14));
		consoleData.setEditable(false);
		aIConsole.setFont(new Font("Courier New", Font.PLAIN, 14));
		aIConsole.setEditable(false);
		
		Maze maze = new Maze(fileName, fileDir);
		RenderPanel renderPanel = new RenderPanel(maze);
		ai = renderPanel.getAI();
		
		//Button declarations
		JButton btnStart = new JButton("Start Maze Run");
		JButton btnStartOver = new JButton("Start Over");
		
		//Label declarations
		JLabel lblNewLabel = new JLabel("AI Console");
		
		String[] speedArr = {"0.1x", "0.25x", "0.5x", "1.0x", "2x", "5x", "Instant"};
		String[] ruleArr = {"Probability from data", "Random Direction"};
		
		frmDisplayWindow = new JFrame();
		frmDisplayWindow.setTitle("MazeRunner Display Window");
		frmDisplayWindow.setBounds(10, 10, 960, 540);
		frmDisplayWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmDisplayWindow.setResizable(false);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {640, 320, 320};
		gridBagLayout.rowHeights = new int[] {480, 30, 210};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, 1.0};
		frmDisplayWindow.getContentPane().setLayout(gridBagLayout);
		
		
		startX = renderPanel.mazeStartX - 1;
		startY = renderPanel.mazeStartY + renderPanel.getMaze().startPos;
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.weighty = 1.0;
		gbc_panel.weightx = 1.0;
		gbc_panel.gridwidth = 2;
		gbc_panel.insets = new Insets(5, 5, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		frmDisplayWindow.getContentPane().add(renderPanel, gbc_panel);
		
		JPanel ctrlPanel = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(5, 0, 5, 5);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 2;
		gbc_panel_1.gridy = 0;
		frmDisplayWindow.getContentPane().add(ctrlPanel, gbc_panel_1);
		ctrlPanel.setLayout(new GridLayout(10, 0, 0, 0));
		
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnStart.setEnabled(false);
				mainConsole.add("Starting maze run");
				try {
					ai.move(1);
				} catch (Exception e) {
					ai.log(e.getMessage());
				}
				if(ai.finished) {
					mainConsole.add("Maze Completed. ");
				}
				btnStartOver.setEnabled(true);
				renderPanel.update();
				updateConsoles();
			}
		});
		ctrlPanel.add(btnStart);
		
		JComboBox speedCombo = new JComboBox(speedArr);
		speedCombo.setSelectedIndex(5);
		setSpeed((String)(speedCombo.getSelectedItem()));
		speedCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(speedCombo.getSelectedIndex() == 6) {
					mainConsole.add("Setting speed to instant. ");
					ai.updateDelayFactor(0);
					updateConsoles();
				}
				else {
					setSpeed((String)(speedCombo.getSelectedItem()));
				}
			}
		});
		
		
		btnStartOver.setEnabled(false);
		btnStartOver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							RunnerWindow window = new RunnerWindow(f);
							window.frmDisplayWindow.setVisible(true);
							frmDisplayWindow.dispose();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		ctrlPanel.add(btnStartOver);
		
		JButton btnSelectADifferent = new JButton("Select a different Maze");
		btnSelectADifferent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							StartPopup window = new StartPopup();
							window.frame.setVisible(true);
							frmDisplayWindow.dispose();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		ctrlPanel.add(btnSelectADifferent);
		ctrlPanel.add(speedCombo);
		
		JComboBox ruleCombo = new JComboBox(ruleArr);
		ruleCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainConsole.add("Setting rule to " + ruleCombo.getSelectedItem());
				ai.setRule(ruleCombo.getSelectedIndex() + 1);
				updateConsoles();
			}
		});
		ctrlPanel.add(ruleCombo);
		
		JLabel lblConsoleData = new JLabel("Main Console");
		lblConsoleData.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_lblConsoleData = new GridBagConstraints();
		gbc_lblConsoleData.weighty = 1.0;
		gbc_lblConsoleData.weightx = 1.0;
		gbc_lblConsoleData.fill = GridBagConstraints.VERTICAL;
		gbc_lblConsoleData.insets = new Insets(0, 0, 5, 5);
		gbc_lblConsoleData.gridx = 0;
		gbc_lblConsoleData.gridy = 1;
		frmDisplayWindow.getContentPane().add(lblConsoleData, gbc_lblConsoleData);
		
		//AI Label
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.weighty = 1.0;
		gbc_lblNewLabel.weightx = 1.0;
		gbc_lblNewLabel.gridwidth = 2;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 1;
		frmDisplayWindow.getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		JScrollPane jsp_consoleData = new JScrollPane(consoleData);
		GridBagConstraints gbc_consoleData = new GridBagConstraints();
		gbc_consoleData.weighty = 1.0;
		gbc_consoleData.weightx = 1.0;
		gbc_consoleData.insets = new Insets(0, 5, 5, 2);
		gbc_consoleData.fill = GridBagConstraints.BOTH;
		gbc_consoleData.gridx = 0;
		gbc_consoleData.gridy = 2;
		frmDisplayWindow.getContentPane().add(jsp_consoleData, gbc_consoleData);
		
		JScrollPane jsp_consoleAI = new JScrollPane(aIConsole);
		GridBagConstraints gbc_consoleAI = new GridBagConstraints();
		gbc_consoleAI.gridwidth = 2;
		gbc_consoleAI.insets = new Insets(0, 2, 5, 5);
		gbc_consoleAI.fill = GridBagConstraints.BOTH;
		gbc_consoleAI.gridx = 1;
		gbc_consoleAI.gridy = 2;
		frmDisplayWindow.getContentPane().add(jsp_consoleAI, gbc_consoleAI);
		frmDisplayWindow.pack();
	}
	public void setSpeed (String str) {
		double fac = 1.0 / Double.parseDouble(str.substring(0, str.indexOf("x")));
		mainConsole.add("Setting speed to " + str + ". ");
		ai.updateDelayFactor(fac);
		updateConsoles();
	}
	public static void updateConsoles() {
		consoleData.setText(mainConsole.toString());
		aIConsole.setText(ai.getCons().toString());
	}
}
