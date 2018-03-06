package render;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DrawTest {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				createAndShowGUI();
			}

			private void createAndShowGUI() {
					//System.out.println("Create and Show GUI? " + SwingUtilities.isEventDispatchThread());
					JFrame f = new JFrame();
					f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					f.add(new MyPanel());
					f.pack();
					f.setVisible(true);
			}
		});
	}
	public static class MyPanel extends JPanel{
		public MyPanel() {
			setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}
		public Dimension getPreferredSize() {
			return new Dimension(250, 200);
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			g.drawString("Custom Panel", 10, 20);
		}
	}
}
