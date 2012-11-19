package data;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

@SuppressWarnings("serial")
public class ClassListMap extends
		HashMap<String, Pentuple<File, Integer, String, String,String>> {
	public static final String[][] supportedLanguages = {
			{ "java 1.6",
					"/Users/carlchapman/Desktop/SE_416/japiSets/japi6.txt",
					".java", "import","Java" },
			{ "java 1.7",
					"/Users/carlchapman/Desktop/SE_416/japiSets/japi7.txt",
					".java", "import","Java" } };

	public ClassListMap() {
		for (String[] sa : supportedLanguages) {
			try {
				File f = new File(sa[1]);
				put(sa[0], new Pentuple<File, Integer, String, String,String>(f,
						countLines(f), sa[2], sa[3],sa[4]));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String[] getLanguages() {
		String[] toReturn = new String[supportedLanguages.length];
		for (int i = 0; i < supportedLanguages.length; i++) {
			toReturn[i] = supportedLanguages[i][0];
		}
		return toReturn;
	}

	private static int countLines(File f) {
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(f));
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n')
						++count;
				}
			}
			is.close();
			return (count == 0 && !empty) ? 1 : count;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
}
