import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.io.*;

public class ParallelPrimes {

    private static int count = 0;

    // replace this string with your team name
    public static final String TEAM_NAME = "Isa";

    public static final int MAX_VALUE = Integer.MAX_VALUE;
    public static final int N_PRIMES = 105_097_565;
    public static final int ROOT_MAX = (int) Math.sqrt(MAX_VALUE);
    public static final int MAX_SMALL_PRIME = 1 << 20;

    // CHANGING THESE WILL CHANGE THE NUMBER OF STEPS
    private static final int bitSetSize = ROOT_MAX * 25;


    /**
     The goal of optimizedPrimes is to calculate prime numbers as fast as possible using 
     multithreading techniques
    */

    public static void optimizedPrimes(int[] primes){

        // compute small prime values up to ROOT_MAX
        int[] smallPrimes = Primes.getSmallPrimesUpTo(ROOT_MAX); 

        // Get numThreads and initialize pool
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);

        // Write smallPrimes to Primes
        int minSize = Math.min(primes.length, smallPrimes.length);
        for (; count < minSize; count++) {
            primes[count] = smallPrimes[count];
        }

        // Check if we've already filled primes, and return if so
        if (primes.length == minSize) {
            return;
        }
        
        // Get the number of tasks to complete
        int numTasks = (MAX_VALUE - ROOT_MAX)/bitSetSize;

        // Create list of futures to store each thread's primes
        ArrayList<Future<int[]>> results = new ArrayList<Future<int[]>>(numTasks);

        // Now fill this list of futures with its own task that covers a unique subset of numbers
        // and finds the primes in that subset

        try {
            int start = ROOT_MAX;
            for (int i = 0; i < numTasks; i++) {
                results.add(threadPool.submit(new PrimeChunkTask(smallPrimes, start, bitSetSize, (int) Math.sqrt((start + (bitSetSize))))));
                start += bitSetSize;
            }
            
            // Last task has different value to pass in for sqrEnd because if we did it in loop
            // with the same formula, the calculated end would be past the limit
        
            results.add(threadPool.submit(new PrimeChunkTask(smallPrimes, start, bitSetSize, (int) Math.sqrt(Integer.MAX_VALUE))));
    
            start = ROOT_MAX;

            // Now write the primes you found above
            for (Future<int[]> result : results) {

                int[] chunkPrimes = result.get();
                for(int i = 0; i<chunkPrimes.length && count < primes.length; i++){
                    primes[count++] = chunkPrimes[i];
                }
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        threadPool.shutdown();
    }    

}

