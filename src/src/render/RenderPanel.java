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
		a = new Agent(inMaze, this);
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
		cellSize = 2;
		cellSize = Math.min((widthRes / (m.width + 2)), (heightRes / (m.height + 2)));
		System.out.println(cellSize);
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
	private boolean atStart = true;
	protected boolean finished = false;
	private int prior;
	private ArrayList<Integer> logSuccesses = new ArrayList<Integer>();
	private RenderPanel rp;
	private double delayFac = 1.0;
	private double[] prob;
	private int[] probValues;
	private boolean dataSaved = false;
	private boolean[] viable = new boolean[4];
	String probFileName = "agent_data", probFileDir = ".\\.mazerunner";
	//1 for use data
	//2 for random direction
	private int rule = 1;
	
	public Agent(Maze inMaze, RenderPanel renderP) {
		m = new Maze(inMaze.getFilename(), inMaze.getFileDir());
		console = new OutConsole();
		x = -1;
		y = m.startPos;
		rp = renderP;
		intLog = new ArrayList<Dimension>();
		curPath = new ArrayList<Integer>();
		try {
			loadProb();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setRule(int i) {
		if(i > 0 && i < 3) {
			rule = i;
		}
		else {
			showError("Incorrect input number, must be '1' or '2'");
		}
	}
	public void showError(String str) {JOptionPane.showMessageDialog(null, str);}
	private void loadProb() throws Exception {
		log("Loading agent data from " + probFileDir + "\\" + probFileName + ".ini");
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
			probValues[0] = Integer.parseInt(probData.getProperty("timesEast"));
			probValues[1] = Integer.parseInt(probData.getProperty("successEast"));
			probValues[2] = Integer.parseInt(probData.getProperty("timesSouth"));
			probValues[3] = Integer.parseInt(probData.getProperty("successSouth"));
			probValues[4] = Integer.parseInt(probData.getProperty("timesWest"));
			probValues[5] = Integer.parseInt(probData.getProperty("successWest"));
			probValues[6] = Integer.parseInt(probData.getProperty("timesNorth"));
			probValues[7] = Integer.parseInt(probData.getProperty("successNorth"));
		}
		//Calculate probability values
		for(int i = 0; i < 4; i++) {
			prob[i] = (double)(probValues[(i * 2) + 1]) / (double)(probValues[(i * 2)]);
			//System.out.println(prob[i]);
		}
		for(int i = 0; i < 8; i++) {
			//System.out.println(probValues[i]);
		}
		log("Data loaded. ");
	}
	public void log(String str) {
		console.add(str);
		//RunnerWindow.updateConsoles();
	}
	private void checkCell() {
		RunnerWindow.updateConsoles();
		if(finished) {
			log("Maze Completed. ");
			if(!dataSaved) {
				try {
					saveLogData();
				} catch (Exception e) {
					showError(e.getMessage());
				}
				dataSaved = true;
			}
		}
		else {
			updateCur();
			switch(cur.pathNum) {
			case 1: //Only 1 path means a dead-end
				log("Dead-end. ");
				deadEnd();
			break;
			case 2: //2 paths means the agent can only continue along the tube
				tube();
			break;
			case 3:	//3 paths means coming to a T - log as an intersection
				log("Logging intersection at position (" + x + ", " + y + "). ");
				intersection();
			break;
			case 4: //4 paths means coming to a 4 way cross - log as an intersection
				log("Logging intersection at position (" + x + ", " + y + "). ");
				intersection();
			break;
			}
		}
	}
	private void saveLogData() throws IOException {
		log("Saving agent log data... ");
		FileOutputStream outStream;
		String out = probFileDir + "\\" + probFileName + ".ini";
		Properties probProp = new Properties();
		
		for(int i = 0; i < logSuccesses.size(); i++) {
			probValues[(logSuccesses.get(i) * 2) - 1]++;
		}
		
		probProp.setProperty("timesEast", Integer.toString(probValues[0]));
		probProp.setProperty("successEast", Integer.toString(probValues[1]));
		probProp.setProperty("timesSouth", Integer.toString(probValues[2]));
		probProp.setProperty("successSouth", Integer.toString(probValues[3]));
		probProp.setProperty("timesWest", Integer.toString(probValues[4]));
		probProp.setProperty("successWest", Integer.toString(probValues[5]));
		probProp.setProperty("timesNorth", Integer.toString(probValues[6]));
		probProp.setProperty("successNorth", Integer.toString(probValues[7]));
		
		probProp.setProperty("type", "a");

		outStream = new FileOutputStream(out);
		probProp.store(outStream, "Data for agent for probability calculation. ");
		outStream.close();
		log("Data saved under " + probFileDir + "\\" + probFileName + ".ini");
	}
	private void intersection() {
		RunnerWindow.updateConsoles();
		intLog.add(new Dimension(x, y));
		//updateCur();
		double [] normProb  = new double[4];
		double sum = 0;
		for(int i = 0; i < 4; i++) {
			if(viable[i] && i != (cur.getPrior() - 1)) {
				if(rule == 2) {
					sum += 1;
				}
				else {
					sum += prob[i];
				}
			}
		}
		for(int i = 0; i < normProb.length; i++) {
			normProb[i] = 0.0;
			if(viable[i] && i != (cur.getPrior() - 1)) {
				if(rule == 2) {
					normProb[i] = 1.0 / sum;
					System.out.println(normProb[i]);
				}
				else {
					normProb[i] = prob[i] / sum;
				}
			}
		}
		int move = 1;
		double choice = Math.random();
		if(choice <= normProb[0]) {
			move = 1;
		}
		else if(choice <= normProb[1] + normProb[0]) {
			move = 2;
		}
		else if(choice <= normProb[2] + normProb[1] + normProb[0]) {
			move = 3;
		}
		else {
			move = 4;
		}
		//Log the current path
		curPath.add(move);
		//Increment each "times" of direction to be saved later for a total of attempts
		probValues[(move - 1) * 2]++;
		//Log every move into successes, remove if the path leads to a dead-end. 
		logSuccesses.add(move);
		//Move in that direction
		move(move);
	}
	/*
	 * If a move is valid, move the agent in that direction, else print "Invalid Move"
	 * 1 = east
	 * 2 = south
	 * 3 = west
	 * 4 = north
	 * */
	public void move(int dir) {
		RunnerWindow.updateConsoles();
		delay();
		if(finished) {
			log("Maze Already Completed. ");
		}
		else {
			switch(dir) {
			case 1: //Go Right/East
				if(atStart || cur.east){
					x++;
					log("Moving right. ");
					if(atStart) {
						atStart = false;
						log("Leaving starting position. ");
					}
				}
				else {
					log("Invalid move. ");
				}
			break;
			case 2: //Go Down/South
				if(cur.south) {
					y++;
					log("Moving down. ");
				}
				else if(atStart || !cur.south){
					log("Invalid move. ");
				}
			break;
			case 3: //Go Left/West
				if(cur.west && x != 0) {
					x--;
					log("Moving left. ");
				}
				else if(atStart || !cur.west){
					log("Invalid move. ");
				}
			break;
			case 4: //Go Up/North
				if(cur.north) {
					y--;
					log("Moving up. ");
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
	}
	private void tube() {
		RunnerWindow.updateConsoles();
		//updateCur();
		try {
			move(tubeMove());
		} catch (Exception e) {
			e.printStackTrace();
		}
		RunnerWindow.updateConsoles();
	}
	private void deadEnd() {
		if(!finished) {
			if(!(intLog.isEmpty())) {
				delay();
				x = intLog.get(intLog.size()-1).width;
				y = intLog.get(intLog.size()-1).height;
				if(!curPath.isEmpty()) {
					m.get(x, y).setDir(curPath.get(curPath.size()-1), false);
				}
				updateCur();
				if(!logSuccesses.isEmpty()) {
					//System.out.println("removing " + logSuccesses.get(logSuccesses.size()-1) + " from log of successes. ");
					logSuccesses.remove(logSuccesses.size()-1);
				}
				cur.pathNum--;
				if(cur.pathNum == 1) {
					intLog.remove(intLog.size()-1);
					deadEnd();
				}
				else {
					curPath.remove(curPath.size()-1);
					log("Returning to intersection at position (" + x + ", " + y + "). ");
					if(cur.pathNum == 2) {
						logSuccesses.add(tubeMove());
					}
					checkCell();
				}
				
			}
			else {
				log("No possible outlets. ");
			}
		}
	}
	private Integer tubeMove() {
		int dir = 1; 
		for(int i = 1; i <= 4; i++) {
			if(i != cur.getPrior() && cur.getDirBool(i)) {
				dir = i;
			}
		}
		return dir;
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
