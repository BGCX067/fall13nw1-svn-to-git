package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import main.AlgoCFPGrowth_saveToFile;

/**
 * Example of how to use the CFPGrowth algorithm, from the source code.
 */
public class MainTestCFPGrowth {

	public static void main(String[] arg) throws FileNotFoundException,
			IOException {
		String database = fileToPath("contextCFPGrowth.txt");
		//database = fileToPath("Small.dat");
		database = fileToPath("Medium.dat");
		
		String output = ".//output.txt";
		String MISfile = fileToPath("MIS.txt");
		
	
		
		// Applying the CFPGROWTH algorithmMainTestFPGrowth.java
		AlgoCFPGrowth_saveToFile algo = new AlgoCFPGrowth_saveToFile();
		algo.runAlgorithm(database, output, MISfile);
		algo.printStats();
	}

	public static String fileToPath(String filename)
			throws UnsupportedEncodingException {
		URL url = MainTestCFPGrowth.class.getResource(filename);
		return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
	}
}
