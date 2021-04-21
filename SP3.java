
package pxc190029;
import java.util.Random;
import java.util.Scanner;
/**
 * Performance comparison of different merge sort implementations. 
 * @author Jie Su
 * @author Pengchao Cai
 */
public class SP3 {
    public static Random random = new Random();
    public static int numTrials = 50; // number of trials.
	public static int[] wcInput; //inp array.
	public static int T; // Threshold.

    public static void main(String[] args) {
		int n = 0, choice = 0;
		while (true) {
			Scanner in = new Scanner(System.in);
			System.out.println("Please enter your choice: [0: take 0; 3: take 3; 4: take 4; 6: take 6; -1 to quit]");
			choice = in.nextInt();
			if (choice == -1) break;
			
			System.out.println("Please enter array size n: ");
			n = in.nextInt();
			
			System.out.println("Please enter threshold:");
			T = in.nextInt();
			
		    int[] arr = new int[n]; // initally inp. finally sorted
			wcInput = new int[n];
		    wcInitArray(wcInput, 0, wcInput.length);
		    
		    boolean choiceCorrect = true;
			Timer timer = new Timer();
			switch(choice) {
			case 0:
				for(int i=0; i<numTrials; i++) {
					System.out.println("Running trial " + i);
				    initArray(arr);
				    mergeSort0(arr);
			    }
				break;
			case 3:
			    for(int i=0; i<numTrials; i++) {
			    	System.out.println("Running trial " + i);
					initArray(arr);
					mergeSort3(arr);
			    }
			    break; 
			case 4:
			    for(int i=0; i<numTrials; i++) {
			    	System.out.println("Running trial " + i);
					initArray(arr);
					mergeSort4(arr);
			    }
			    break; 
			case 6:
			    for(int i=0; i<numTrials; i++) {
			    	System.out.println("Running trial " + i);
					initArray(arr);
					mergeSort6(arr);
			    }
			    break; 
			default: {
				System.out.println("Please enter correct choice!");
				choiceCorrect = false;
			} 
				break;
			}
			if (choiceCorrect) {
				timer.end();
				timer.scale(numTrials); // Calculate the average running time. 
				System.out.println("Choice: " + choice + "\n" + timer + "\n");
			} 
		}
	
    }
    /**
     * This insertionSort will be used in take 4 & take 6 when threshold is given. 
     * @param arr array to be sorted.
     * @param start start index.
     * @param end end index.
     */
    public static void insertionSort(int[] arr, int start, int end) {
    	for(int i = start+1; i <= end; i++){
    		int key = arr[i];
    		int j = i-1;
    		while(j >= start && arr[j] > key){
    			arr[j+1] = arr[j];
    			j--;
			}
    		j++;
    		arr[j] = key;
		}
    }
    /**
     * Base version of mergeSort without any optimization. 
     * @param arr array to be sorted. 
     */
    public static void mergeSort0(int[] arr) {
    	int length = arr.length;
    	mergeSortHelper(arr,0,length-1);
    }
    public static void mergeSortHelper(int[] arr, int start, int end){
    	if(start < end){
    		int mid = start + (end-start)/2;
    		mergeSortHelper(arr, start ,mid);
    		mergeSortHelper(arr,mid+1,end);
    		merge(arr,start, mid, end);
		}
	}
	public static void merge(int[] arr, int start, int mid, int end){
    	int p1 = start, p2 = mid+1;
    	int cur = start;
    	int[] tmp = new int[arr.length];
    	for(int i = start; i <= end; i++){tmp[i] = arr[i];}
    	while(p1 <= mid && p2 <= end){
    		if(tmp[p1] <= tmp[p2]){
    			arr[cur++] = tmp[p1++];
			}else{
				arr[cur++] = tmp[p2++];
			}
		}
    	while(p1 <= mid){arr[cur++] = tmp[p1++];}
    	while(p2 <= end){arr[cur++] = tmp[p2++];}
	}

	/**
	 * Take 3. Optimized version of merge sort.
	 * Auxiliary arrays is created (copied from input arr) only once outside the merge funciton.
	 * Switch auxiliary array and input array in each recursion call.
	 * @param arr array to be sorted.
	 */
	public static void mergeSort3(int[] arr) {
		int length = arr.length;
		int[] tmp = new int[length];
		for(int i = 0; i < length; i++){tmp[i] = arr[i];}
		mergeSortHelper3(arr,tmp,0,length-1);
    }
    public static void mergeSortHelper3(int[] arrA, int[] arrB, int start, int end){
		if(start < end){
			int mid = start + (end-start)/2;
			mergeSortHelper3(arrB, arrA, start ,mid);
			mergeSortHelper3(arrB, arrA,mid+1,end);
			merge3(arrA, arrB,start, mid, end);
		}
	}
	public static void merge3(int[] arrA, int[] arrB, int start,int mid, int end){
		int p1 = start, p2 = mid+1;
		int cur = start;
		while(p1 <= mid && p2 <= end){
			if(arrB[p1] <= arrB[p2]){
				arrA[cur++] = arrB[p1++];
			}else{
				arrA[cur++] = arrB[p2++];
			}
		}
		while(p1 <= mid){arrA[cur++] = arrB[p1++];}
		while(p2 <= end){arrA[cur++] = arrB[p2++];}
	}
	/**
	 * Take 4.
	 * Based on take 3; insertion sort will be called for a threshold.
	 * @param arr array to be sorted.
	 */
	public static void mergeSort4(int[] arr) {
		int length = arr.length;
		int[] tmp = new int[length];
		for(int i = 0; i < length; i++){tmp[i] = arr[i];}
		mergeSortHelper4(arr,tmp,0,length-1);
	}
	public static void mergeSortHelper4(int[] arrA, int[] arrB, int start, int end){
    	if((end-start+1) < T){
			insertionSort(arrA,start,end);
			return;
		}
		if(start < end){
			int mid = start + (end-start)/2;
			mergeSortHelper4(arrB, arrA, start ,mid);
			mergeSortHelper4(arrB, arrA,mid+1,end);
			merge3(arrA, arrB,start, mid, end);
		}
	}

	/**
	 * Take 6.
	 * Iiteration method that merges adjacent subarray each time. 
	 * Supports any array size.  
	 * @param arr array to be sorted.
	 */
	public static void mergeSort6(int[] arr){
    	int length = arr.length;
    	int[] arrB = new int[length];
		int[] inp = arr;
		if(length < T){insertionSort(arr,0,length-1);}
		int end = 0;
		for(int i = 0; i < length-T+1; i+=T){
			end = i+T-1;
			insertionSort(arr,i,i+T-1);
		}
		if(end <  length){insertionSort(arr,end+1,length-1);}
    	for(int i = T; i < length; i = 2*i){
    		for(int j = 0; j < length; j = j + 2*i){
    			int start = j; int endIndex = j+2*i-1;
    			if(length-start <= i) continue;
    			if(endIndex >= length){endIndex = length-1;}
    			merge3(arrB,inp,j,j+i-1,endIndex);
			}
    		int[] tmp = inp; inp = arrB; arrB = tmp;
		}
    	if(arr != inp){System.arraycopy(inp,0,arr,0,length);}
	}
	
	/**
	 * Initialize the array with worst case input. Nice algorithm.
	 * src: https://stackoverflow.com/questions/24594112/when-will-the-worst-case-of-merge-sort-occur
	 * @param arr stores elements of worst case.
	 * @param start start index.
	 * @param sz end index.
	*/
    public static void wcInitArray(int[] arr, int start, int sz){

        if (sz == 1) { arr[start] = 1; return;}
        int lsz = sz/2;
        //int rsz = (sz%2 == 0 ? lsz : lsz+1);
        wcInitArray(arr, start, lsz);
        wcInitArray(arr, start + lsz, (sz%2 == 0 ? lsz : lsz+1));
        for ( int i = start; i < start + lsz; i++){
            arr[i] *= 2;
        }
        for ( int i = start + lsz; i < start + sz; i++){
            arr[i] = arr[i] * 2 - 1;
        }
    }
	
	/**
	 * Initialize the array to be sorted. 
	 * @param arr array to be initialized with worst input.
	 */
	public static void initArray(int[] arr){
        System.arraycopy(wcInput, 0, arr, 0, arr.length);
	}
	


   /** Timer class for roughly calculating running time of programs
     *  @author rbk
     *  Usage:  Timer timer = new Timer();
     *          timer.start();
     *          timer.end();
     *          System.out.println(timer);  // output statistics
     */
    public static class Timer {
        long startTime, endTime, elapsedTime, memAvailable, memUsed;
        boolean ready;

        public Timer() {
            startTime = System.currentTimeMillis();
            ready = false;
        }

        public void start() {
            startTime = System.currentTimeMillis();
            ready = false;
        }

        public Timer end() {
            endTime = System.currentTimeMillis();
            elapsedTime = endTime-startTime;
            memAvailable = Runtime.getRuntime().totalMemory();
            memUsed = memAvailable - Runtime.getRuntime().freeMemory();
            ready = true;
            return this;
        }

        public long duration() { if(!ready) { end(); }  return elapsedTime; }

        public long memory()   { if(!ready) { end(); }  return memUsed; }

	public void scale(int num) { elapsedTime /= num; }
	
        public String toString() {
            if(!ready) { end(); }
            return "Time: " + elapsedTime + " msec.\n" + "Memory: " + (memUsed/1048576) + " MB / " + (memAvailable/1048576) + " MB.";
        }
    }
    
    /** @author rbk : based on algorithm described in a book
     */

    /* Shuffle the elements of an array arr[from..to] randomly */
    public static class Shuffle {
	
	public static void shuffle(int[] arr) {
	    shuffle(arr, 0, arr.length-1);
	}

	public static<T> void shuffle(T[] arr) {
	    shuffle(arr, 0, arr.length-1);
	}

	public static void shuffle(int[] arr, int from, int to) {
	    int n = to - from  + 1;
	    for(int i=1; i<n; i++) {
		int j = random.nextInt(i);
		swap(arr, i+from, j+from);
	    }
	}

	public static<T> void shuffle(T[] arr, int from, int to) {
	    int n = to - from  + 1;
	    Random random = new Random();
	    for(int i=1; i<n; i++) {
		int j = random.nextInt(i);
		swap(arr, i+from, j+from);
	    }
	}

	static void swap(int[] arr, int x, int y) {
	    int tmp = arr[x];
	    arr[x] = arr[y];
	    arr[y] = tmp;
	}
	
	static<T> void swap(T[] arr, int x, int y) {
	    T tmp = arr[x];
	    arr[x] = arr[y];
	    arr[y] = tmp;
	}

	public static<T> void printArray(T[] arr, String message) {
	    printArray(arr, 0, arr.length-1, message);
	}

	public static<T> void printArray(T[] arr, int from, int to, String message) {
	    System.out.print(message);
	    for(int i=from; i<=to; i++) {
		System.out.print(" " + arr[i]);
	    }
	    System.out.println();
	}
    }
}

