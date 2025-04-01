import java.util.concurrent.ThreadLocalRandom;
public class PiThread implements Runnable {
    
    private long points = 0;
    private double x = 0;
    private double y = 0;
    private double distance = 0;
    private int pointsInCircle = 0;

    private int id;                           // thread id
    private int[] array;                      // array


    // constructor sets number of points to run in this thread, thread number, and the shared array
    public PiThread (long pts, int thrd, int[] arr) {
	    points = pts;
        id = thrd;
        array = arr;
    }

    // the run method required by the Runnable interface
    // this is the code that gets executed when we start the thread
    public void run () {
        
	    for (int i = 0; i < points; i++){
            // Use circle equation and random points to see if the dart hits
            x = ThreadLocalRandom.current().nextDouble()*2 - 1;
            y = ThreadLocalRandom.current().nextDouble()*2 - 1;

            distance = (x * x) + (y * y);

            if(distance <= 1){
                pointsInCircle++;
            }
        }

        array[id] = pointsInCircle;
    }
}