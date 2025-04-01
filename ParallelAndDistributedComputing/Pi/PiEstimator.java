public class PiEstimator {
    // add any desired fields here
    private int threads;
    private long points;
    private double circTot;

    // constructor taking in the number of sample points, numPoints, 
    // and the number of threads used to compute the estimate
    public PiEstimator (long numPoints, int numThreads) {
        threads = numThreads;
        points = numPoints;
    }

    // compute the estimate of pi by dividing work among threads
    public double getPiEstimate () {

        // Shared array that gets edited by each thread
        int[] inCirc = new int[threads];

	    Thread[] threadArr = new Thread[threads];

        // initialize threads 
	    for (int i = 0; i < threads; i++) {
	        threadArr[i] = new Thread(new PiThread(points/threads, i, inCirc));
    	}

        // start all of the threads
        for (Thread t : threadArr) {
            // Must use start instead of run 
            t.start();
        }

        // wait for all threads to complete
	    for (Thread t : threadArr) {
	        try {
		        t.join();
            }
    	    catch (InterruptedException ignored) {
     		// don't care if t was interrupted
	        }
    	}

        // Add up the different results for points in circle and calculate pi
        for (int j = 0; j < this.threads; j++){
            circTot += inCirc[j];
        }


        return 4 * (circTot / points);
    }
}
