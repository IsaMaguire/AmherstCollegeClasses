import java.util.concurrent.RecursiveAction;
import java.util.Arrays;

class QSTask extends RecursiveAction {

    private float[] data;
    private int farLeft;
    private int farRight;
    private final int cutoff = 1000;

    public QSTask (float[] data, int i, int j) {
        this.data = data;
        this.farLeft = i;
        this.farRight = j;
    }	

    /**
     The below method should split a subsection of the larger array into values greater than or less than
     a pivot and recur on the two splits until this entire subsection is sorted. 
     */

    @Override
    protected void compute () {
        
        // First, check to see if your inputs were valid. If not, return
        if(farRight <= farLeft){
            return;
        }

        // Second, check base case and if it is met then use Arrays.sort
        if (farRight - farLeft < cutoff) {
            Arrays.sort(data, farLeft, farRight + 1);
            return;
        }	
        
        

        // Then, pick pivot and split array
        int pivot;

        if(farRight - farLeft < 50000){
            pivot = pivotAndSplitHoare();
        } else {
            pivot = pivotAndSplitLomoto();
        }
        
        QSTask left;
        QSTask right;
        

        // Tail recursion: recur on the smaller subarray, compute the larger one locally
        if (pivot - farLeft < farRight - pivot){
            left = new QSTask(data, farLeft, pivot-1);
            right = new QSTask(data, pivot+1, farRight);
        } else {
            left = new QSTask(data, pivot+1, farRight);
            right = new QSTask(data, farLeft, pivot-1);
        }
        left.fork();
        right.compute();
        left.join();

        
        return;
    }

    /**
        Below is Lomoto's scheme with middle of 3
     */

    public int pivotAndSplitLomoto(){
        
        // Middle of 3

        // Get middle index
        int mid = (farLeft + farRight)/2;

        // Sort the first, middle, and last elements of array, use middle as pivot (median of 3)
        if (data[farLeft] > data[mid]){
            swap(farLeft,mid);
        } 
        if (data[farLeft] > data[farRight]){
            swap(farLeft,farRight);
        } 
        if (data[mid] > data[farRight]){
            swap(mid,farRight);
        } 
        
        // Now swap mid with the second to last value in array, since we know it is smaller than the last
        swap(mid,farRight-1);
        
        // Get value so we don't access array every time
        float pivVal = data[farRight-1];
        
        // Lomoto's scheme

        int ind = farLeft;
        for(int k = farLeft+1; k < farRight-1; k++){
            if(data[k] <= pivVal){
                ind++;
                swap(ind,k);
            }
        }

        swap(ind+1,farRight-1);
        return ind+1;

    }

    /**
        Below is Hoare's scheme with middle of 3
     */

    public int pivotAndSplitHoare(){

        // Middle of 3

        // Get middle index
        int mid = (farLeft + farRight)/2;

        // Sort the first, middle, and last elements of array, use middle as pivot (median of 3)
        if (data[farLeft] > data[mid]){
            swap(farLeft,mid);
        } 
        if (data[farLeft] > data[farRight]){
            swap(farLeft,farRight);
        } 
        if (data[mid] > data[farRight]){
            swap(mid,farRight);
        } 
        
        // Now swap mid with the second to last value in array, since we know it is smaller than the last
        swap(mid,farRight-1);
        
        // Get value so we don't access array every time
        float pivVal = data[farRight-1];

        // Hoare's Scheme

        int i = farLeft;
        int j = farRight;

        // Iterate through array after inc i, dec j
        i++;
        j -= 2;

        
        while (i <= j){

            if(data[i] <= pivVal){
                i++;
                continue;
            }

            if(data[j] >= pivVal){
                j--;
                continue;
            }


            // if we get to this point then both are false so swap them
            swap(i,j);
            i++;
            j--;
        }

        // Now we can move the pivot to the new j after it crossed with i, then return right (index to pivot)
        swap(farRight-1,j+1);
        return j+1;

    }

    public void swap(int i, int j){
        float h = data[i];
        data[i] = data[j];
        data[j] = h;
        return;
    }
}