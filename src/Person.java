public enum State {CLEAN, INFECTED, DEAD, RECOVERED} // Flags for state of person

public class Person {
	private State state; // Flag for state of person
	private int age;
	private boolean socialDistancing;

	public Person(int age, boolean socialDistancing) {
		// Set age
		this.age = age;
		// Set whether individual is social distancing or not
		this.socialDistancing = socialDistancing;

		// Set initial state to clean
		state = CLEAN;
	}

	// Change the state of the person
	public void changeState(State newState) {
		state = newState;
	}

	// Get current state of the person
	public State getState() {
		return state;
	}
}
