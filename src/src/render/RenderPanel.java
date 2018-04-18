package render;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import objects.Maze;

@SuppressWarnings("serial")
public class RenderPanel extends JPanel{
	private int cellSize = 64;
	private boolean running = false;
	private boolean pause = false;
	public int mazeStartY = 1, mazeStartX = 2;
	public int widthRes = 1280;
	public int heightRes = 720;
	//private int interval = 1;
	private short [][] grid = new short [30][30];
	private Maze m;
	private Agent a;
	private boolean divisions = true;
	public RenderPanel(Maze inMaze, Agent inAgent) {
		m = inMaze;
		a = inAgent;
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setBackground(Color.WHITE);
	}
	public Dimension getPreferredSize() {
		return new Dimension(widthRes, heightRes);
	}
	public static void showError(String str) {JOptionPane.showMessageDialog(null, str);}
	public void setRunning(boolean val) {
		running = val;
	}
	public boolean getRunning() {
		return running;
	}
	public boolean getPause() {
		return pause;
	}
	public void setPause(boolean val) {
		pause = val;
	}
	public void generateMaze() {
		m.generateMaze();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//Render grid
		for(int i = 0; i < grid.length; i ++) {
			for(int j = 0; j < grid[0].length; j++) {
				g.setColor(Color.BLACK);
				g.drawRect((i * cellSize) - 2, (j * cellSize) - 2, 4, 4);
				g.setColor(Color.GRAY);
				g.drawRect((i * cellSize), (j * cellSize), cellSize, cellSize);
			}
		}
		renderMaze(g);
		renderAgent(g);
	}
	public Maze getMaze() {
		return m;
	}
	public void update() {
		paintComponent(getGraphics());
	}
	public void renderMaze(Graphics g) {
		int offset = 0;
		int cellSizeOff = cellSize + 0;
		if(divisions) {
			cellSizeOff -= 3;
			offset = 2;
		}
		double scale = (cellSizeOff) / 64.0;
		for(int i = mazeStartX; i < m.width + mazeStartX; i ++) {
			for(int j = mazeStartY; j < m.height + mazeStartY; j++) {
				grid[i][j] = 1;
				g.drawImage(m.get(i - mazeStartX, j - mazeStartY).getImage(), (i * cellSize) + offset, (j * cellSize)+offset, (int)(64 * scale), (int)(64 * scale), this);
			}
		}
		//render start and end position
		grid[mazeStartX - 1][mazeStartY + m.startPos] = 2;
		g.setColor(new Color(0, 200, 0));
		g.fillRect((mazeStartX - 1) * cellSize, (mazeStartY + m.startPos) * cellSize, cellSize, cellSize);
		//render end position
		grid[mazeStartX + m.width][mazeStartY + m.endPos] = 3;
		g.setColor(new Color(200, 0, 0));
		g.fillRect((mazeStartX + m.width) * cellSize, (mazeStartY + m.endPos) * cellSize, cellSize, cellSize);
	}
	public void renderAgent(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillOval(cellSize * (a.getPos().width + mazeStartX), cellSize * (a.getPos().height + mazeStartY), cellSize, cellSize);
	}
}
