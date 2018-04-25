package render;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class StartPopup {

	protected JFrame frame;
	private ArrayList<File> mazes = new ArrayList<File>();
	private final File MAZE_DIR = new File("./.mazerunner/maze_save");
	private int w, h;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartPopup window = new StartPopup();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public StartPopup() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialize() {
		try {
			loadMazes(MAZE_DIR);
			updateVals(0);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JLabel label = new JLabel("Width: " + w);
		JLabel label_1 = new JLabel("Height: " + h);
		frame = new JFrame();
		frame.setTitle("Select a maze...");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JComboBox comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					updateVals(comboBox.getSelectedIndex());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				label.setText("Width: " + w);
				label_1.setText("Height: " + h);
			}
		});
		String [] mazeList = new String [mazes.size()];
		for(int i = 0; i < mazes.size(); i++) {
			mazeList[i] = mazes.get(i).getName();
		}
		comboBox.setModel(new DefaultComboBoxModel(mazeList));
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 0;
		gbc_comboBox.gridy = 0;
		frame.getContentPane().add(comboBox, gbc_comboBox);
		
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 0);
		gbc_label.gridx = 0;
		gbc_label.gridy = 2;
		frame.getContentPane().add(label, gbc_label);
		
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 5, 0);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 3;
		frame.getContentPane().add(label_1, gbc_label_1);
		
		JButton btnSelectMaze = new JButton("Select Maze");
		btnSelectMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							RunnerWindow window = new RunnerWindow(mazes.get(comboBox.getSelectedIndex()));
							window.frmDisplayWindow.setVisible(true);
							frame.dispose();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 8;
		frame.getContentPane().add(btnSelectMaze, gbc_btnNewButton);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		GridBagConstraints gbc_btnClose = new GridBagConstraints();
		gbc_btnClose.gridx = 0;
		gbc_btnClose.gridy = 9;
		frame.getContentPane().add(btnClose, gbc_btnClose);
	}
	private void loadMazes(File dir) throws IOException {
		Properties propTemp = new Properties(); 
		File [] list = dir.listFiles();
		if(dir.isDirectory()) {
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
	private void updateVals(int index) throws Exception {
		Properties propTemp = new Properties(); 
		FileInputStream inStream = new FileInputStream(mazes.get(index));
		propTemp.load(inStream);
		inStream.close();
		w = Integer.parseInt(propTemp.getProperty("width"));
		h = Integer.parseInt(propTemp.getProperty("height"));
	}
}
