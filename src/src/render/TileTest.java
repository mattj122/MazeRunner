package render;

import objects.Cell;

public class TileTest {

	public static void main(String[] args) {
		Cell c = new Cell(2);
		System.out.println("North: " + c.north);
		System.out.println("South: " + c.south);
		System.out.println("East: " + c.east);
		System.out.println("West: " + c.west);
	}

}
