package objects;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JOptionPane;

public class Maze {
	Cell[][] mazeArray;
	public int width, height, startPos, endPos;
	private static String dir = "./.mazerunner/maze_save/";
	private static String fileName = "dummy";
	public static void main(String [] args) throws Exception {
		//Maze m = new Maze(5, 3);
		//m.saveMaze();
		Maze m = new Maze(fileName, dir);
	}
	public Maze(int x, int y) {
		width = x;
		height = y;
		startPos = 0;
		endPos = 0;
		mazeArray = new Cell [width][height];
		for(int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				mazeArray[i][j] = new Cell();
			}
		}
	}
	public Maze(String fileName, String address) {
		try {
			loadMaze(fileName, address);
		} catch (Exception e) {
			showError(e.getMessage());
		}
		printMazeValues(true);
		//printMazeValues(false);
	}
	public String getFilename() {
		return fileName;
	}
	public String getFileDir() {
		return dir;
	}
	public Cell get(int x, int y) {
		return mazeArray[x][y];
	}
	public void saveMaze() throws Exception {
		FileOutputStream outStream;
		String out = dir + "\\" + fileName + ".ini";
		Properties mazeProp = new Properties();
		String key;
		String value;
		for(int i = width - 1; i >= 0; i--){
			for(int j = height - 1; j >= 0; j--) {
				key = Integer.toString(i) + "-" + Integer.toString(j);
				value = Integer.toString(mazeArray[i][j].getType());
				mazeProp.setProperty(key, value);
			}
		}
		mazeProp.setProperty("width", Integer.toString(width));
		mazeProp.setProperty("height", Integer.toString(height));
		mazeProp.setProperty("start", Integer.toString(startPos));
		mazeProp.setProperty("end", Integer.toString(endPos));
		outStream = new FileOutputStream(out);
		mazeProp.store(outStream, "Data for " + width + " x " + height + " maze.");
		outStream.close();
		mazeProp.list(System.out);
		
	}
	public void generateMaze() {
		
	}
	public void loadMaze(String fileName, String address) throws Exception {
		Properties mazeProp = new Properties();
		FileInputStream inStream = new FileInputStream(address + "\\" + fileName + ".ini");
		mazeProp.load(inStream);
		inStream.close();
		String fileType = mazeProp.getProperty("type");
		if(!(fileType.substring(0, 1).equalsIgnoreCase("m"))) {
			mazeProp.clear();
			throw new Exception("Error: type fault, clearing properties");
		}
		else {
			width = Integer.parseInt(mazeProp.getProperty("width"));
			height = Integer.parseInt(mazeProp.getProperty("height"));
			startPos = Integer.parseInt(mazeProp.getProperty("start"));
			endPos = Integer.parseInt(mazeProp.getProperty("end"));
			mazeArray = new Cell [width][height];
			for(int i = width-1; i >= 0; i--) {
				for(int j = height-1; j >= 0; j--) {
					mazeArray[i][j] = new Cell(Integer.parseInt(mazeProp.getProperty(Integer.toString(i) + "-" + Integer.toString(j), "0")));
				}
			}
		}
		
	}
	public void showError(String str) {JOptionPane.showMessageDialog(null, str);}
	/* Type values
	 * 0 = dead cell
	 * 1 = north open
	 * 2 = west open
	 * 3 = west north open
	 * 4 = south open
	 * 5 = south north open
	 * 6 = south west open
	 * 7 = south west north open
	 * 8 = east open
	 * 9 = east north open
	 * 10= east west open
	 * 11= east west north open
	 * 12= east south open
	 * 13= east south north open
	 * 14= east south west open
	 * 15= all open
	 */
	//Needs work
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
	//Needs work
	public void openDirection(int x, int y, int dir) {
		
	}
	public void printMazeValues(boolean b) {
		boolean mazeVal = b;
		if(width == 0){
			showError("Unable to print null array");
		}
		else{
			for(int i = 0; i < height; i++) {
				for(int j = 0; j < width; j++) {
					if(mazeVal) {
						System.out.print(mazeArray[j][i].getType() + "\t");
					}
					else {
						System.out.print(mazeArray[j][i].pathNum + "\t");
					}
				}
				System.out.println();
			}
			System.out.println("start = " + startPos);
			System.out.println("end = " + endPos);
		}
	}
	//Needs work
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
	public Cell CellAt(int xPos, int yPos) {
		return mazeArray[xPos][yPos];
	}
}
