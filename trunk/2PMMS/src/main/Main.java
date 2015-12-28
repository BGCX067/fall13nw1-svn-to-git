package main;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

	public static void main(String[] args) throws IOException {		
		
		 	 /* Runtime runtime = Runtime.getRuntime();

		  	StringBuilder sb = new StringBuilder();
		    long maxMemory = runtime.maxMemory();
		    long allocatedMemory = runtime.totalMemory();
		    long freeMemory = runtime.freeMemory();
		    
		    System.out.println("MAX Memory");
		    System.out.println(maxMemory/1024);
		    System.out.println("Allocated Memory");
		    System.out.println(allocatedMemory/1024);
		    System.out.println("Free Memory");
		    System.out.println(freeMemory/1024);	*/	    
		    
			StopWatch timer = new StopWatch();
			timer.start();
		    	    
		    
			/* SOURCE FILE / FOLDER */
			String sourceFile = "medium.dat";
			Path sourceFolder = Paths.get("C:\\Temp\\p1data");			
		    String outputFileName = "C:\\Temp\\p1data\\small.out.dat";
		    
		    int sublists;
		    sublists = Phase1.sort(sourceFolder,sourceFile);
		    Phase2 p2 = new Phase2(sourceFolder,outputFileName,sublists);
		    		    
		 	timer.stop();
		 	
		 	System.out.println("Elapsed Time bellow:");
		 	System.out.println(timer.getElapsedTime());	 
		
	}

}
