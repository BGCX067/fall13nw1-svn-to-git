package main;

public class MergeSort {

	 public static void sort(int array[],int lo, int n){
		   int low = lo;
		   int high = n;
		   if (low >= high) {
		   return;
		   }
		 
		  int middle = (low + high) / 2;
		   sort(array, low, middle);
		   sort(array, middle + 1, high);
		   int end_low = middle;
		   int start_high = middle + 1;
		   while ((lo <= end_low) && (start_high <= high)) {
		   if (array[low] < array[start_high]) {
		   low++;
		   } else {
		   int Temp = array[start_high];
		   for (int k = start_high- 1; k >= low; k--) {
		   array[k+1] = array[k];
		   }
		   array[low] = Temp;
		   low++;
		   end_low++;
		   start_high++;
		   }
		   }
		   }  		 

}
