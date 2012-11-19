package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import data.ClassListMap;
import data.DataMiner;

/*
 *  This program allows the user to select a language
 *  and view statistics about the usage of Classes for that language 
 *  for a set of projects (probably from github or sourceforge or something like that)
 *  
 *  notes for development:
 *  step 1: select a language
 *  		a). load a list of Classes from a local file into a structure (ClassList)
 *  (step 2: select the depth of search (here up to 100))
 *  step 3: get a list of all remote projects (ProjectList) for that language (here .java)
 *  		a). save the ProjectList to a text file.
 *  step 4: for each project on the ProjectList
 *  		a). clone the repo to a local folder.
 *  		b). find a list of absolute paths for the (.java) files
 *  		c). for each file, count the occurrences of import statements that are contained in the classlist
 *  		d). save all of these counts in a local file (KyotoCabinet?) structure representing that project
 *  		(d). if possible and counter>0, dial back the repo to a previous step, decrementing a counter)
 *  		(e). repeat a-e)
 *  step 5: using the on-disk structures that represent the projects, generate:
 *  		a). top N imports graph with x being Class, y being times imported (absolute, by project) 
 *  		b). select a single project and see the frequency of (all? or top N) classes used
 *  		c). select a single class from the language and look at the frequency of use
 *  
 */
@SuppressWarnings("serial")
public class APIUsageExplorer extends JPanel implements ActionListener{
	public static boolean hasData;
	private JComboBox comboBox;
	private JButton evaluateButton;
	
	public APIUsageExplorer(){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		hasData=false;
		JTabbedPane tPane = new JTabbedPane();
		tPane.add("select",getSelectionPane());
		tPane.add("Top N Classes",getTopNPane());
		tPane.add("Top N by Project",getByProjectPane());
		tPane.add("Single Class Usage",getSingleClassPane());
		add(tPane,BorderLayout.CENTER );
	}
	
	private JPanel getSelectionPane(){
		JPanel panel = new JPanel();
		comboBox = new JComboBox(ClassListMap.getLanguages());
		comboBox.setSelectedIndex(0);
		panel.add(comboBox);
		panel.add(new JLabel("please select the language you would like to evaluate"));
		evaluateButton = new JButton("evaluate");
		evaluateButton.addActionListener(this);
		panel.add(evaluateButton);
		return panel;
	}
	
	private JPanel getTopNPane(){
		JPanel panel = new JPanel();
		panel.add(new JLabel("some results go here"));
		return panel;
	}

	private JPanel getByProjectPane() {
		JPanel panel = new JPanel();
		panel.add(new JLabel("some results go here"));
		return panel;
	}
	
	private JPanel getSingleClassPane() {
		JPanel panel = new JPanel();
		panel.add(new JLabel("some results go here"));
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		if(a.getSource()==evaluateButton){
			launchEvaluation();
			hasData=true;
		}
	}
	
	private void launchEvaluation(){
		Thread evaluationThread = new Thread() {
			public void run() {
				try {
					SwingUtilities.invokeAndWait(new DataMiner((String)comboBox.getSelectedItem()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (Main.thread) {
					System.out.println("Finished on " + Thread.currentThread());

				}
			}
		};
		evaluationThread.start();
	}
}
