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
	int width = 1280;
	int height = 720;
	Maze m;
	public RenderPanel(Maze inMaze) {
		m = inMaze;
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setBackground(Color.WHITE);
		cellSize = Math.min(width, height) / (Math.max(m.width + 4, m.height + 4));
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
				g.drawRect((i * cellSize)-2, (j * cellSize)-2, 4, 4);
			}
		}
		
		double scale = cellSize / 64.0;
		for(int i = 0; i < m.width; i ++) {
			for(int j = 0; j < m.height; j++) {
				//g.setColor(new Color(150, 150, 150));
				g.drawImage(m.get(i, j).getImage(), ((i+2)*cellSize), ((j + 2)*cellSize), (int)(64 * scale), (int)(64 * scale), this);
			}
		}
	}
}
