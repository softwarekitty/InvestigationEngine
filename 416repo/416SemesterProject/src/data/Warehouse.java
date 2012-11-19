package data;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.LinkedList;

public class Warehouse implements WindowListener{
	private LinkedList<File> storedFiles;
	
	public Warehouse(){
		storedFiles = new LinkedList<File>();
	}
	
	public boolean add(File f){
		return storedFiles.add(f);
	}
	
	public File get(String filename){
		for(File f:storedFiles){
			if(f.getName().equals(filename)){
				return f;
			}
		}
		return null;
	}
	
	public void cleanUp(){
		for(File f:storedFiles){
			f.delete();
		}
		if(storedFiles.size()>0){
			System.err.println("some files were not deleted!!!");
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		cleanUp();
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
