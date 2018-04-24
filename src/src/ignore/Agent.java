package ignore;

import java.awt.Dimension;
import java.util.ArrayList;

import objects.Cell;
import objects.Maze;
import objects.OutConsole;
//import render.List;

public class Agent {
	int xPos, yPos;
	private Maze m;
	private ArrayList<Dimension> intersect;
	private boolean atStart = true, finished = false;
	private boolean vpNorth = false, vpSouth = false, vpWest = false, vpEast = true;
	//private List intersList;
	private int prior = 0;
	private Cell cur;
	private OutConsole console;
	public Agent(Maze inMaze, OutConsole aiConsole) {
		m = new Maze(inMaze.getFilename(), inMaze.getFileDir());
		console = aiConsole;
		xPos = -1;
		yPos = m.startPos;
		intersect = new ArrayList<Dimension>();
	}
	public void log(String str) {
		console.add(str);
	}
	public OutConsole getCons() {
		return console;
	}
	public Dimension getPos() {
		return new Dimension(xPos, yPos);
	}
	//1 = east
	//2 = south
	//3 = west
	//4 = north
	public void move(int dir) throws Exception {
		if(finished) {
			throw new Exception("Maze Already Completed. ");
		}
		if(!atStart) {
			checkMovements(dir);
		}
		switch(dir) {
		case 1: //Go Right
			xPos++;
			console.add("Moving agent 1 space to the right. ");
			if(atStart) {
				atStart = false;
			}
		break;
		case 2: //Go Down
			yPos--;
			console.add("Moving agent 1 space up. ");
		break;
		case 3: //Go Left 
			xPos--;
			console.add("Moving agent 1 space to the left. ");
		break;
		case 4: //Go down
			yPos++;
			console.add("Moving agent 1 space down. ");
		break;
		}
		
		if(!finished || !atStart) {
			checkCell();
		}
	}
	private boolean checkMovements(int dir) throws Exception {
		Cell cur = new Cell(m.get(xPos, yPos).getType());
		boolean out = false;
		Exception e2 = new Exception("Cannot go out of bounds of the maze");
		if(xPos == 0 && dir == 3) {
			throw e2;
		}
		else if(yPos == 0 && dir == 2) {
			throw e2;
		}
		else if(xPos == (m.width-1) && dir == 1) {
			if(yPos == m.endPos) {
				finished = true;
			}
			else {
				throw e2;
			}
		}
		else if(yPos == (m.height-1) && dir == 4) {
			throw e2;
		}
		else {
			out = true;
		}
		return out;
	}
	public void checkCell() throws Exception {
		if(finished) {
			console.add("Maze Completed. ");
		}
		cur = new Cell(m.CellAt(xPos, yPos).getType());
		boolean inters = false;
		int x = -1, y = -1;
		//If a cell is a power of 2, it is a dead end
		switch(cur.pathNum) {
		case 1:
			deadEnd();
		break;
		case 2: //Proceed to next viable direction
			int movement = 1;
			while((movement == prior) || !(checkMovements(movement))) {
				movement++;
			}
			move(movement);
		break;
		case 3: //Intersection to log
			inters = true;
		break;
		case 4:	//Intersection to log
			inters = true;
		break;
		}
		if(inters == true) {
			intersect.add(new Dimension(xPos, yPos));
			console.add("Logging intersection at position (" + xPos + ", " + yPos + "). ");
		}
	}
	public void deadEnd() throws Exception {
		console.add("Dead-End");
		if(!(intersect.isEmpty())) {
			xPos = intersect.get(intersect.size()-1).width;
			yPos = intersect.get(intersect.size()-1).height;
		}
		else {
			throw new Exception("No possible outlets. ");
		}
		cur = m.get(xPos, yPos);
		if(cur.pathNum == 1) {
			intersect.remove(intersect.size()-1);
			deadEnd();
		}
		console.add("Returning to intersection at position (" + xPos + ", " + yPos + "). ");
	}
}
