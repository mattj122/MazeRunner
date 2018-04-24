package objects;

import java.awt.Dimension;
import java.util.ArrayList;

public class Agent {
	private Maze m;
	private int x, y;
	private Cell cur;
	public OutConsole console;
	private ArrayList<Dimension> intLog;
	//private ArrayList<Integer> curPath;
	private boolean atStart = true, finished = false;
	private int prior;
	public Agent(Maze inMaze, OutConsole aiConsole) {
		m = new Maze(inMaze.getFilename(), inMaze.getFileDir());
		console = aiConsole;
		x = -1;
		y = m.startPos;
		intLog = new ArrayList<Dimension>();
		//curPath = new ArrayList<Integer>();
	}
	public void log(String str) {
		console.add(str);
	}
	private void checkCell() throws Exception {
		if(finished) {
			throw new Exception("Maze Completed. ");
		}
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
	private void intersection() {
		intLog.add(new Dimension(x, y));
		console.add("Logging intersection at position (" + x + ", " + y + "). ");
	}
	//1 = east
	//2 = south
	//3 = west
	//4 = north
	public void move(int dir) throws Exception {
		if(finished) {
			throw new Exception("Maze Already Completed. ");
		}
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
		prior = dir;
		if(dir <= 2) {
			prior += 2;
		}
		else {
			prior -= 2;
		}
		checkCell();
	}
	private void tube() throws InterruptedException {
		delay();
		if(!finished) {
			updateCur();
			int dir = 1; 
			for(int i = 1; i <= 4; i++) {
				if(i != prior && cur.getDirBool(i)) {
					dir = i;
				}
			}
			try {
				move(dir);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void deadEnd() throws Exception {
		log("Dead-end. ");
		//To-Do
	}
	public OutConsole getCons() {
		return console;
	}
	public Dimension getPos() {
		return new Dimension(x, y);
	}
	private void updateCur() {
		cur = m.get(x, y);
	}
	private void delay() throws InterruptedException {
		Thread.sleep(500);
	}
	public void updateDelayFactor() {
		
	}
}
