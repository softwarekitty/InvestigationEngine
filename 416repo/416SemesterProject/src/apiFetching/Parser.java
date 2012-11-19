package apiFetching;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.TreeSet;

public class Parser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("start time: " + System.currentTimeMillis());
		
		String[] filenames = {"japi6.txt","japi7.txt"};
		String[] japiPath = { "/Users/carlchapman/Desktop/SE_416/docs1.6/api/allclasses-noframe.html","/Users/carlchapman/Desktop/SE_416/docs1.7/api/allclasses-noframe.html" };

		@SuppressWarnings("unchecked")
		TreeSet<String>[] japiSet = (TreeSet<String>[]) Array.newInstance(
				new TreeSet<String>().getClass(), japiPath.length);

		FileReader reader = null;

		for (int i = 0; i < japiPath.length; i++) {
			japiSet[i] = new TreeSet<String>();
			try {
				//int counter = 0;
				reader = new FileReader(japiPath[i]);
				BufferedReader br = new BufferedReader(reader);
				String s;
				boolean haveReachedList = false;

				while ((s = br.readLine()) != null) {
					if (!haveReachedList) {
						haveReachedList = s.startsWith("<TD") || s.startsWith("<h1 class=");
					} 
					else if (s.toLowerCase().contains("href")) {
						//counter++;
						String[] tokens = s.split("\"");
						String japiToken = tokens[1];

						// remove the ".html" suffix
						japiToken = japiToken.substring(0,
								japiToken.length() - 5);

						// put asterix version into japiSet
						String[] pathParts = japiToken.split("/");
						String asterixString = "";
						for (int j = 0; j < pathParts.length - 1; j++) {
							asterixString += pathParts[j] + ".";
						}
						// note adding identical strings will not increase size of set
						japiSet[i].add(asterixString + "*");
						
						// put the token into set
						japiSet[i].add(japiToken.replace('/', '.'));
					}
				}
				//System.out.println(" path counter: " + counter + " size of japiSet[" + i + "]: " + japiSet[i].size());
				br.close();
				reader.close();
				japiSetToFile(filenames[i],japiSet[i]);
			} catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		System.out.println("end time: " + System.currentTimeMillis());
	}
	
	public static void japiSetToFile(String filename, TreeSet<String> japiSet){
		String prefix = "/Users/carlchapman/Desktop/SE_416/japiSets/";
		try{
			File file = new File(prefix+filename);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for(String s:japiSet){
				bw.write(s+"\n");
			}
			bw.close();
			System.out.println("wrote file: "+filename);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
