/**
 * Class DiningPhilosophers
 * The main starter.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class DiningPhilosophers
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */

	/**
	 * This default may be overridden from the command line
	 */
	public static final int DEFAULT_NUMBER_OF_PHILOSOPHERS = 4;

	/**
	 * Dining "iterations" per philosopher thread
	 * while they are socializing there
	 */
	public static final int DINING_STEPS = 10;

	/**
	 * Our shared monitor for the philosphers to consult
	 */
	public static Monitor soMonitor = null;

	/*
	 * -------
	 * Methods
	 * -------
	 */

	/**
	 * Main system starts up right here
	 */
	public static void main(String[] argv) {
        try {
        	
        	/*
			 * Should be settable from the command line
			 * or the default if no arguments supplied.
			 */
        	
            int iPhilosophers = DEFAULT_NUMBER_OF_PHILOSOPHERS;
            
            //checking integer if positive
            if (argv.length > 0) {
                try {
                    int inputPhilosophers = Integer.parseInt(argv[0]);
                    if (inputPhilosophers > 0) {
                        iPhilosophers = inputPhilosophers;
                    } else {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) { //if not positive, print this line
                    System.err.println("\"" + argv[0] + "\" is not a positive decimal integer");
                    System.err.println("Usage: java DiningPhilosophers [NUMBER_OF_PHILOSOPHERS]");
                    System.exit(1);
                }
            }
            //Make the monitor aware of how many philosophers there are
            soMonitor = new Monitor(iPhilosophers);
            Philosopher aoPhilosophers[] = new Philosopher[iPhilosophers]; //Space for all the philosophers

            for (int j = 0; j < iPhilosophers; j++) {
                aoPhilosophers[j] = new Philosopher();
                aoPhilosophers[j].start();
            }

            System.out.println(iPhilosophers + " philosopher(s) came in for a dinner.");
            
            //waits for philosophers to finish
            for (int j = 0; j < iPhilosophers; j++)
                aoPhilosophers[j].join();

            System.out.println("All philosophers have left. System terminates normally.");
        } catch (InterruptedException e) {
            System.err.println("main():");
            reportException(e);
            System.exit(1);
        }
    }

	/**
	 * Outputs exception information to STDERR
	 * @param poException Exception object to dump to STDERR
	 */
	public static void reportException(Exception poException)
	{
		System.err.println("Caught exception : " + poException.getClass().getName());
		System.err.println("Message          : " + poException.getMessage());
		System.err.println("Stack Trace      : ");
		poException.printStackTrace(System.err);
	}
}

// EOF
