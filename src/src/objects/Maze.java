package objects;

public class Maze {
	Cell[][] mazeArray;
	public int width;
	public int height;
	public Maze(int x, int y) {
		width = x;
		height = y;
		mazeArray = new Cell [width][height];
		for(int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				mazeArray[i][j] = new Cell();
			}
		}
	}
	public Cell get(int x, int y) {
		return mazeArray[x][y];
	}
	/* Type values
	 * 1 = dead cell
	 * 2 = north open
	 * 3 = west open
	 * 4 = west north
	 * 5 = south open
	 * 6 = south north open
	 * 7 = south west open
	 * 8 = south west north open
	 * 9 = east open
	 * 10= east north open
	 * 11= east west open
	 * 12= east west north open
	 * 13= east south open
	 * 14= east south north open
	 * 15= east south west open
	 * 16= all open
	 */
	public void setCell(int x, int y, int type) throws Exception {
		if(type > 15 || x > width || y > height || x < 0 || y < 0){
			throw new Exception("Invalid input value(s)");
		}
		//Set East and set cell adjacent West
		if((type / 8) % 2 > 0) {
			if(x == width-1) {
				throw new Exception("No adjacent cell to open to");
			}
			else {
				mazeArray[x][y].setEast(true);
				mazeArray[x+1][y].setWest(true);
			}
		}
		//Set South and set cell below North
		if((type / 4) % 2 > 0) {
			if(y == height-1) {
				throw new Exception("No adjacent cell to open to");
			}
			else {
				mazeArray[x][y].setSouth(true);
				mazeArray[x][y+1].setNorth(true);
			}
		}
		//Set West and set cell adjacent East
		if((type / 2) % 2 > 0) {
			if(x == 0) {
				throw new Exception("No adjacent cell to open to");
			}
			else {
				mazeArray[x][y].setWest(true);
				mazeArray[x-1][y].setEast(true);
			}
		}
		//Set North and set cell above South
		if(type % 2 > 0) {
			if(y == 0) {
				throw new Exception("No adjacent cell to open to");
			}
			else {
				mazeArray[x][y].setNorth(true);
				mazeArray[x][y-1].setSouth(true);
			}
		}
	}
	/*
	 * 1 = East
	 * 2 = South
	 * 3 = West
	 * 4 = North
	 */
	public void openDirection(int x, int y, int dir) {
		
	}
	public void closeDirection(int x, int y, int dir) throws Exception {
		switch(dir) {
		case 1: mazeArray[x][y].setEast(false);
		if(x > 0) {
			
		}
		break;
		case 2: mazeArray[x][y].setSouth(false);
		break;
		case 3: mazeArray[x][y].setWest(false);
		break;
		case 4: mazeArray[x][y].setNorth(false);
		break;
		default: throw new Exception("Invalid direction input");
		}
		
	}
}
