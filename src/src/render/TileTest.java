package render;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import objects.Cell;
import objects.Maze;

public class TileTest {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				createAndShowGUI();
			}

			private void createAndShowGUI() {
				JFrame f = new JFrame();
				Maze m = dummyMaze();
				RenderPanel rPanel = new RenderPanel(m);
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.getContentPane().add(rPanel);
				f.setTitle("Debugging Rendering");
				f.pack();
				f.setVisible(true);
				if(!false) {
					JButton startBtn = new JButton("Start");
					startBtn.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							rPanel.setRunning(!rPanel.getRunning());
							rPanel.update();
						}
					});
					JButton pauseBtn = new JButton("Pause");
					JFrame buttons = new JFrame();
					buttons.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					buttons.getContentPane().setLayout(new GridLayout(2, 0, 0, 0));
					buttons.getContentPane().add(startBtn);
					buttons.getContentPane().add(pauseBtn);
					buttons.pack();
					buttons.setLocation(1300, 75);
					buttons.setVisible(true);
				}
			}
			private Maze dummyMaze() {
				return new Maze("dummy", "./.mazerunner/maze_save/");
			}
		});
	}
}
