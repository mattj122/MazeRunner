package render;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class RunnerWindow {

	private JFrame frmMazerunnerDisplayWindow;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					RunnerWindow window = new RunnerWindow();
					window.frmMazerunnerDisplayWindow.setVisible(true);
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
		JTextPane consoleData = new JTextPane();
		JTextPane consoleAI = new JTextPane();
		//String[] speedArr = {"0.1x", "0.25x", "0.5x", "1.0x", "2.0x"};
		//String[] ruleArr = {"Keep Right", "Keep Left", "Keep Straight", "Random Direction", "Probability from data"};
		
		frmMazerunnerDisplayWindow = new JFrame();
		frmMazerunnerDisplayWindow.setTitle("MazeRunner Display Window");
		frmMazerunnerDisplayWindow.setBounds(100, 100, 1280, 720);
		frmMazerunnerDisplayWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMazerunnerDisplayWindow.setResizable(false);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {640, 320, 320};
		gridBagLayout.rowHeights = new int[] {480, 30, 210};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, 1.0};
		frmMazerunnerDisplayWindow.getContentPane().setLayout(gridBagLayout);
		
		
		RenderFrame renderPanel = new RenderFrame();
		//renderPanel.setBackground(Color.WHITE);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.weighty = 1.0;
		gbc_panel.weightx = 1.0;
		gbc_panel.gridwidth = 2;
		gbc_panel.insets = new Insets(5, 5, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		frmMazerunnerDisplayWindow.getContentPane().add(renderPanel, gbc_panel);
		
		JPanel ctrlPanel = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 2;
		gbc_panel_1.gridy = 0;
		frmMazerunnerDisplayWindow.getContentPane().add(ctrlPanel, gbc_panel_1);
		ctrlPanel.setLayout(new GridLayout(10, 0, 0, 0));
		
		JButton btnStartMazeRun = new JButton("Start Maze Run");
		ctrlPanel.add(btnStartMazeRun);
		
		JButton btnPauseMazeRun = new JButton("Pause Maze Run");
		ctrlPanel.add(btnPauseMazeRun);
		
		JButton btnStartOver = new JButton("Start Over");
		ctrlPanel.add(btnStartOver);
		
		JLabel lblConsoleData = new JLabel("Console Data");
		lblConsoleData.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_lblConsoleData = new GridBagConstraints();
		gbc_lblConsoleData.weighty = 1.0;
		gbc_lblConsoleData.weightx = 1.0;
		gbc_lblConsoleData.fill = GridBagConstraints.VERTICAL;
		gbc_lblConsoleData.insets = new Insets(0, 0, 5, 5);
		gbc_lblConsoleData.gridx = 0;
		gbc_lblConsoleData.gridy = 1;
		frmMazerunnerDisplayWindow.getContentPane().add(lblConsoleData, gbc_lblConsoleData);
		
		JLabel lblNewLabel = new JLabel("Console AI");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.weighty = 1.0;
		gbc_lblNewLabel.weightx = 1.0;
		gbc_lblNewLabel.gridwidth = 2;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 1;
		frmMazerunnerDisplayWindow.getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		JScrollPane jsp_consoleData = new JScrollPane(consoleData);
		GridBagConstraints gbc_consoleData = new GridBagConstraints();
		gbc_consoleData.weighty = 1.0;
		gbc_consoleData.weightx = 1.0;
		gbc_consoleData.insets = new Insets(0, 5, 5, 2);
		gbc_consoleData.fill = GridBagConstraints.BOTH;
		gbc_consoleData.gridx = 0;
		gbc_consoleData.gridy = 2;
		frmMazerunnerDisplayWindow.getContentPane().add(jsp_consoleData, gbc_consoleData);
		
		JScrollPane jsp_consoleAI = new JScrollPane(consoleAI);
		GridBagConstraints gbc_consoleAI = new GridBagConstraints();
		gbc_consoleAI.gridwidth = 2;
		gbc_consoleAI.insets = new Insets(0, 2, 5, 5);
		gbc_consoleAI.fill = GridBagConstraints.BOTH;
		gbc_consoleAI.gridx = 1;
		gbc_consoleAI.gridy = 2;
		frmMazerunnerDisplayWindow.getContentPane().add(jsp_consoleAI, gbc_consoleAI);
		frmMazerunnerDisplayWindow.pack();
	}
	
	@SuppressWarnings("serial")
	public static class RenderFrame extends JPanel{
		public RenderFrame() {
			setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			g.setColor(Color.BLACK);
			g.drawString("Render here", 10, 20);
		}
	}
}
