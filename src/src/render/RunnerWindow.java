package render;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import objects.Maze;
import objects.OutConsole;

public class RunnerWindow {

	private JFrame frmDisplayWindow;
	public boolean paused = false;
	public int startX, startY;
	private String fileName = "dummy_2";
	static JTextPane consoleData = new JTextPane();
	static OutConsole mainConsole = new OutConsole();
	static JTextPane aIConsole = new JTextPane();
	static Agent ai;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					RunnerWindow window = new RunnerWindow();
					window.frmDisplayWindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RunnerWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		consoleData.setFont(new Font("Courier New", Font.PLAIN, 14));
		consoleData.setEditable(false);
		aIConsole.setFont(new Font("Courier New", Font.PLAIN, 14));
		aIConsole.setEditable(false);
		
		Maze maze = new Maze(fileName, "./.mazerunner/maze_save/");
		RenderPanel renderPanel = new RenderPanel(maze);
		ai = renderPanel.getAI();
		
		//Button declarations
		JButton btnStart = new JButton("Start Maze Run");
		JButton btnPause = new JButton("Pause Maze Run");
		JButton btnStartOver = new JButton("Start Over");
		JButton moveRt = new JButton("Move agent right");
		JButton moveUp = new JButton("Move agent up");
		JButton moveDn = new JButton("Move agent down");
		JButton moveLt = new JButton("Move agent left");
		
		//Button Initialization
		btnStartOver.setEnabled(false);
		btnPause.setEnabled(false);
		
		//Menus
		JMenuBar menuBar = new JMenuBar();
		//Agent
		JMenu mnAgent = new JMenu("Agent");
		JMenuItem mntmLoadAgentData = new JMenuItem("Load Agent Data...");
		JMenuItem mntmClearAgentData = new JMenuItem("Clear Agent Data");
		//Maze
		JMenu mnMaze = new JMenu("Maze");
		JMenuItem mntmLoadMaze = new JMenuItem("Load Maze...");
		JMenuItem mntmSaveMaze = new JMenuItem("Save Maze...");
		JMenuItem mntmGenerateMaze = new JMenuItem("Generate Maze...");
		JMenuItem mntmMazeOptions = new JMenuItem("Maze Options");
		JMenuItem mntmEditMaze = new JMenuItem("Edit Maze");
		
		//Menu Action Listeners
		//Agent
		mntmLoadAgentData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainConsole.add("Load Agent Data Selected");
				updateConsoles();
			}
		});
		mntmClearAgentData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainConsole.add("Clear Agent Data Selected");
				updateConsoles();
			}
		});
		//Maze
		mntmLoadMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainConsole.add("Load Maze Data Selected");
				updateConsoles();
			}
		});
		mntmSaveMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainConsole.add("Save Maze Data Selected");
				updateConsoles();
			}
		});
		mntmGenerateMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainConsole.add("Generate Maze Selected");
				updateConsoles();
			}
		});
		mntmMazeOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainConsole.add("Maze Options Selected");
				updateConsoles();
			}
		});
		mntmEditMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainConsole.add("Edit Maze Selected");
				updateConsoles();
			}
		});
		
		//Add Menu Items
		menuBar.add(mnAgent);
		mnAgent.add(mntmLoadAgentData);
		mnAgent.add(mntmClearAgentData);
		menuBar.add(mnMaze);
		mnMaze.add(mntmLoadMaze);
		mnMaze.add(mntmSaveMaze);
		mnMaze.add(mntmGenerateMaze);
		mnMaze.add(mntmMazeOptions);
		mnMaze.add(mntmEditMaze);
		
		//Label declarations
		JLabel lblNewLabel = new JLabel("AI Console");
		
		String[] speedArr = {"0.1x", "0.25x", "0.5x", "1.0x", "2.0x"};
		String[] ruleArr = {"Keep Right", "Keep Left", "Keep Straight", "Random Direction", "Probability from data"};
		
		frmDisplayWindow = new JFrame();
		frmDisplayWindow.setTitle("MazeRunner Display Window");
		frmDisplayWindow.setBounds(10, 10, 960, 540);
		frmDisplayWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDisplayWindow.setResizable(false);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {640, 320, 320};
		gridBagLayout.rowHeights = new int[] {480, 30, 210};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, 1.0};
		frmDisplayWindow.getContentPane().setLayout(gridBagLayout);
		frmDisplayWindow.setJMenuBar(menuBar);
		
		
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
				btnStartOver.setEnabled(true);
				btnPause.setEnabled(true);
				mainConsole.add("Starting maze run");
				updateConsoles();
			}
		});
		ctrlPanel.add(btnStart);
		
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(paused) {
					btnPause.setText("Pause Maze Run");
					mainConsole.add("Resuming");
					updateConsoles();
				}
				else {
					btnPause.setText("Unpause Maze Run");
					mainConsole.add("Pausing");
					updateConsoles();
				}
				paused = !paused;
			}
		});
		ctrlPanel.add(btnPause);
		
		btnStartOver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnStart.setEnabled(true);
				btnStartOver.setEnabled(false);
				paused = false;
				btnPause.setText("Pause Maze Run");
				btnPause.setEnabled(false);
				mainConsole.add("Starting over");
				updateConsoles();
			}
		});
		ctrlPanel.add(btnStartOver);
		
		JComboBox comboBox = new JComboBox(speedArr);
		ctrlPanel.add(comboBox);
		
		JComboBox comboBox_1 = new JComboBox(ruleArr);
		ctrlPanel.add(comboBox_1);
		
		moveRt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					ai.move(1);
				} catch (Exception e) {
					ai.log(e.getMessage());
				}
				//updateConsoles();
				renderPanel.update();
			}
		});
		moveUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					ai.move(4);
				} catch (Exception e) {
					ai.log(e.getMessage());
				}
				//updateConsoles();
				renderPanel.update();
			}
		});
		moveDn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					ai.move(2);
				} catch (Exception e) {
					ai.log(e.getMessage());
				}
				//updateConsoles();
				renderPanel.update();
			}
		});
		moveLt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					ai.move(3);
				} catch (Exception e) {
					ai.log(e.getMessage());
				}
				//updateConsoles();
				renderPanel.update();
			}
		});
		ctrlPanel.add(moveRt);
		ctrlPanel.add(moveUp);
		ctrlPanel.add(moveDn);
		ctrlPanel.add(moveLt);
		
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
	public static void updateConsoles() {
		consoleData.setText(mainConsole.toString());
		aIConsole.setText(ai.getCons().toString());
	}
}
