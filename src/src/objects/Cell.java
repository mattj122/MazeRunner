package objects;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Cell {
	int type, imgIndex;
	boolean north, east, south, west;
	BufferedImage tileset;
	
	public Cell() {
		type = 0;
		init();
	}
	public Cell(int x) {
		type = x;
		init();
	}
	public void init() {
		setBoolsFromInt();
		try {
			tileset = ImageIO.read(new File("./.mazerunner/tileset.png"));
		}
		catch(Exception e){
			
		}
		setImgIndex();
	}
	
	/*
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
	private void setBoolsFromInt() {
		east = false;
		west = false;
		south = false;
		north = false;
		
		if((type / 8) % 2 > 0) {
			east = true;
		}
		if((type / 4) % 2 > 0) {
			south = true;
		}
		if((type / 2) % 2 > 0) {
			west = true;
		}
		if(type % 2 > 0) {
			north = true;
		}
		setImgIndex();
	}
	public void setEast(boolean value) {
		east = value;
		setIntFromBools();
	}
	public void setWest(boolean value) {
		west = value;
		setIntFromBools();
	}
	public void setSouth(boolean value) {
		south = value;
		setIntFromBools();
	}
	public void setNorth(boolean value) {
		north = value;
		setIntFromBools();
	}
	public void setImgIndex() {
		if(north) {
			imgIndex++;
		}
		if(south) {
			imgIndex++;
		}
		if(east) {
			imgIndex++;
		}
		if(west) {
			imgIndex++;
		}
	}
	public void setIntFromBools() {
		type = 0;
		if(east) {
			type += 8;
		}
		if(south) {
			type += 4;
		}
		if(west) {
			type += 2;
		}
		if(north) {
			type++;
		}
		setImgIndex();
	}
	public void setType(int x){
		type = x;
		setBoolsFromInt();
	}
	public int getType() {
		return type;
	}
	public void debug() {
		System.out.println("East: " + east);
		System.out.println("West: " + west);
		System.out.println("South: " + south);
		System.out.println("North: " + north);
		System.out.println("Type Num: " + type);
	}
	public BufferedImage getImage() {
		BufferedImage output = tileset.getSubimage(0, 64 * imgIndex, 64, 64);
		return output;
	}
}
