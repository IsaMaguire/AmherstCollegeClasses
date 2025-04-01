import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class HTGenerator {

    public static void main (String[] args) throws FileNotFoundException {

	if (args.length != 3) { showUsageAndExit(); }

	int range  = 0;
	int length = 0;
	try {
	    range  = Integer.parseInt(args[0]);
	    length = Integer.parseInt(args[1]);
	} catch (NumberFormatException e) {
	    showUsageAndExit();
	}
	if (range <= 0 || length <= 0) { showUsageAndExit(); }

        String      outPath = args[2];
        PrintWriter out     = null;
        try {
            out = new PrintWriter(outPath);
        } catch (FileNotFoundException e) {
            System.err.printf("ERROR: Could not write to file %s\n", outPath);
            System.exit(1);
        }

	// Use the length as a rough proxy for the storage requirements.
	HashSet<Integer> hs  = new HashSet<Integer>();
	Random           r   = new Random();

	// Generate randomly chosen instructions.
	for (int line = 1; line <= length; line += 1) {

	    int     value     = r.nextInt(range) + 1;
	    String  operation = null;
	    boolean result    = false;
	    int     opSelect  = r.nextInt(3);
	    switch (opSelect) {
	    case 0:
		operation = "insert";
		result = hs.add(value);
		break;
	    case 1:
		operation = "lookup";
		if (!hs.isEmpty() && r.nextBoolean()) {
		    value = pickRandom(hs, r);
		}
		result = hs.contains(value);
		break;
	    case 2:
		operation = "remove";
		if (!hs.isEmpty() && r.nextFloat() < 0.9) {
		    value = pickRandom(hs, r);
		}
		result = hs.remove(value);
		break;
	    }
            
	    out.printf("%s %d %b\n",
                       operation,
                       value,
                       result);
	    
	}

        out.flush();
        out.close();
	
    }


    private static int pickRandom (HashSet<Integer> hs, Random r) {

	Iterator<Integer> iterator = hs.iterator();
	int               target   = r.nextInt(hs.size());
	int               value    = 0;
	for (int i = 0; i <= target; i += 1) {
	    value = iterator.next();
	}
	return value;
	
    }


    private static void showUsageAndExit () {
	
	System.err.printf("USAGE: java HTGenerator <range> <test length> <output pathname>\n");
	System.exit(1);

    }
    
    
} // class HTGenerator
