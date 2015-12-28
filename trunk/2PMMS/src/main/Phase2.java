
package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;


public class Phase2 {

	String SortedFileName;

	public Phase2(Path sourceFolder, String outputFileName, int sublists) throws IOException{
		
		SortedFileName = outputFileName;			
		System.out.println("TO      "+SortedFileName);
		
		
		//String sourceFolder = "C:\\Users\\r_honvo\\Downloads\\";
		
		int memorySize = 950000;//6;//8;
		int blockSize =50;
		int numberOfSublist =sublists;
		int numberOfBuffers = memorySize/blockSize;
		int numberOfAvailableInputBuffers = numberOfBuffers-1;
		int numberOfPass=1;
		if(numberOfBuffers>2)
			numberOfPass =  (int) Math.ceil((double) Math.log(numberOfSublist)/Math.log(numberOfBuffers-1));
		
		System.out.println(numberOfPass+" passes in second Phase");
//		System.out.println("__ __ ____________________________");
		
		int numberOfInputBuffers=0;//Math.min(numberOfSublist, numberOfAvailableInputBuffers);
		int[][] buffer = new int[numberOfInputBuffers][blockSize];
		
		//ArrayList<Integer>[] Arraybuffer = new ArrayList[numberOfInputBuffers];//();
		ArrayList<Integer>[] Arraybuffer = (ArrayList<Integer> []) new ArrayList[numberOfInputBuffers];
		
		int[] outputBuffer=new int[blockSize];
		ArrayList<Integer> ArrayOutbuffer=new ArrayList<Integer>(blockSize);
		
		int numberOfRuns =0;		
		for(int p=0;p<numberOfPass;p++)
		{			
			
//			System.out.println("PASS ="+p);
						
			numberOfInputBuffers=Math.min(numberOfSublist, numberOfAvailableInputBuffers);
//			System.out.println("INPUITBUFFER ="+numberOfInputBuffers);
			if(p==numberOfPass-1)
			{
//				System.out.println("LAST PASS ="+p);
				
//				System.out.println("LAST PASS ="+numberOfSublist);
				numberOfInputBuffers=numberOfSublist;
				buffer = new int[numberOfInputBuffers][blockSize];
				Arraybuffer = (ArrayList<Integer> [])new ArrayList[numberOfInputBuffers];
			}
			
			
//			System.out.println("numberOfSublist ="+numberOfSublist);
//			System.out.println("numberOfInputBuffers ="+numberOfInputBuffers);
			numberOfRuns = (int)Math.ceil((double)numberOfSublist/numberOfInputBuffers);
//			System.out.println("numberOfRun ="+numberOfRuns);
			
			for(int r =1;r<=numberOfRuns;r++)
			{	
//				System.out.println("RUN ="+r);
				
//				System.out.println("--------create="+ (1+p) +" number"+(r-1));
				
				FileWriter fw = new FileWriter(sourceFolder + "\\" + "sublist_"+ (p+1) +"_" + (r-1) + ".txt");
				PrintWriter pw = new PrintWriter(fw);
				
				
				int numberOfInputBuffersInRun=numberOfInputBuffers;
				if(r==numberOfRuns && p<numberOfPass-1)	
				{	
//					System.out.println("voyons rfinal ="+r);
					
					numberOfInputBuffersInRun=numberOfInputBuffers;
					if(numberOfSublist%numberOfInputBuffers!=0)
						numberOfInputBuffersInRun=numberOfSublist%numberOfInputBuffers;					
				}
//				System.out.println("pass  "+p+"  run  "+r+" use "+numberOfInputBuffersInRun+" buff");
						
				
				BufferedReader[] inputBReader = new BufferedReader[numberOfInputBuffersInRun];//[numberOfInputBuffers];
				boolean[] allEmptys=new boolean [numberOfInputBuffersInRun];//[numberOfInputBuffers];
				buffer = new int[numberOfInputBuffersInRun][blockSize];
				Arraybuffer = (ArrayList<Integer> [])new ArrayList[numberOfInputBuffersInRun];
				
				for(int f = 0;f<numberOfInputBuffersInRun;f++)
				{
					int fileNumber = (r-1)*numberOfInputBuffers+f;
					
//					System.out.println("--------pass="+ p +" number"+fileNumber);
					BufferedReader br = new BufferedReader(new FileReader(sourceFolder+ "\\" +"sublist_"+p+"_"+fileNumber + ".txt"));
					inputBReader[f]=br;
					allEmptys[f]=false;
				}
				
				String line = null;
				for(int j =0;j<numberOfInputBuffersInRun;j++)
				{ 
					int fileNumber = (r-1)*numberOfInputBuffersInRun+j;
					Arraybuffer[j]=new ArrayList<Integer>(blockSize);
					
					
//					System.out.println("READING FILE ="+ p +"_"+fileNumber);
					try
					{							
						for (int b = 0; b < blockSize; b++)
				 	    {							
							 line = inputBReader[j].readLine();
							 if (line != null)
							 {
								 Arraybuffer[j].add(Integer.parseInt(line));
								 buffer[j][b] = Integer.parseInt(line);
							 }								
							 else
							 {
								 allEmptys[j]=false;									 
								 buffer[j][b] = Integer.MAX_VALUE;
							 }	
				 	    }	
						
//						System.out.print("buffer  "+j+"  -------------  VOICI CE QUE J AI LU ----->");
//						for (int b = 0; b < blockSize; b++)					 	    								
//							 System.out.print("  "+buffer[j][b]+"  ");							
//						System.out.println();				
													
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	   
											
				}	
				
				boolean allEmpty=false;
				while(!allEmpty)//(d<4)//(!allEmpty)
				{
					//String line = null;
					int nbb=1;
//					System.out.println("================================================="+nbb + "  READING "+"  "+numberOfInputBuffersInRun);				
			
									
					int min=Integer.MAX_VALUE;
					int bufferHoldingMin=0;
					
					for (int b = 0; b < blockSize; b++)	
					{							
						int indic = 0;
						min=Integer.MAX_VALUE;				
						for(int f = 0;f<numberOfInputBuffersInRun;f++)
						{										
							if (Arraybuffer[f].size()!=0 && min > Arraybuffer[f].get(0))
						    {
							   indic=1;
						       min = Arraybuffer[f].get(0);						       
//						       System.out.println("CURRENT B----->   "+f+"   "+Arraybuffer[f].get(0)+"MINIM ----->"+min);
						       bufferHoldingMin=f;						       
						    }	 
												
						}
						if(Arraybuffer[bufferHoldingMin].size()!=0)
							Arraybuffer[bufferHoldingMin].remove(0);
						
						if(Arraybuffer[bufferHoldingMin].size()==0)
						{
//							System.out.println("CURRENT BUFF EMPTY ----->"+bufferHoldingMin);
							for (int bb = 0; bb < blockSize; bb++)
					 	    {							
								 line = inputBReader[bufferHoldingMin].readLine();
								 if (line != null)
								 {
									 Arraybuffer[bufferHoldingMin].add( Integer.parseInt(line));
									 buffer[bufferHoldingMin][bb] = Integer.parseInt(line);
								 }									
								 else
								 {
									if(Arraybuffer[bufferHoldingMin].size()==0)
										 allEmptys[bufferHoldingMin]=true;									 
									 buffer[bufferHoldingMin][bb] = Integer.MAX_VALUE;
								 }									
					 	    }	
							
							
							
						}
						else
//							System.out.println("CURRENT BUFF NOT EMPTY ----->"+bufferHoldingMin);
						
						
						if(indic ==1)
						{
							outputBuffer[b]=min;
							ArrayOutbuffer.add(min);
//							System.out.println("ArrayOutbuffer add----->"+outputBuffer[b]+"arrayoutlast ----->"+ArrayOutbuffer.get(ArrayOutbuffer.size()-1));										
						}				
					}
					
					
			 	   // for (int k = 0; k < blockSize; k++)
			 	    //	 pw.println(outputBuffer[k]);		
			 	    
			 	   /* for (int k = 0; k < ArrayOutbuffer.size(); k++)
		 	    	 {
		 	    		pw.println(ArrayOutbuffer.get(0));
		 	    		ArrayOutbuffer.remove(0);
		 	    	 }*/
					while(ArrayOutbuffer.size()>0)
					{
						pw.println(ArrayOutbuffer.get(0));
		 	    		ArrayOutbuffer.remove(0);
					}
					
					
					
					
					
					
					
					
					
					
					
					nbb++;
					allEmpty=allEmptys[0];
					for(int f = 0;f<numberOfInputBuffersInRun;f++)					
						allEmpty = allEmpty && allEmptys[f];									
											
//					 System.out.println("  allEmpty ----->"+allEmpty);
				}		
				
				
				
				
				for(int f = 0;f<numberOfInputBuffersInRun;f++)
				{
					inputBReader[f].close();
				}
				pw.close();
		 	    fw.close();
				
			}				
			numberOfSublist = numberOfRuns;
//			System.out.println("pass  finish =   "+p);
//			System.out.println("________________________________");
		}
		
//		System.out.println(numberOfSublist+" Sublist so it s the end");
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

}
