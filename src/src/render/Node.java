package render;

import java.awt.Dimension;

public class Node {
	private Node next;
	Dimension d;
	private boolean exists;
	public Node(Dimension f) {
		exists = true;
		d = f;
	}
	public Node() {
		exists = false;
	}
	//To-Do
	public boolean hasNext() {
		return true;
	}
	public Dimension getDimension() {
		return d;
	}
	public void setData(Dimension f) {
		d = f;
	}
	public boolean exists() {
		return exists;
	}
	public void setNext(Node n) {
		next = n;
	}
}
