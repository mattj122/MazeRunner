package objects;

import java.io.File;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JOptionPane;

public class OutConsole {
	public static void main(String[] args) {
		OutConsole n = new OutConsole();
		n.add("Testing some stuff out. ");
		n.add("\tTesting tab indentation");
		n.add("Another line");
		n.exportConsole();
	}
	
	//Declare Variables
	private ArrayList<String> mainList;
	
	//Constructor
	public OutConsole() {
		mainList = new ArrayList<String>();
	}
	
	//Voids
	public void add(String str) {
		if((toString() + str).length() >= Integer.MAX_VALUE) {
			exportConsole();
		}
		mainList.add(str);
	}
	public void clear() {mainList.clear();}
	public void exportConsole() {
		DateFormat df = new SimpleDateFormat("MM_dd_yy");
		DateFormat fileName = new SimpleDateFormat("HH_mm_ss");
		File outdir = new File("./.mazerunner/console/" + df.format(Calendar.getInstance().getTime()));
		File outFile = new File(outdir.toString() + "/" + fileName.format(Calendar.getInstance().getTime()) + ".txt");
		outdir.mkdirs();
		if(!outFile.exists()) {
			try {
				outFile.createNewFile();
				System.out.println("File Created.");
			} catch (Exception e) {
				showError(e.getMessage());
			}
		}
		try {
			PrintWriter out = new PrintWriter(outFile);
			System.out.println("Writing\n" + toString() + "to file");
			for(int i = 0; i < mainList.size(); i++) {
				out.println(mainList.get(i));
			}
			out.flush();
			out.close();
		}
		catch(Exception e) {
			showError(e.getMessage());
		}
		mainList.clear();
	}
	
	//Functions
	public String toString() {
		String output = "";
		for(int i = 0; i < mainList.size(); i++) {
			output += mainList.get(i) + "\n";
		}
		return output;
	}
	public static void showError(String str) {JOptionPane.showMessageDialog(null, str);}
}
