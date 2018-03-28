package render;

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
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.getContentPane().add(new RenderPanel(m));
				f.setTitle("Draw Test");
				f.pack();
				f.setVisible(true);
			}
			private Maze dummyMaze() {
				return new Maze("dummy", "./.mazerunner/maze_save/");
			}
		});
	}
}
