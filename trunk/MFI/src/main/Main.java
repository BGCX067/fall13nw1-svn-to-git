package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import model.Dataset;
import model.Itemset;
import model.Transaction;
import algorithm.LCM;

public class Main {

	public static void main(String[] args) throws IOException {		
		
			/* Checking memory */
		 	Runtime runtime = Runtime.getRuntime();

		  	StringBuilder sb = new StringBuilder();
		    long maxMemory = runtime.maxMemory();
		    long allocatedMemory = runtime.totalMemory();
		    long freeMemory = runtime.freeMemory();
		   //-Xms5m -Xmx5m 
		    
		    
		    
			String sourceFile = "list.dat";
			Path sourceFolder = Paths.get("C:\\Temp\\p1data");			
		    String outputFileName = "SORTED_"+sourceFile;//"C:\\Temp\\p1data\\small.out.dat";
		    
		    int sublists;
		    
		    StopWatch timer = new StopWatch();
			timer.start();
			
			
			String datasetPath;
	    	datasetPath = "C:\\Temp\\VT\\Small.dat";	    	
	    	
	    	LCM lcm = new LCM(Double.valueOf(2), Dataset.getInstance(datasetPath));

	        List<Itemset> closedFrequentItems = lcm.run();

	        System.out.println("=====================================");
	        System.out.println("Total count: "+closedFrequentItems.size());
	        
	        
	        //new
	        
	        int n = closedFrequentItems.size();
	        for(int i = 0; i < n ; i++)
	          System.out.println( closedFrequentItems.get( i ) );

	        
	        File factorizedDataset = new File("C:\\Temp\\VT","Small_final.dat");
	        FileWriter fstream = new FileWriter(factorizedDataset);
            BufferedWriter out = new BufferedWriter(fstream);
            try {
                //for(Transaction transaction : closedFrequentItems.()) {
                  //  if(transaction.toString()!=null && !transaction.toString().isEmpty()) {
            	
            	 for(int i = 0; i < n ; i++) {
       	         // System.out.println( closedFrequentItems.get( i ) );
            	 
                        out.write(String.valueOf(closedFrequentItems.get(i)));
                        out.newLine();
            	 }
                   // }

               // }

            } finally {
                //Close the output stream
                out.close();
            }
	        
	        
	        //new
	        
	        
	        
	        
	        System.out.println("=====================================");
			
			
			
			
		    //sublists = Phase1.sort(sourceFolder,sourceFile);
		    //Phase2 p2 = new Phase2(sourceFolder,outputFileName,sublists);
		    		    
		 	timer.stop();
		 	
		 	System.out.println("Elapsed Time bellow:");
		 	System.out.println(timer.getElapsedTime());	 
		
		 	System.out.println("MAX Memory");
		    System.out.println(maxMemory/1024);
		    System.out.println("Allocated Memory");
		    System.out.println(allocatedMemory/1024);
		    System.out.println("Free Memory");
		    System.out.println(freeMemory/1024);		    
		    /* Checking memory */
		 	
		 	//System.out.println("inp"+PhaseT.sort(sourceFolder,sourceFile)); 
		 	//System.out.println("out"+PhaseT.sort(sourceFolder,outputFileName));
		 	 
		 	
		 	
	}

}
