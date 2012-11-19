package data;

import gui.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.json.JSONArray;
import org.json.JSONObject;

public class DataMiner implements Runnable {
	private String selectedLanguage;
	private String baseFolder = "/Users/carlchapman/Desktop/SE_416/";

	public DataMiner(String selectedLanguage) {
		this.selectedLanguage = selectedLanguage;
	}

	@Override
	public void run() {
		Pentuple<File, Integer, String, String,String> pentuple = Main.getClassListMap()
				.get(selectedLanguage);
		ArrayList<String> classList = getFileLines(pentuple.getFirst(),
				pentuple.getSecond());
		// TODO - scrape out a certain number of projects from github, put into

		for (String project : getProjects(pentuple.getFifth())) {
			String remotePath = "https://github.com/" + project + ".git";
			String localPath = baseFolder + project;
			cloneRepository(localPath, remotePath);
			String[] fileList = produceFileList(localPath, pentuple.getThird());
			for (String filename : fileList) {
				LinkedList<String> references = extractReferences(filename,
						pentuple.getFourth());
				int nReferences = 0;
				for (String reference : references) {
					if (classList.contains(reference)) {
						nReferences++;
						// Do something to save this in a structure
					}
				}
				if(Main.test){
					System.out.println(" filename: "+ filename+" nReferences: "+nReferences);
				}
			}
		}

		// TODO - remove all previous data if the warehouse is too big
		// TODO - call the methods to do all the work
	}

	private ArrayList<String> getFileLines(File f, int i) {
		BufferedReader br = null;
		ArrayList<String> toReturn = new ArrayList<String>(i + 10);
		try {
			String line;
			br = new BufferedReader(new FileReader(f));
			while ((line = br.readLine()) != null) {
				toReturn.add(line);
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return toReturn;
	}

	public static void cloneRepository(String localPath, String remotePath) {

		File f = new File(localPath);

		if (f.exists()) {
			System.out.println("Repository exists.");
			System.out
					.println("Deleting repository and cloning a new copy....");
			try {
				FileUtils.deleteDirectory(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out
					.println("Repository doesn't exist.  Cloning new repository....");
		}
		try {

			Git.cloneRepository().setURI(remotePath).setDirectory(f).call();
			System.out.println("Clone complete.");
		} catch (InvalidRemoteException e) {
			e.printStackTrace();
		} catch (TransportException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

	/*
	 * produces an array of absolute filenames with the given extension (for
	 * example, '.java')
	 */
	public static String[] produceFileList(String localPath,
			String languagePostfix) {
		LinkedList<String> languageFiles = new LinkedList<String>();
		LinkedList<String> nonLanguageFiles = new LinkedList<String>();

		String[] opposingCommand = {
				"/bin/sh",
				"-c",
				"find "
						+ localPath
						+ " \\( -type f -not -name \"*"
						+ languagePostfix
						+ "\" \\) -and \\( -type f -not -name \".*\" \\) | grep -v \"/\\.\"" };

		String[] command = {
				"/bin/sh",
				"-c",
				"find " + localPath + " -type f -name \"*" + languagePostfix
						+ "\"" };

		if (Main.verbose || Main.test) {
			System.out.println("Find language specific files: " + command[2]);

		}
		if (Main.test) {
			System.out
					.println("Find other files, excluding hidden files and directories: "
							+ opposingCommand[2]);
		}

		try {

			Process p = Runtime.getRuntime().exec(command);
			Scanner sc = new Scanner(p.getInputStream());

			while (sc.hasNext()) {
				languageFiles.add(sc.nextLine());
			}

			if (Main.test) {
				Process p2 = Runtime.getRuntime().exec(opposingCommand);
				Scanner sc2 = new Scanner(p2.getInputStream());

				while (sc2.hasNext()) {
					nonLanguageFiles.add(sc2.nextLine());
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		int languageFileCount = 0;
		if (Main.verbose || Main.test) {
			languageFileCount = languageFiles.size();
			System.out.println("Language file count: " + languageFileCount);
		}

		if (Main.test) {
			int nonLanguageFileCount = nonLanguageFiles.size();
			System.out.println("Non-Language file count: "
					+ nonLanguageFileCount);
			System.out.println("Total file count: "
					+ (languageFileCount + nonLanguageFileCount));
		}

		Object[] objectArray = languageFiles.toArray();
		return Arrays.copyOf(objectArray, objectArray.length, String[].class);

	}

	public static LinkedList<String> extractReferences(String fileAddress,
			String referencingToken) {
		LinkedList<String> returner = new LinkedList<String>();
		File file = new File(fileAddress);
		Scanner input = null;
		String found;
		String edited;
		try {
			input = new Scanner(file);
		    while(input.hasNext()) {
		    	if(input.next().startsWith("import")){
		    		found = input.next();
		    		
		    		if(found.endsWith(";")){
			    		edited = found.substring(0, found.length()-1);
		    		}else{
		    			edited=found;
		    		}
		    		returner.add(edited);
		    	}
		    }
		    input.close();
//			input = new Scanner(file);
//			while (input.hasNextLine()) {
//				String found = input.nextLine().trim();
//				if(Main.verbose){
//					System.out.println("found: "+found+" referencingToken"+referencingToken+" startsWith?: "+found.startsWith(referencingToken));
//				}
//				if (found.startsWith(referencingToken)) {
//					String edited;
//					if(Main.test){
//						edited = found.substring(0,
//								found.length() - 1);
//						System.out.println(" edited1: "+edited);
//						edited = edited.trim();
//						System.out.println(" edited2: "+edited);
//						edited = edited.substring(referencingToken.length());
//						System.out.println(" edited3: "+edited);
//					}else{
//						edited = found.substring(referencingToken.length(),found.length()-1).trim();
//					}
//					if (Main.verbose) {
//						System.out.println("found: " + found + " edited: "
//								+ edited);
//					}
//					returner.add(edited);
//				}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return returner;
	}

	public TreeSet<String> getProjects(String language){
		TreeSet<String> theSet = new TreeSet<String>();
		
		for (int k = 1; k < 2; k++) {

			try{
				// the url for a given letter and page
				char currentLetter = (char)(Main.getGen().nextInt(26) + 'a');
				URL url = new URL("https://api.github.com/legacy/repos/search/"
						+ currentLetter + "?language="+language+"&start_page=" + k);

				// getting the json array from that letter and page url
				InputStream is = url.openStream();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is,
						Charset.forName("UTF-8")));
				StringBuilder sb = new StringBuilder();
				int cp;
				while ((cp = rd.read()) != -1) {
					sb.append((char) cp);
				}
				String jsonText = sb.toString();
				JSONObject json = new JSONObject(jsonText);

				JSONArray currentRepositories = json.getJSONArray("repositories");

				// get the username and name, add to the set
				for (int j = 0; j < currentRepositories.length(); ++j) {
					String username = currentRepositories.getJSONObject(j)
							.getString("username");
					String name = currentRepositories.getJSONObject(j).getString(
							"name");
					theSet.add(username + "/" + name);
				}
				is.close();
				rd.close();
			}catch(Exception e){
				e.printStackTrace();
			}

		}

		//print out all the projects you got
		if (Main.test) {
			Iterator<String> it = theSet.iterator();
			int projectNumber = 1;

			while (it.hasNext()) {
				String value = (String) it.next();
				System.out.println(projectNumber + ": " + value);
				projectNumber++;
			}

			System.out.println(theSet.size());
		}
		return theSet;
	}

}
