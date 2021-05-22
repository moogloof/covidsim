public class Person {
	private State state; // Flag for state of person
	private int age;
	private int infectedTime;
	private boolean socialDistancing;

	public enum State {CLEAN, INFECTED, DEAD, RECOVERED} // Flags for state of person

	public Person(int age, boolean socialDistancing) {
		// Set age
		this.age = age;
		// Set whether individual is social distancing or not
		this.socialDistancing = socialDistancing;
		// Reset infected time
		infectedTime = 0;

		// Set initial state to clean
		state = State.CLEAN;
	}

	public Person(Person orig) {
		// Copy the original person over to another
		age = orig.age;
		state = orig.state;
		infectedTime = orig.infectedTime;
		socialDistancing = orig.socialDistancing;
	}

	// Change the state of the person
	public void changeState(State newState) {
		state = newState;
	}

	// Get current state of the person
	public State getState() {
		return state;
	}

	// Get current age of person
	public int getAge() {
		return age;
	}

	// Increase infected time
	public void increaseInfectedTime() {
		infectedTime++;
	}

	// Get infected time
	public int getInfectedTime() {
		return infectedTime;
	}

	// Whether person is social distancing or not
	public boolean isSocialDistancing() {
		return socialDistancing;
	}

	// Set whether the person is social distancing or not
	public void setSocialDistancing(boolean socialDistancing) {
		this.socialDistancing = socialDistancing;
	}
}
