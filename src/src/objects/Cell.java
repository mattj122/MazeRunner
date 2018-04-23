package objects;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Cell {
	int type;
	public int imgIndex, pathNum = 0;
	public boolean north;
	public boolean east;
	public boolean south;
	public boolean west;
	BufferedImage tileset;
	BufferedImage tile;
	public int rotFac;
	
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
			showError(e.getMessage());
		}
		setImgIndex();
		setImage();
	}
	public static void showError(String str) {JOptionPane.showMessageDialog(null, str);}
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
	private void setBoolsFromInt() {
		east = false;
		west = false;
		south = false;
		north = false;
		
		if((type / 8) % 2 > 0) {
			east = true;
			pathNum++;
		}
		if((type / 4) % 2 > 0) {
			south = true;
			pathNum++;
		}
		if((type / 2) % 2 > 0) {
			west = true;
			pathNum++;
		}
		if(type % 2 > 0) {
			north = true;
			pathNum++;
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
		imgIndex = 0;
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
		if((east && west && !north && !south) || (north && south && !east && !west)) {
			imgIndex = 5;
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
		return tile;
	}
	public void setImage() {
		int x = 64 * imgIndex;
		rotFac = 0;
		tile = tileset.getSubimage(x, 0, 64, 64);
		switch(type) {
		case 2: rotFac = 270;
		break;
		case 3: rotFac = 270;
		break;
		case 4: rotFac = 180;
		break;
		case 6: rotFac = 180;
		break;
		case 7: rotFac = 180;
		break;
		case 8: rotFac = 90;
		break;
		case 10: rotFac = 90;
		break;
		case 11: rotFac = 270;
		break;
		case 12: rotFac = 90;
		break;
		case 14: rotFac = 90;
		break;
		}
		rotate(rotFac);
	}
	public void rotate(int fac) {
		AffineTransform at = new AffineTransform();
		at.rotate(Math.toRadians(fac), tile.getWidth()/2, tile.getHeight()/2);
		AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		tile = op.filter(tile, null);
	}
}
