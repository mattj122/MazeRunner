package render;

import java.awt.Dimension;
import java.util.ArrayList;

import objects.Cell;
import objects.Maze;
import objects.OutConsole;

public class Agent {
	int xPos, yPos;
	private Maze m;
	private ArrayList<Dimension> intersect;
	private boolean atStart = true, finished = false;
	private List intersList;
	private OutConsole console;
	public Agent(Maze inMaze, OutConsole aiConsole) {
		m = inMaze;
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
	private void checkMovements(int dir) throws Exception {
		Cell cur = new Cell(m.get(xPos, yPos).getType());
		Exception e2 = new Exception("Cannot go out of bounds of the maze");
		Exception e = new Exception("Not a viable direction to move. ");
		if(dir == 1 && !cur.east) {
			throw e;
		}
		if(dir == 2 && !cur.north) {
			throw e;
		}
		if(dir == 3 && !cur.west) {
			throw e;
		}
		if(dir == 4 && !cur.south) {
			throw e;
		}
		if(!atStart) {
			if(xPos == 0 && dir == 3) {
				throw e2;
			}
			if(yPos == 0 && dir == 2) {
				throw e2;
			}
			if(xPos == (m.width-1) && dir == 1) {
				if(yPos == m.endPos) {
					finished = true;
				}
				else {
					throw e2;
				}
			}
			if(yPos == (m.height-1) && dir == 4) {
				throw e2;
			}
		}
		else {
			if(dir != 1) {
				throw new Exception("Can only go right. ");
			}
		}
	}
	public void checkCell() throws Exception {
		if(finished) {
			console.add("Maze Completed. ");
		}
		boolean inters = false;
		int x = -1, y = -1;
		Cell cur = new Cell(m.CellAt(xPos, yPos).getType());
		//If a cell is a power of 2, it is a dead end
		switch(cur.getType()) {
		case 1: //Dead-End
			deadEnd();
		break;
		case 2: //Dead-End
			deadEnd();
		break;
		case 4: //Dead-End
			deadEnd();
		break;
		case 7: //Intersection
			console.add("Intersection discovered.");
			inters = true;
		break;
		case 8: //Dead-End
			deadEnd();
		break;
		case 11: //Intersection
			console.add("Intersection discovered.");
			inters = true;
		break;
		case 13: //Intersection
			console.add("Intersection discovered.");
			inters = true;
		break;
		case 14: //Intersection
			console.add("Intersection discovered.");
			inters = true;
		break;
		case 15: //Intersection
			console.add("Intersection discovered.");
			inters = true;
		break;
		//All others are paths that are not dead-ends or intersections
		default: //Go along path
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
		console.add("Returning to intersection at position (" + xPos + ", " + yPos + "). ");
	}
}
