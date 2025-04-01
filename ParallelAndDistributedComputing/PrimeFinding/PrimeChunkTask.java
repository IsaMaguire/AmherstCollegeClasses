import java.util.concurrent.*;
import java.util.BitSet;


public class PrimeChunkTask implements Callable<int[]> {
    private BitSet isntPrime;
    private int[] smallPrimes;
    private int start;
    private int bitSetLen;
    private int sqrEnd; // square root of the max value in this thread

    public PrimeChunkTask (int[] smallPrimes, int start, int bitSetLen, int sqrEnd) {
        this.isntPrime = new BitSet(bitSetLen);
        this.smallPrimes = smallPrimes;
        this.start = start;
        this.bitSetLen = bitSetLen;
        this.sqrEnd = sqrEnd;
    }

    /**
     The job of the method below is to find all the prime numbers between start and start+bitSetLen using sieve of eratosthenes.
    */
    public int[] call (){

        // We can stop searching when smallPrimes[l] < sqrEnd because there is no factor of any number n 
        // that is greater than the square root of n, except that it is multiplied by something smaller than 
        // the square root of n (which would have already been used)
        for(int l = 0; l < smallPrimes.length && smallPrimes[l] <= sqrEnd; l++){
            int p = smallPrimes[l];
            int i = (start % p == 0) ? start : p * (1 + start / p);
            i -= start;

            while (i < bitSetLen) {
                isntPrime.set(i); 
                i += p;
            }
        }

        // Now get the actual primes
        int numPrimes = bitSetLen - isntPrime.cardinality();
        int[] chunkPrimes = new int[numPrimes];
        int count = 0;

        // Only iterate over bits that are false, not the whole bitset.
        for (int i = isntPrime.nextClearBit(0); i >= 0; i = isntPrime.nextClearBit(i+1)) {
            chunkPrimes[count] = (int) start + i;
            count++;
            if (count == chunkPrimes.length) {
                break; 
            }
        }
        
        

        return chunkPrimes;
    }
}
