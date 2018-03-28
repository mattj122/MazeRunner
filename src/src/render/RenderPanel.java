package render;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import objects.Maze;

public class RenderPanel extends JPanel{
	int cellSize;
	int width = 960;
	int height = 540;
	Maze m;
	public RenderPanel(Maze inMaze) {
		m = inMaze;
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setBackground(Color.WHITE);
		cellSize = Math.min(width, height) / (Math.max(m.width + 2, m.height + 2));
	}
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		//g.drawString(Integer.toString(this.getWidth()), 10, 20);
		//g.drawString(Integer.toString(this.getHeight()), 10, 40);
		g.setColor(new Color(0, 0, 0));
		for(int i = 0; i < (this.getWidth() / cellSize) + 1; i ++) {
			for(int j = 0; j < (this.getHeight() / cellSize) + 2; j++) {
				g.drawRect(i * cellSize, j * cellSize, 1, 1);
			}
		}
		
		double scale = cellSize / 64.0;
		System.out.println(scale);
		System.out.println(cellSize);
		for(int i = 0; i < m.width; i ++) {
			for(int j = 0; j < m.height; j++) {
				//g.setColor(new Color(150, 150, 150));
				g.drawImage(m.get(i, j).getImage(), ((i+2)*cellSize), ((j + 2)*cellSize), (int)(64 * scale), (int)(64 * scale), this);
				g.setColor(Color.WHITE);
				g.drawString(Integer.toString(m.get(i, j).getType()), (i+2) * cellSize + 10, (j+2) * cellSize + 10);
				g.drawString("North: " + m.get(i, j).north, (i+2) * cellSize + 10, (j+2) * cellSize + 22);
				g.drawString("East: " + m.get(i, j).east, (i+2) * cellSize + 10, (j+2) * cellSize + 34);
				g.drawString("South: " + m.get(i, j).south, (i+2) * cellSize + 10, (j+2) * cellSize + 46);
				g.drawString("West: " + m.get(i, j).west, (i+2) * cellSize + 10, (j+2) * cellSize + 58);
			}
		}
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				createAndShowGUI();
			}

			private void createAndShowGUI() {
				JFrame f = new JFrame();
				Maze m;
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.getContentPane().add(new RenderPanel(new Maze("dummy", "./.mazerunner/maze_save/")));
				f.setTitle("Draw Test");
				f.pack();
				f.setVisible(true);
			}
			private void dummyMaze(Maze inMaze) {
				inMaze = new Maze(15, 10);
			}
		});
	}
}
