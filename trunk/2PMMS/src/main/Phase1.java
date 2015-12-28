package main;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public final class Phase1 {

	public static int sort(Path sourceFolder, String sourceFile) {
		
		Path sourceFilePath = Paths.get(sourceFolder + "\\" + sourceFile);
		
		/**
		 * Best method to read
		 *  -  newBufferedReader(Path, Charset) > http://docs.oracle.com/javase/tutorial/essential/io/file.html
		 *  -  Randon Access > http://docs.oracle.com/javase/tutorial/essential/io/rafs.html
		 * */	

		final int memorySize = 750000;
		
		int dataSize =0;		
		int[] buffer = new int[memorySize];
		int sublists = 0;
			 	
	 	Charset charset = Charset.forName("US-ASCII");
	 	try (BufferedReader reader = Files.newBufferedReader(sourceFilePath, charset)) {
	 	    String line = null;
	 	    	 	    
	 	    while ((line = reader.readLine()) != null) {  		 	    	
	 	  	    		
	 	    		buffer[dataSize] = Integer.parseInt(line);	 	    		
	 	    		
	 	    		if (dataSize == memorySize-1){
	 	    			
				 	    Arrays.sort(buffer,0,dataSize);
				 	    				 			 	    
				 	    FileWriter fw = new FileWriter(sourceFolder + "\\" + "sublist_0_" + sublists + ".txt");
				 	    PrintWriter pw = new PrintWriter(fw);
				 	     for (int k = 0; k < dataSize; k++)
				 	    	 pw.println(buffer[k]);		
				 	    pw.close();
				 	    fw.close();
				 	    
				 	   sublists++;
				 	    
				 	    //Reinitialize Data Size
				 	    dataSize = 0;				 	    
	 	    		}
	 	    		else {
	 	    			dataSize++;
	 	    		} 
	 	    } //while
	 	    
	 	    //Writing the remaining records to the last subslist
	 	    if(dataSize != 0){		 	  
		 	    
		 	    Arrays.sort(buffer,0,dataSize-1);
		    
		 	    FileWriter fw = new FileWriter(sourceFolder + "\\" + "sublist_0_" + sublists + ".txt");
		 	    PrintWriter pw = new PrintWriter(fw);
		 	     for (int k = 0; k < dataSize; k++)
		 	    	 pw.println(buffer[k]);		
		 	    pw.close();
		 	    fw.close();	 
		 	    
		 	    sublists++;
	 	    }	     
	 	    	 	   	 	
	 	   reader.close();
	 	} catch (IOException x) {
	 	    System.out.println("File not found!");
	 	}
	  
	 	return sublists;	 			
	}	
	
}
