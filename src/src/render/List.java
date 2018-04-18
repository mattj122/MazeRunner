package render;

import java.awt.Dimension;

public class List {
	Node head, tail;
	public List() {
		//if(head)
		head.setNext(tail);
	}
	public void append(Dimension d) {
		
	}
	public void add(Dimension d) {
		
	}
	public void add(int x, int y) {
		
	}
	public void removeAt(int index) throws Exception {
		
	}
	public void printList() throws Exception {
		
	}
	public static void main(String[] args) {
		List l = new List();
		for(int i = 0; i < 10; i++) {
			l.add(new Dimension(0, i));
		}
		try {
			l.printList();
		} catch (Exception e) {	
			e.printStackTrace();
		}
	}
}
