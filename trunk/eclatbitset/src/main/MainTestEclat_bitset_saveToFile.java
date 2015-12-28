package main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import main.AlgoEclat_Bitset_saveToFile;


public class MainTestEclat_bitset_saveToFile {

	public static void main(String [] arg) throws IOException{
		// Loading the binary context
	//	String input = fileToPath("dw.txt");  // the database
	//	String input = fileToPath("Small.dat");  // the database
	//	String input = fileToPath("Medium.dat");  // the database
		String input = fileToPath("Large.dat");  // the database
	//	String output = ".//output.txt";  // the path for saving the frequent itemsets found
		
		double minsup = 0.02; // means a minsup of 2 transaction (we used a relative support)
		
		// Applying the ECLAT algorithm
		AlgoEclat_Bitset_saveToFile algo = new AlgoEclat_Bitset_saveToFile();
		//algo.runAlgorithm(input, output, minsup);
		
		int n = 2;
		String sub_input =""; 
	//	String input = "Small.dat";
		String output = "output.txt";
		
		String sub_output =""; 
		/*for(int i = 1;i<=n;i++)
		{
			
			sub_input =  fileToPath(i+"_"+input);
			sub_output =  ".//"+i+"_"+output;
			//AlgoEclat_Bitset_saveToFile algo = new AlgoEclat_Bitset_saveToFile();
			algo.runAlgorithm(sub_input, sub_output, minsup);
			
		}*/
		
		
		algo.runAlgorithm(input, output, minsup);
		
		
		/* Checking memory */
	 	Runtime runtime = Runtime.getRuntime();

	  	StringBuilder sb = new StringBuilder();
	    long maxMemory = runtime.maxMemory();
	    long allocatedMemory = runtime.totalMemory();
	    long freeMemory = runtime.freeMemory();
	 	System.out.println("MAX Memory");
	    System.out.println(maxMemory/1024);
	    System.out.println("Allocated Memory");
	    System.out.println(allocatedMemory/1024);
	    System.out.println("Free Memory");
	    System.out.println(freeMemory/1024);	
	   //-Xms5m -Xmx5m 				
	
		algo.printStats();

	}
	
	public static String fileToPath(String filename) throws UnsupportedEncodingException{
		URL url = MainTestEclat_bitset_saveToFile.class.getResource(filename);
		 return java.net.URLDecoder.decode(url.getPath(),"UTF-8");
	}
}
