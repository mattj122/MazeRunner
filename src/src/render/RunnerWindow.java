package render;

import javax.swing.JApplet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

//import com.jgoodies.forms.layout.FormLayout;
//import com.jgoodies.forms.layout.ColumnSpec;
//import com.jgoodies.forms.layout.RowSpec;

//import objects.OutConsole;

import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Canvas;
import javax.swing.JScrollBar;

public class RunnerWindow extends JApplet {

	/**
	 * Create the applet.
	 */
	public RunnerWindow() {
		String[] speedArr = {"0.1x", "0.25x", "0.5x", "1.0x", "2.0x"};
		String[] ruleArr = {"Keep Right", "Keep Left", "Keep Straight", "Random Direction", "Probability from data"};
		JTextArea consoleAI = new JTextArea();
		
		//getContentPane().setSize(new Dimension(1280, 720));
		
		getContentPane().setBackground(Color.WHITE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {640, 320, 320};
		gridBagLayout.rowHeights = new int[] {540, 180};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0};
		getContentPane().setLayout(gridBagLayout);
		
		//OutConsole outC = new OutConsole();
		//outC.add("This is a test");
		//outC.add("Another Line");
		//outC.add("\tTabbed line");
		//outC.add("Final Line");
		
		JPanel renderPanel = new JPanel();
		
		GridBagConstraints gbc_renderPanel = new GridBagConstraints();
		gbc_renderPanel.gridwidth = 2;
		gbc_renderPanel.insets = new Insets(0, 0, 5, 5);
		gbc_renderPanel.fill = GridBagConstraints.BOTH;
		gbc_renderPanel.gridx = 0;
		gbc_renderPanel.gridy = 0;
		getContentPane().add(renderPanel, gbc_renderPanel);
		
		JPanel ctrlPanel = new JPanel();
		GridBagConstraints gbc_ctrlPanel = new GridBagConstraints();
		gbc_ctrlPanel.insets = new Insets(0, 0, 5, 0);
		gbc_ctrlPanel.fill = GridBagConstraints.BOTH;
		gbc_ctrlPanel.gridx = 2;
		gbc_ctrlPanel.gridy = 0;
		getContentPane().add(ctrlPanel, gbc_ctrlPanel);
		ctrlPanel.setLayout(new GridLayout(5, 0, 0, 0));
		
		JButton btnStartMazeRun = new JButton("Start Maze Run");
		ctrlPanel.add(btnStartMazeRun);
		
		JButton btnPause = new JButton("Pause");
		ctrlPanel.add(btnPause);
		
		JButton btnStartOver = new JButton("Start Over");
		ctrlPanel.add(btnStartOver);
		
		JButton btnConsoleUpdateTester = new JButton("Console Update Tester");
		btnConsoleUpdateTester.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//outC.add("\tAn updated line. ");
				//consoleAI.setText(outC.toString());
				//consoleAI.update(consoleAI.getGraphics());
			}
		});
		ctrlPanel.add(btnConsoleUpdateTester);
		
		JTextPane textPane = new JTextPane();
		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.insets = new Insets(0, 0, 0, 5);
		gbc_textPane.fill = GridBagConstraints.BOTH;
		gbc_textPane.gridx = 0;
		gbc_textPane.gridy = 1;
		getContentPane().add(textPane, gbc_textPane);
		
		
		consoleAI.setText("null");
		GridBagConstraints gbc_txtrDsafdg = new GridBagConstraints();
		gbc_txtrDsafdg.gridwidth = 2;
		gbc_txtrDsafdg.insets = new Insets(0, 0, 0, 5);
		gbc_txtrDsafdg.fill = GridBagConstraints.BOTH;
		gbc_txtrDsafdg.gridx = 1;
		gbc_txtrDsafdg.gridy = 1;
		getContentPane().add(consoleAI, gbc_txtrDsafdg);
		
		

	}
	public void paintComponent(Graphics g) {
		//super.paintComponents(g);
		
		g.setColor(Color.BLUE);
		g.drawRect(20, 20, 20, 20);
	}
	public void ExportConsole() {
	}

}
