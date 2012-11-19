package gui;

/*
 *  This program allows the user to select a language
 *  and view statistics about the usage of Classes for that language 
 *  for a set of projects (probably from github or sourceforge or something like that)
 *  
 *  notes for development:
 *  step 1: select a language
 *  		a). load a list of Classes from a local file into a structure (ClassList)
 *  step 2: select the depth of search (here up to 100)
 *  step 3: get a list of all remote projects (ProjectList) for that language (here .java)
 *  		(a). save the ProjectList to a text file)
 *  step 4: for each project on the ProjectList
 *  		a). clone the repo to a local folder.
 *  		b). find a list of absolute paths for the (.java) files
 *  		c). for each file, count the occurrences of import statements that are contained in the classlist
 *  		d). save all of these counts in a local file (KyotoCabinet?) structure representing that project
 *  		d). if possible and counter>0, dial back the repo to a previous step, decrementing a counter
 *  		e). repeat a-e
 *  step 5: using the on-disk structures that represent the projects, generate:
 *  		a). top N imports graph with x being Class, y being times imported (absolute, by project) 
 *  		b). select a single project and see the frequency of (all? or top N) classes used
 *  		c). select a single class from the language and look at the frequency of use
 *  
 */
import java.awt.Dimension;
import java.util.Random;

import javax.swing.JApplet;
import javax.swing.JFrame;

import data.ClassListMap;
import data.Warehouse;

@SuppressWarnings("serial")
public class Main extends JApplet {
	private static JFrame frame;
	public static boolean test = true;
	public static boolean verbose = true;
	public static boolean thread = false;
	private static Warehouse warehouse = new Warehouse();
	private static ClassListMap classListMap = new ClassListMap();
	private static Random gen = new Random(System.currentTimeMillis());

	// private static File currentFile;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		frame = new JFrame();
		frame.setPreferredSize(new Dimension(900, 750));
		frame.addWindowListener(warehouse);
		frame.setContentPane(new APIUsageExplorer());
		frame.pack();
		frame.setVisible(true);
	}

	public static void repack() {
		frame.pack();
		if (verbose) {
			System.out.println("frame repacked");
		}
	}
	
	public static Warehouse getWarehouse(){
		return warehouse;
	}


	public static ClassListMap getClassListMap() {
		return classListMap;
	}
	
	public static Random getGen(){
		return gen;
	}
}
