/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */

public class Monitor {
	
	/*
	 * ------------
	 * Data members
	 * ------------
	 */
    private enum State {THINKING, HUNGRY, EATING, TALKING} 
    private State[] states;
    private boolean talking;
    private int numPhilosophers;
    
    /**
	 * Constructor
	 */
    public Monitor(int piNumberOfPhilosophers) {
    	//set appropriate number of chopsticks based on the # of philosophers
        numPhilosophers = piNumberOfPhilosophers;
        states = new State[numPhilosophers];
        for (int i = 0; i < numPhilosophers; i++) {
            states[i] = State.THINKING; //initializing the state of each philosopher to "THINKING".
        }
        talking = false;
    }
    
	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
    public synchronized void pickUp(final int piTID) {
        states[piTID - 1] = State.HUNGRY; //making the state of the philosopher piTID "HUNGRY".
        
        //Loop checks if philosopher is eating
        while (!canEat(piTID)) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println("Monitor.pickUp():");
                DiningPhilosophers.reportException(e);
                System.exit(1);
            }
        }
        states[piTID - 1] = State.EATING; //changing the state of the philosopher piTID "EATING".
    }
    
    /**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
    public synchronized void putDown(final int piTID) {
        states[piTID - 1] = State.THINKING; //changing the state of the philosopher piTID to "THINKING".
        notifyAll(); //Notifying other philosophers that were waiting.
    }
    
    /**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
    public synchronized void requestTalk(int piTID) {
    	//loops checks if any philosophers is talking.
        while (talking) {
            try {
                wait(); //waiting if a philosopher is talking.
            } catch (InterruptedException e) {
                System.err.println("Monitor.requestTalk():");
                DiningPhilosophers.reportException(e);
                System.exit(1);
            }
        }
        talking = true; //allowed to talk if no one else talking
    }
    
    /**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
    public synchronized void endTalk(int piTID) {
    	//notifies all philosophers that are waiting
        talking = false; 
        notifyAll();
    }
    
    private boolean canEat(final int piTID) {
    	//checking whether philosopher is eating
        int left = (piTID + numPhilosophers - 2) % numPhilosophers;
        int right = piTID % numPhilosophers;
        return states[left] != State.EATING && states[right] != State.EATING;
    }
}
//EOF