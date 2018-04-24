package render;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import objects.Cell;
import objects.Maze;
import objects.OutConsole;

@SuppressWarnings("serial")
public class RenderPanel extends JPanel{
	private int cellSize = 64;
	private boolean running = false;
	private boolean pause = false;
	public int mazeStartY = 1, mazeStartX = 2;
	public int widthRes = 960;
	public int heightRes = 540;
	//private int interval = 1;
	private short [][] grid = new short [30][30];
	private Maze m;
	private Agent a;
	private boolean divisions = !true, numbers = !true;
	public Agent getAI() {
		return a;
	}
	public RenderPanel(Maze inMaze) {
		m = inMaze;
		a = new Agent(inMaze, new OutConsole(), this);
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
		g.setColor(Color.BLACK);
		if(numbers) {
			for(int i = mazeStartX; i < m.width + mazeStartX; i ++) {
				for(int j = mazeStartY; j < m.height + mazeStartY; j++) {
					g.drawString(m.get(i - mazeStartX, j - mazeStartY).getType() + "", (i * cellSize) + offset + 24, (j * cellSize) + offset + 30);
				}
			}
		}
		//render start position
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
class Agent {
	private Maze m;
	private int x, y;
	private Cell cur;
	public OutConsole console;
	private ArrayList<Dimension> intLog;
	private ArrayList<Integer> curPath;
	private boolean atStart = true, finished = false;
	private int prior;
	private ArrayList<Integer> logSuccesses = new ArrayList<Integer>();
	private RenderPanel rp;
	private double delayFac = 1.0;
	private double[] prob;
	private int[] probValues;
	private boolean dataSaved = false;
	private boolean[] viable = new boolean[4];
	private String probFileName = "agent_data", probFileDir = "./.mazerunner/";
	
	public Agent(Maze inMaze, OutConsole aiConsole, RenderPanel renderP) {
		m = new Maze(inMaze.getFilename(), inMaze.getFileDir());
		console = aiConsole;
		x = -1;
		y = m.startPos;
		rp = renderP;
		intLog = new ArrayList<Dimension>();
		curPath = new ArrayList<Integer>();
		try {
			loadProb();
		} catch (Exception e) {
			showError(e.getMessage());
		}
	}
	public void showError(String str) {JOptionPane.showMessageDialog(null, str);}
	private void loadProb() throws Exception {
		prob = new double [4];
		probValues = new int[8];
		Properties probData = new Properties();
		FileInputStream inStream = new FileInputStream(probFileDir + "\\" + probFileName + ".ini");
		probData.load(inStream);
		inStream.close();
		String fileType = probData.getProperty("type");
		if(!(fileType.substring(0, 1).equalsIgnoreCase("a"))) {
			probData.clear();
			throw new Exception("Error: type fault, clearing properties");
		}
		else {
			probValues[0] = Integer.parseInt(probData.getProperty("timesEast", "1"));
			probValues[1] = Integer.parseInt(probData.getProperty("successEast", "1"));
			probValues[2] = Integer.parseInt(probData.getProperty("timesSouth", "1"));
			probValues[3] = Integer.parseInt(probData.getProperty("successSouth", "1"));
			probValues[4] = Integer.parseInt(probData.getProperty("timesWest", "1"));
			probValues[5] = Integer.parseInt(probData.getProperty("successWest", "1"));
			probValues[6] = Integer.parseInt(probData.getProperty("timesNorth", "1"));
			probValues[7] = Integer.parseInt(probData.getProperty("successNorth", "1"));
		}
		
		//Calculate probability values
		for(int i = 0; i < 4; i++) {
			prob[i] = (double)(probValues[(i * 2) + 1]) / (double)(probValues[(i * 2)]);
		}		
	}
	public void log(String str) {
		console.add(str);
		RunnerWindow.updateConsoles();
	}
	private void checkCell() {
		RunnerWindow.updateConsoles();
		if(finished) {
			log("Maze Completed. ");
			if(!dataSaved) {
				try {
					saveLogData();
				} catch (IOException e) {
					showError(e.getMessage());
				}
				dataSaved = true;
			}
		}
		else {
			updateCur();
			switch(cur.pathNum) {
			case 1: //Only 1 path means a dead-end
				deadEnd();
			break;
			case 2: //2 paths means the agent can only continue along the tube
				tube();
			break;
			case 3:	//3 paths means coming to a T - log as an intersection
				intersection();
			break;
			case 4: //4 paths means coming to a 4 way cross - log as an intersection
				intersection();
			break;
			}
		}
	}
	private void saveLogData() throws IOException {
		FileOutputStream outStream;
		String out = probFileDir + "\\" + probFileName + ".ini";
		Properties mazeProp = new Properties();
		
		for(int i = 0; i < logSuccesses.size(); i++) {
			probValues[(logSuccesses.get(i) * 2) - 1]++;
		}
		
		mazeProp.setProperty("timesEast", Integer.toString(probValues[0]));
		mazeProp.setProperty("successesEast", Integer.toString(probValues[1]));
		mazeProp.setProperty("timesSouth", Integer.toString(probValues[2]));
		mazeProp.setProperty("successesSouth", Integer.toString(probValues[3]));
		mazeProp.setProperty("timesWest", Integer.toString(probValues[4]));
		mazeProp.setProperty("successesWest", Integer.toString(probValues[5]));
		mazeProp.setProperty("timesNorth", Integer.toString(probValues[6]));
		mazeProp.setProperty("successesNorth", Integer.toString(probValues[7]));

		outStream = new FileOutputStream(out);
		mazeProp.store(outStream, "Data for agent for probability calculation. ");
		outStream.close();
		mazeProp.list(System.out);
	}
	private void intersection() {
		RunnerWindow.updateConsoles();
		intLog.add(new Dimension(x, y));
		console.add("Logging intersection at position (" + x + ", " + y + "). ");
		//updateCur();
		double [] normProb  = new double[4];
		double sum = 0;
		for(int i = 0; i < 4; i++) {
			if(viable[i] && i != (cur.getPrior() - 1)) {
				sum += prob[i];
			}
		}
		for(int i = 0; i < normProb.length; i++) {
			normProb[i] = 0.0;
			if(viable[i] && i != (cur.getPrior() - 1)) {
				normProb[i] = prob[i] / sum;
			}
		}
		for(int i = 0; i < normProb.length; i++) {
			System.out.println(normProb[i]);
		}
		int move = 1;
		double choice = Math.random();
		System.out.println(choice);
		if(choice <= normProb[0]) {
			System.out.println("Choose East. ");
			move = 1;
		}
		else if(choice <= normProb[0] + normProb[1]) {
			System.out.println("Choose South. ");
			move = 2;
		}
		else if(choice <= normProb[0] + normProb[1] + normProb[2]) {
			System.out.println("Choose West. ");
			move = 3;
		}
		else {
			System.out.println("Choose North. ");
			move = 4;
		}
		curPath.add(move);
		logSuccesses.add(move);
		move(move);
	}
	//1 = east
	//2 = south
	//3 = west
	//4 = north
	public void move(int dir) {
		RunnerWindow.updateConsoles();
		delay();
		if(finished) {
			log("Maze Already Completed. ");
		}
		else {
			switch(dir) {
			case 1: //Go Right
				if(atStart || cur.east){
					x++;
					log("Moving agent 1 space to the right. ");
					if(atStart) {
						atStart = false;
						log("Leaving starting position. ");
					}
				}
				else {
					log("Invalid move. ");
				}
			break;
			case 2: //Go Down
				if(cur.south) {
					y++;
					log("Moving agent 1 space down. ");
				}
				else if(atStart || !cur.south){
					log("Invalid move. ");
				}
			break;
			case 3: //Go Left 
				if(cur.west && x != 0) {
					x--;
					log("Moving agent 1 space to the left. ");
				}
				else if(atStart || !cur.west){
					log("Invalid move. ");
				}
			break;
			case 4: //Go Up
				if(cur.north) {
					y--;
					log("Moving agent 1 space up. ");
				}
				else if(atStart || !cur.north){
					log("Invalid move. ");
				}
			break;
			}
			if(x == m.width && y == m.endPos) {
				finished = true;
			}
			if(dir <= 2) {
				prior = (dir + 2);
			}
			else {
				prior = (dir - 2);
			}
			if(!finished) {
				m.get(x, y).setPrior(prior);
			}
			checkCell();
		}
		probValues[(dir - 1) * 2]++;
	}
	private void tube() {
		RunnerWindow.updateConsoles();
		//updateCur();
		int dir = 1; 
		for(int i = 1; i <= 4; i++) {
			if(i != cur.getPrior() && cur.getDirBool(i)) {
				dir = i;
			}
		}
		try {
			move(dir);
		} catch (Exception e) {
			e.printStackTrace();
		}
		RunnerWindow.updateConsoles();
	}
	private void deadEnd() {
		if(!finished) {
			log("Dead-end. ");
			if(!(intLog.isEmpty())) {
				delay();
				x = intLog.get(intLog.size()-1).width;
				y = intLog.get(intLog.size()-1).height;
				logSuccesses.remove(logSuccesses.size()-1);
				m.get(x, y).setDir(curPath.get(curPath.size()-1), false);
				updateCur();
				intLog.remove(intLog.size()-1);
				curPath.remove(curPath.size()-1);
				cur.pathNum--;
				if(cur.pathNum == 1) {
					deadEnd();
				}
				else {
					log("Returning to intersection at position (" + x + ", " + y + "). ");
					checkCell();
				}
				
			}
			else {
				log("No possible outlets. ");
			}
		}
	}
	public OutConsole getCons() {
		return console;
	}
	public Dimension getPos() {
		return new Dimension(x, y);
	}
	private void updateCur() {
		cur = m.get(x, y);
		viable[0] = cur.east;
		viable[1] = cur.south;
		viable[2] = cur.west;
		viable[3] = cur.north;
		prior = cur.getPrior();
	}
	private void delay() {
		rp.update();
		RunnerWindow.updateConsoles();
		try {
			TimeUnit.MILLISECONDS.sleep((long) (500 * delayFac));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		RunnerWindow.updateConsoles();
	}
	public void updateDelayFactor(double fac) {
		delayFac = 1.0 * fac;
	}
}
