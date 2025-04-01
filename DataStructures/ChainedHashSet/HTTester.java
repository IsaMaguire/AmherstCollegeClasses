import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class HTTester {

    public static void main (String[] args) throws FileNotFoundException {

	if (args.length != 3) showUsageAndExit();

	int storageSize = 0;
	try {
	    storageSize = Integer.parseInt(args[0]);
	} catch (NumberFormatException e) {
	    showUsageAndExit();
	}
	if (storageSize <= 0) showUsageAndExit();

        String inputPath  = args[1];
        String outputPath = args[2];

	AmhHashSet<Integer> hs     = new ChainedHashSet<Integer>(storageSize);
        File                inFile = new File(inputPath);
        PrintWriter         out    = null;
        if (hs == null) showUsageAndExit();
        if (!inFile.canRead()) {
            System.err.printf("ERROR: Cannot read file %s\n", inputPath);
            System.exit(1);
        }
        try {
            out = new PrintWriter(outputPath);
        } catch (FileNotFoundException e) {
            System.err.printf("ERROR: Could not open file %s\n", outputPath);
            System.exit(1);
        }

	// Read instructions until there are no more.
	Scanner     in   = new Scanner(inFile);
	int         line = 0;
	while (in.hasNext()) {

	    line = line + 1;
	    String  operation = in.next();
	    int     value     = 0;
	    boolean expectedResult = false;
	    try {
		value = in.nextInt();
		expectedResult = in.nextBoolean();
	    } catch (InputMismatchException e) {
		System.err.printf("ERROR: Could not read line %d\n", line);
		System.exit(1);
	    }

	    boolean result = false;
	    if (operation.equals("insert")) {
		result = hs.insert(value);
	    } else if (operation.equals("lookup")) {
		result = hs.lookup(value);
	    } else if (operation.equals("remove")) {
		result = hs.remove(value);
	    }

	    out.printf("%s %10d -> %5b : %s\n",
                       operation,
                       value,
                       result,
                       (result == expectedResult ? "match" : "MISMATCH"));
	    
	}

	out.printf("Total collisions = %d\n", hs.getNumberCollisions());
        out.flush();
        out.close();
	
    }

    
    private static void showUsageAndExit () {
	
	System.err.printf("USAGE: java HTTester <storage size>\n"     +
                          "                     <input pathname>\n"   +
                          "                     <output pathname>\n"  );
	System.exit(1);

    } // showUsageAndExit ()

    
} // class HTTester
