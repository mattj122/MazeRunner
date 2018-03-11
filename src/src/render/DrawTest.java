package render;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import objects.Maze;

public class DrawTest {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				createAndShowGUI();
			}

			private void createAndShowGUI() {
					//System.out.println("Create and Show GUI? " + SwingUtilities.isEventDispatchThread());
					JFrame f = new JFrame();
					f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					f.getContentPane().add(new RenderFrame());
					f.setTitle("Draw Test");
					f.pack();
					f.setVisible(true);
			}
		});
	}
	@SuppressWarnings("serial")
	public static class RenderFrame extends JPanel{
		final int GRID_CELL_SIZE = 20;
		Maze m = new Maze(30, 20);
		public RenderFrame() {
			setBorder(BorderFactory.createLineBorder(Color.BLACK));
			setBackground(Color.WHITE);
		}
		public Dimension getPreferredSize() {
			return new Dimension(960, 540);
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Color cellOutline = new Color(230, 230, 230);
			g.setColor(Color.BLACK);
			g.setColor(cellOutline);
			for(int i = 0; i < this.getWidth() / GRID_CELL_SIZE; i ++) {
				for(int j = 0; j < this.getHeight() / GRID_CELL_SIZE; j++) {
					g.drawRect(i * GRID_CELL_SIZE, j * GRID_CELL_SIZE, GRID_CELL_SIZE, GRID_CELL_SIZE);
				}
			}
			setDummyMaze(m);
			for(int i = 0; i < m.width; i ++) {
				for(int j = 1; j < m.height; j++) {
					g.setColor(new Color(150, 150, 150));
					g.fillRect((i+2) * GRID_CELL_SIZE, j * GRID_CELL_SIZE, GRID_CELL_SIZE, GRID_CELL_SIZE);
					if(m.get(i, j).getType() == 0){
						g.setColor(new Color(20, 20, 20));
					}
					g.drawRect((i+2) * GRID_CELL_SIZE, j * GRID_CELL_SIZE, GRID_CELL_SIZE, GRID_CELL_SIZE);
					g.setColor(Color.WHITE);
					g.drawString(Integer.toString(m.get(i, j-1).getType()), (i+2) * GRID_CELL_SIZE + 2, j * GRID_CELL_SIZE + 12);
				}
			}
			
			//Image displaying
			
		}
		private void setDummyMaze(Maze m) {
			try {
				m.setCell(0, 0, 8);
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
