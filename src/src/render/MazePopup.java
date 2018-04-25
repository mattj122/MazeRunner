package render;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.JScrollPane;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import java.awt.GridBagConstraints;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Insets;
import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MazePopup extends JFrame {

	private JPanel contentPane;
	private ArrayList<File> mazes = new ArrayList<File>();
	private final File MAZE_DIR = new File("./.mazerunner/maze_save"); 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MazePopup frame = new MazePopup();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public void showError(String str) {JOptionPane.showMessageDialog(null, str);}
	public MazePopup() {
		try {
			loadMazes(MAZE_DIR);
		}
		catch(Exception e) {
			showError(e.getMessage());
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JComboBox comboBox = new JComboBox();
		String [] mazeList = new String [mazes.size()];
		for(int i = 0; i < mazes.size(); i++) {
			mazeList[i] = mazes.get(i).getName();
		}
		System.out.println(mazes.size());
		comboBox.setModel(new DefaultComboBoxModel(mazeList));
		comboBox.setSelectedIndex(0);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.gridwidth = 2;
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 0;
		gbc_comboBox.gridy = 0;
		contentPane.add(comboBox, gbc_comboBox);
		
		JLabel lblWidth = new JLabel("Width: ");
		GridBagConstraints gbc_lblWidth = new GridBagConstraints();
		gbc_lblWidth.insets = new Insets(0, 0, 5, 5);
		gbc_lblWidth.gridx = 0;
		gbc_lblWidth.gridy = 2;
		contentPane.add(lblWidth, gbc_lblWidth);
		
		JLabel lblHeight = new JLabel("Height: ");
		GridBagConstraints gbc_lblHeight = new GridBagConstraints();
		gbc_lblHeight.insets = new Insets(0, 0, 5, 5);
		gbc_lblHeight.gridx = 0;
		gbc_lblHeight.gridy = 3;
		contentPane.add(lblHeight, gbc_lblHeight);
		
		JButton btnSelectMaze = new JButton("Select Maze");
		btnSelectMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							RunnerWindow window = new RunnerWindow(mazes.get(comboBox.getSelectedIndex()));
							window.frmDisplayWindow.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		GridBagConstraints gbc_btnSelectMaze = new GridBagConstraints();
		gbc_btnSelectMaze.insets = new Insets(0, 0, 0, 5);
		gbc_btnSelectMaze.gridx = 0;
		gbc_btnSelectMaze.gridy = 8;
		contentPane.add(btnSelectMaze, gbc_btnSelectMaze);
		
		JButton btnClose = new JButton("Close");
		GridBagConstraints gbc_btnClose = new GridBagConstraints();
		gbc_btnClose.gridx = 1;
		gbc_btnClose.gridy = 8;
		contentPane.add(btnClose, gbc_btnClose);
	}

	private void loadMazes(File dir) throws IOException {
		Properties propTemp = new Properties(); 
		File [] list = dir.listFiles();
		if(dir.isDirectory()) {
			System.out.println("is dir");
			System.out.println(list.length);
			for(int i = 0; i < list.length; i ++) {
				FileInputStream inStream = new FileInputStream(list[i]);
				propTemp.load(inStream);
				inStream.close();
				String extension = list[i].getName().substring(list[i].getName().lastIndexOf('.'), list[i].getName().length());
				if(extension.equalsIgnoreCase(".ini")) {
					if(propTemp.getProperty("type").substring(0, 1).equalsIgnoreCase("m")) {
						mazes.add(list[i]);
					}
				}
			}
		}
	}

}
