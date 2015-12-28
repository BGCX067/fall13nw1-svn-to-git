package main;
/* This file is copyright (c) 2008-2013 Philippe Fournier-Viger
* 
* This file is part of the SPMF DATA MINING SOFTWARE
* (http://www.philippe-fournier-viger.com/spmf).
* 
* SPMF is free software: you can redistribute it and/or modify it under the
* terms of the GNU General Public License as published by the Free Software
* Foundation, either version 3 of the License, or (at your option) any later
* version.
* 
* SPMF is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
* A PARTICULAR PURPOSE. See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with
* SPMF. If not, see <http://www.gnu.org/licenses/>.
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import main.Itemset;



/**
 * This is a bitset-based implementation of the ECLAT algorithm as proposed by ZAKI (2000).
 * <br/><br/>
 * 
 * See this article for details about ECLAT:
 * <br/><br/>
 * 
 * Zaki, M. J. (2000). Scalable algorithms for association mining. Knowledge and Data Engineering, IEEE Transactions on, 12(3), 372-390.
 * <br/><br/>
 * 
 * NOTE: This version implement TIDs sets as bit vectors.  Note however 
 * that Zaki have proposed other optimizations (e.g. diffset), not used here.
 *
 * @see Itemset
 * @see ITSearchTree
 * @author Philippe Fournier-Viger
 */
public class AlgoEclat_Bitset_saveToFile {

	// for statistics
	private long startTimestamp; // start time of the last execution
	private long endTimestamp; // end time of the last execution
	
	// the minsup parameter
	private int minsupRelative;
	
	// The vertical database
		// Key: item   Value : the bitset representing its tidset
	Map<Integer, BitSet> mapItemTIDS = new HashMap<Integer, BitSet>();
	
	
	int tidcount; // the number of transactions
	BufferedWriter writer = null; // object to write the output file
	BufferedWriter wpass1 = null; // object to write the output file
	//BufferedWriter wpass1b = null; // object to write the output file
	//BufferedWriter wpass1c = null; // object to write the output file
	private int itemsetCount; // the number of patterns found

	
	int[] x_freq;
	
	int x_freq_count;
	String[] lineSplited;
	int FREQ;
	double maxbufferforbig = 10000;
	/**
	 * Default constructor
	 */
	public AlgoEclat_Bitset_saveToFile() {
		
	}

	/**
	 * Run the algorithm.
	 * @param input an input file path of a transation database
	 * @param output an output file path for writing the result or if null the result is saved into memory and returned
	 * @param minsup the minimum suppport
	 * @throws IOException exception if error while writing the file.
	 */
	public void runAlgorithm(String input, String output, double minsup) throws IOException {
		
		Runtime runtime = Runtime.getRuntime();
		StringBuilder sb = new StringBuilder();
	    long maxMemory = runtime.maxMemory();
	    long allocatedMemory = runtime.totalMemory();
	    long freeMemory = runtime.freeMemory();
	    String pass1 = "pass1.txt";
	 
		
		// reset the number of itemset found to 0
		itemsetCount = 0;
		
		// record the start timestamp
		startTimestamp = System.currentTimeMillis();
		
		// prepare object to write output file
		writer = new BufferedWriter(new FileWriter(output)); 
		wpass1 = new BufferedWriter(new FileWriter(pass1));
		//wpass1B = new BufferedWriter(new FileWriter(pass1b)); 
		//wpass1C = new BufferedWriter(new FileWriter(pass1c)); 

		// (1) count the tid set of each item in the database in one database pass
		
		// This map will contain the tidset of each item
		// Key: item   Value :  tidset
		mapItemTIDS = new HashMap<Integer, BitSet>(); // id item, count
		BufferedReader reader = new BufferedReader(new FileReader(input));
		String line;
			
	

		//PASS 1 - Frequent items SINGLETONS
		int[] x = new int[2000];
		tidcount = 0;
		
		System.out.println("before  pass1------------------");
		maxMemory = runtime.maxMemory();
	    allocatedMemory = runtime.totalMemory();
	    freeMemory = runtime.freeMemory();
	 	System.out.println("MAX Memory");
	    System.out.println(maxMemory/1024);
	    System.out.println("Allocated Memory");
	    System.out.println(allocatedMemory/1024);
	    System.out.println("Free Memory");
	    System.out.println(freeMemory/1024);

	    //FIRST SCAN
	    while( ((line = reader.readLine())!= null)){ 			
   	
			lineSplited = line.split(" ");
			for(String stringItem : lineSplited){
				// convert the item to integer
				int item = Integer.parseInt(stringItem);				
				x[item] = x[item]+1;
			}
			tidcount++;
		}
	 
		reader.close();		
		lineSplited = null;
		line = null;
			
	/*	System.out.println("after pass1------------------");
		maxMemory = runtime.maxMemory();
	    allocatedMemory = runtime.totalMemory();
	    freeMemory = runtime.freeMemory();
	 	System.out.println("MAX Memory");
	    System.out.println(maxMemory/1024);
	    System.out.println("Allocated Memory");
	    System.out.println(allocatedMemory/1024);
	    System.out.println("Free Memory");
	    System.out.println(freeMemory/1024);*/	
				
	    
		this.minsupRelative = (int) Math.ceil(minsup * tidcount);
		System.out.println("this.minsupRelative" + this.minsupRelative);
		System.out.println("starting writing >=" + this.minsupRelative);
		
				
		//WRITING FREQUENT ITEMS (PASS 1 ) TO DISK - IMPROVE
		String space;
		reader = new BufferedReader(new FileReader(input));
		while( ((line = reader.readLine())!= null)){ 						
			
			space = "";
			lineSplited = line.split(" ");
			for(String stringItem : lineSplited){
				// convert the item to integer
				int item = Integer.parseInt(stringItem);	
				if (x[item] >= this.minsupRelative){
					wpass1.write(space + item);
					space = " ";
				}				
			}
			if (space == " ")
				wpass1.newLine();
		}
		wpass1.close();
		reader.close();
		line = null;
		lineSplited = null;
		System.out.println("finished writing");
		//---		
		
		int r = 0;

		for(int y=0;y<2000;y++){
			if (x[y] >= this.minsupRelative){
				r++;
			}			
		}
		System.out.println("count 1=  "+r );
			
		   
		x_freq = new int[r];
		int x_freq_i[][] = new int[r][4];

				
		int r2=0;
		System.out.println("count 2=  "+r );
		for(int y=0;y<2000;y++){
			if (x[y] >= this.minsupRelative){
			    //System.out.println(y + " at row" + r + "total trans = " + x[y]);
				x_freq[r2]=y;//x[y];	
				x_freq_i[r2][0] = y;
				x_freq_i[r2][1] = x[y];
				x_freq_i[r2][2] = (int) (x[y] +x[y]*0.10*(r-r2));// +(int)((x.length-r)/* *(0.10*x[y]) */);
				x_freq_i[r2][3] = r2;
				
				r2++;
			}			
		}
		
		
	
		
		x = null;
		
		for (int j = 0; j > x_freq_i.length; j++) {
		    for (int w = 1; w > x_freq_i.length - j; w++) {
		        if (x_freq_i[w - 1][2] > x_freq_i[w][2]) {
		            int temp = x_freq_i[w - 1][0];
		            int temp1 = x_freq_i[w - 1][1];
		            int temp2 = x_freq_i[w - 1][2];
		            int temp3 = x_freq_i[w - 1][3];
		            
		            x_freq_i[w - 1][0] = x_freq_i[w][0];//item
		            x_freq_i[w - 1][1] = x_freq_i[w][1];//total transactions
		            x_freq_i[w - 1][2] = x_freq_i[w][2];//index
		            x_freq_i[w - 1][3] = x_freq_i[w][3];//index
		            
		            x_freq_i[w][0] = temp;
		            x_freq_i[w][1] = temp1;
		            x_freq_i[w][2] = temp2;
		            x_freq_i[w][3] = temp3;

		        }
		    }
		  }
	    
				
		
		int root_guide = 0;
		
		System.out.println("before while root-guide------------------");
		maxMemory = runtime.maxMemory();
	    allocatedMemory = runtime.totalMemory();
	    freeMemory = runtime.freeMemory();
	 	System.out.println("MAX Memory");
	    System.out.println(maxMemory/1024);
	    System.out.println("Allocated Memory");
	    System.out.println(allocatedMemory/1024);
	    System.out.println("Free Memory");
	    System.out.println(freeMemory/1024);	
		
				
//-------------------------------------------------------
	    while (root_guide <  x_freq_i.length) //recheck
		{	
			//System.out.println("while1");
			ArrayList<Integer> a = new ArrayList<Integer>();
			int t = 0;
			int aux = root_guide;
			
			do{
				
				a.add(x_freq_i[aux][0]);
				t = t + x_freq_i[aux][2];
				aux++;
				
				if(aux >= x_freq_i.length)
					t=800000;
				
				//System.out.println(x_freq_i[aux][2]);
			//	System.out.println(x_freq_i[aux][0]);
				
			}while(t<800000);
			
		//	root_guide = aux;
			
			
			System.out.println(root_guide+1 + "/"+x_freq_i.length);
//			System.out.println(x_freq_i[aux-1][1]);
		
		
	/*		
		System.out.println("--------here----------");
	//	maxMemory = runtime.maxMemory();
	    allocatedMemory = runtime.totalMemory();
	    freeMemory = runtime.freeMemory();
	 //	System.out.println("MAX Memory");
	  //  System.out.println(maxMemory/1024);
	    System.out.println("Allocated Memory");
	    System.out.println(allocatedMemory/1024);
	    System.out.println("Free Memory");
	    System.out.println(freeMemory/1024);
	   
			*/

	    int lineok = 0;
	    int itemfreq = 0;
	    
	    
	    //DATA FROM LAST ITEM IN THE LIST
//	   System.out.println("item"+ a.get(aux-1));
//	   System.out.println("frequency"+ x_freq_i[aux-1][1]);
//	   System.out.println("index"+ x_freq_i[aux-1][2]);
	//    System.out.println("total x_freq[FREQ1]("+x_freq[FREQ+0]+")= " + x[x_freq[FREQ+0]]);
	    
	 
	 
	   //extra-passes>>>
	   if (a.size() ==1){
		   
		   System.out.println("BIG GUY");//--------------------------------------------
		   		   
		   int nparts = (int) (Math.ceil(t/maxbufferforbig)); 
		 //  int p1 = 0;
		   
		  // int p2 = (int) (Math.ceil((double)100/nparts));
		  // System.out.println("p2=" + p2);
		  // p2 = (r-1)/nparts;
		  // int factor = (int) (Math.ceil((double)(r-1)/nparts));
		   int factor = (int) (Math.ceil((double)(r-x_freq_i[root_guide][3])/nparts));
		  // int p2 = factor;// Math.min(factor,r-1);
		   
		   int p1 = x_freq_i[root_guide][3];
		   int p2 = x_freq_i[root_guide][3] + factor;
		   
		   System.out.println("-----------------------------------p2=" + p2);
		   
		
		    System.out.println("at"+x_freq_i[root_guide][3]); 
		    System.out.println("nparts=" + nparts);
		    System.out.println("t=" + t);
			System.out.println("r=" + r);
			System.out.println("p1=" + p1 );
			System.out.println("p2=" + p2 );
			System.out.println("factor=" + factor );
		  
		   
			while (nparts!=0) {		
				
			/*	System.out.println("npart------------------");
			    allocatedMemory = runtime.totalMemory();
			    freeMemory = runtime.freeMemory();
			    System.out.println("Allocated Memory");
			    System.out.println(allocatedMemory/1024);
			    System.out.println("Free Memory");
			    System.out.println(freeMemory/1024);	
			    */
			
			p2=Math.min(p2,r-1);
			p1=Math.min(p1,r-1);
			
			
			
			 System.out.println("nparts=" + nparts);
			/*   System.out.println("p1=" + p1 );
				System.out.println("p2=" + p2 );*/
				
		    int newcount = 0;
		    tidcount = 0;	
		    reader = new BufferedReader(new FileReader(pass1));
		    while( ((line = reader.readLine())!= null)){ 						
			
		    	
		    	
				lineSplited = line.split(" ");
				lineok = 0;
				
				for(String stringItem : lineSplited){  //recheck
					
					int item = Integer.parseInt(stringItem);
					
					int z=0;
					
					
					if (lineok == 0 && a.contains(item)){
						lineok = 1; 
						x_freq_count++;
					}

						
						
					//if (lineok == 1 /*&& (a.contains(item))*/ && item >= x_freq[(int)((p1/100)*(r-1))] &&  (item < x_freq[(int)((p2/100)*(r-1))]  || (nparts ==1 && item <= x_freq[(int)((p2/100)*(r-1))] ))){
					//if (lineok == 1 && item >= x_freq[p1] &&  item < x_freq[p2]){ //(item < x_freq[p2]  || (nparts ==1 && item <= x_freq[r-1] ))){ 
					if (lineok == 1 && item >= x_freq[p1] &&  (item < x_freq[p2]  || (nparts ==1 && item <= x_freq[p2] ))){ 						
						newcount++;
						
									
						if 	(newcount < 40000){
						// get the tidset of the item
						BitSet tids = mapItemTIDS.get(item);						
						// add the tid of the current transaction to the tidset of the item
						if(tids == null){
							tids = new BitSet();
							mapItemTIDS.put(item, tids);
							
						}
						tids.set(tidcount);
						//System.out.println("tidcount="+tidcount);
						}
						  
				
					}	
					
			
				}
				lineSplited = null;			
				tidcount++;  // increase the transaction count
			}
		    
			reader.close();  // closed input file
			line = null;
			
		    //System.out.println(mapItemTIDS);
			
//			System.out.println("newcount=" + newcount);
	/*		System.out.println("x_freq_count = " + x_freq_count);*/
			x_freq_count = 0; //remove ???
			//newcount = 0;
			
		  	x = null;
		    
			//pass2B - removing from hash		  
		    int[] to_remove = new int[r];
		    int remc = 0;
			for(Entry<Integer, BitSet> entry2 : mapItemTIDS.entrySet()){
				int entryCardinality2 = entry2.getValue().cardinality();
				
				if(entryCardinality2 < minsupRelative){
					//System.out.println("removed" + entry2.getKey());
					//mapItemTIDS.remove(entry2.getKey());
					to_remove[remc] = entry2.getKey();
					remc++;
				}
				
			}
					
			for(int p=0;p<remc;p++){
				mapItemTIDS.remove(to_remove[p]);			
			}			
			
			
			p1 = p1 + factor;
			
			p2 = p2 + factor;
			p2=Math.min(p2,r-1);
			nparts--;
			
					if (p1>=(r-1)){ 
						nparts=0;
					}
				/*	else{
						System.out.println("p1" + p1);
						System.out.println("r-1" + (r-1));
					}*/
		
		   } //while big ones
		   
	   }  //if big ones
	   else { //small ones
	    
		   
		   System.out.println("SMALLS");//------------------------------------------------
		   
	    int newcount = 0;
	    tidcount = 0;	
	    reader = new BufferedReader(new FileReader(pass1));
	    while( ((line = reader.readLine())!= null)){ 						
		
			lineSplited = line.split(" ");
			lineok = 0;
			
			for(String stringItem : lineSplited){  //recheck
				
				int item = Integer.parseInt(stringItem);
				int z=0;
				
				//if (item ==x_freq[FREQ] || item ==x_freq[FREQ+0]){  //candidate line
				if (lineok == 0 && a.contains(item)){
					lineok = 1; 
					x_freq_count++;
				}
				
				//if (lineok == 1 && (item >=x_freq[FREQ] || item >=x_freq[FREQ+0]) && item<(x_freq[FREQ+(int)(0.50*(r-FREQ))])){
				if (lineok == 1){ // /*&& (a.contains(item))*/ && item > x_freq[(int)(0.0*r)] ){ //&& item<250){
				
					newcount++;
								
				if 	(newcount < 40000){
					// get the tidset of the item
					BitSet tids = mapItemTIDS.get(item);						
					// add the tid of the current transaction to the tidset of the item
					if(tids == null){
						tids = new BitSet();
						mapItemTIDS.put(item, tids);
						
					}
					tids.set(tidcount);					
					} ////
				}			
				stringItem = null;				
			}
			lineSplited = null;			
			tidcount++;  // increase the transaction count
		}
	    
		reader.close();  // closed input file
		line = null;
		
	    //System.out.println(mapItemTIDS);
		
	//	System.out.println("newcount=" + newcount);
/*		System.out.println("x_freq_count = " + x_freq_count);*/
		x_freq_count = 0; //remove ???
		newcount = 0;
		
	  	x = null;
	    
		//pass2B - removing from hash
	   // System.out.println("----");
	    int[] to_remove = new int[r];
	    int remc = 0;
		for(Entry<Integer, BitSet> entry2 : mapItemTIDS.entrySet()){
			int entryCardinality2 = entry2.getValue().cardinality();
			
			if(entryCardinality2 < minsupRelative){
				
				//mapItemTIDS.remove(entry2.getKey());
				to_remove[remc] = entry2.getKey();
				remc++;
			}
			
		}
				
		for(int p=0;p<remc;p++){
			mapItemTIDS.remove(to_remove[p]);			
		}

	   }
	   
	   	    
		// convert absolute minsup to relative minsup
		//this.minsupRelative = (int) Math.ceil(minsup * tidcount);
				
		// (2) create ITSearchTree with root node
		ITSearchTree tree = new ITSearchTree();
		// create root note with the empty set
		ITNode root = new ITNode(new HashSet<Integer>());
		tree.setRoot(root);
		// set its support to size of the database
		root.setTidset(null, tidcount);
	
	
		
		// (3) create childs of the root node.
		for(Entry<Integer, BitSet> entry : mapItemTIDS.entrySet()){
			int entryCardinality = entry.getValue().cardinality();
			
	//		System.out.println(" entry.getValue().cardinality()" );
	//		System.out.println(entry.getValue().cardinality());
			
			
			
		/*	System.out.println(" entry" );
			System.out.println(entry);
			
			System.out.println(" entryCardinality" );
			System.out.println(entryCardinality);
			*/
			//if the item is frequent
			if(entryCardinality >= minsupRelative){
				// create a new node for that item
				Set<Integer> itemset = new HashSet<Integer>();
				

				
				itemset.add(entry.getKey());
				
				ITNode newNode = new ITNode(itemset);
				// set its tidset as the tidset that we have calculated previously
								
				newNode.setTidset(entry.getValue(), entryCardinality);
				// set its parent as the root
				
				newNode.setParent(root);

				// add the new node as child of the root node
				root.getChildNodes().add(newNode); 
				
				
			}
		}
		
		// for optimization, sort the child of the root according to the support
	sortChildren(root);

		
	mapItemTIDS.clear();
	/*for(int p=0;p<mapItemTIDS.size();p++){
		mapItemTIDS.remove(to_remove[p]);			
	}*/
	
	
	
//	writer.write("[" + x_freq[FREQ] + "] #SUP: x " + x_freq_count);
//	writer.newLine();
	
		//	save(x_freq[FREQ]);
	
		// while there is at least one child node of the root
		while(root.getChildNodes().size() > 0){
			// get the first child node
			ITNode child = root.getChildNodes().get(0);
			// extend it
	//		System.out.println(child.getItemset().toString());
		//	System.out.println(child.getItemset().toString().replace("[", "").replace("]", ""));
			
			int opa = Integer.parseInt(child.getItemset().toString().replace("[", "").replace("]", ""));
			//System.out.println("opa=" + opa);
			if (
					a.contains(opa)
					/*child.getItemset().toString().equals("["+x_freq[FREQ] +"]")
					||
					child.getItemset().toString().equals("["+x_freq[FREQ+0] +"]")
					*/) {
				//System.out.println("VT");
			//	System.out.println("write=" + opa);
			extend(child);
			// save it
			//save2(child, x_freq[FREQ]);
			save(child);
			//System.out.println("-----------------------------------------------write=" + opa);
			// delete it
			}
			delete(child);
			
			
		}
		// record the end time for statistics
		endTimestamp = System.currentTimeMillis();
		// close output file
		
		//root_guide++;
		root_guide = aux;
		}	
		
		writer.close();
	}

	/**
	 * This is the "extend" method as described in the paper.
	 * @param currNode the current node.
	 * @throws IOException exception if error while writing to file.
	 */
	private void extend(ITNode currNode ) throws IOException {
		// loop over the brothers of that node
		for(ITNode brother : currNode.getParent().getChildNodes()){
			// if the brother is not the current node
			if(brother != currNode){
				// try to generate a candidate by doing the union
				// of the itemset of the current node and the brother
				ITNode candidate = getCandidate(currNode,brother);
				// if a candidate was generated (with enough support)
				if(candidate != null){
					// add the candidate as a child of the current node
					currNode.getChildNodes().add(candidate);
					candidate.setParent(currNode);
				}
			}
		}
		
		// for optimization, sort the child of the root according to the support
		sortChildren(currNode);

		// while the current node has child node
		while (currNode.getChildNodes().size() > 0) {
			// get the first child
			ITNode child = currNode.getChildNodes().get(0);
			extend(child);  // extend it (charm is a depth-first search algorithm)
			//save2(child,FreqItem); // save the node
			save(child); // save the node
			delete(child); // then delete it
		}
	}
	
	/**
	 * Generate a candidate by performing the union of the current node and a brother of that node.
	 * @param currNode the current node
	 * @param brother  the itemset of the brother node
	 * @return  a candidate or null if the resulting candidate do not have enough support.
	 */
	private ITNode getCandidate(ITNode currNode, ITNode brother) {

		// create list of common tids of the itemset of the current node
		// and the brother node
		BitSet commonTids = (BitSet) currNode.getTidset().clone();
		commonTids.and(brother.getTidset());
		// get the cardinality of the bitset
		int cardinality = commonTids.cardinality();
		
		// if the common tids cardinality is enough for the minimum support
		if(cardinality >= minsupRelative){
			// perform the union of the itemsets
			Set<Integer> union = new HashSet<Integer>(brother.getItemset());
			union.addAll(currNode.getItemset());
			// create a new node with the union
			ITNode node = new ITNode(union);
			// set the tidset as the intersection of the tids of both itemset
			node.setTidset(commonTids, cardinality);
			// return the node
			return node;
		}
		// otherwise return null because the candidate did not have enough support
		return null;
	}

	/**
	 * Delete a child from its parent node.
	 * @param child the child node
	 */
	private void delete(ITNode child) {
		child.getParent().getChildNodes().remove(child);
	}

	/**
	 * Save a node (as described in the paper).
	 * @param node the node
	 * @throws IOException
	 */
	private void save(ITNode node) throws IOException {
		
		// write the itemset to the file
		writer.write(node.getItemset().toString() + " #SUP: y " + node.size());
		//writer.write("[[" + x_freq[FREQ] + "]," + node.getItemset().toString() + "] #SUP: y " + node.size());
		
		writer.newLine();
		// increase the frequent itemset count
		itemsetCount++;
		//System.out.println("writing herereeeeeeeeeeeeeeeee=");
	}

	/*private void save2(ITNode node, int FreqItem ) throws IOException {
		// write the itemset to the file
		writer.write(node.addRoot(FreqItem)+ " #SUP: " + node.size());
		writer.newLine();
		// increase the frequent itemset count
		itemsetCount++;
	}*/
	
	/**
	 *  Sort the children of a node according to the order of support.
	 * @param node the node.
	 */
	private void sortChildren(ITNode node) {
		// sort children of the node according to the support.
		Collections.sort(node.getChildNodes(), new Comparator<ITNode>(){
			//Returns a negative integer, zero, or a positive integer as 
			// the first argument is less than, equal to, or greater than the second.
			public int compare(ITNode o1, ITNode o2) {
				return o1.getTidset().size() - o2.getTidset().size();
			}
		});
	}

	/**
	 * Print statistics about the algorithm execution to System.out.
	 */
	public void printStats() {
		System.out
				.println("=============  ECLAT - STATS =============");
		long temps = endTimestamp - startTimestamp;
		System.out.println(" Transactions count from database : "
				+ tidcount);
		System.out.println(" Frequent itemsets count : " + itemsetCount); 
		System.out.println(" Total time ~ " + temps + " ms");
		System.out
				.println("===================================================");
	}

}
