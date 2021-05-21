public class Simulation {
	private int t; // Cycles elapsed
	private Person[][] population; // Population grid

	public Simulation(int width, int height) {
		// Set origin time
		t = 0;

		// Set population grid
		population = new Person[height][width];

		// Reset population
		for (int i = 0; i < population.length; i++) {
			for (int j = 0; j < population[0].length; j++) {
				population[i][j] = new Person((int)(Math.random() * 80) + 1, false);
			}
		}

		// Set around 5% initially to infected
		// Sometimes unreliable, but mostly is
		for (int i = 0; i < (int)(0.05 * width * height); i++) {
			int randomI = (int)(Math.random() * height);
			int randomJ = (int)(Math.random() * width);
			population[randomI][randomJ].changeState(Person.State.INFECTED);
		}
	}

	public void next() {
		// Set time forward
		t += 1;

		// Change states of the board
		Person[][] newPopulation = new Person[population.length][population[0].length];

		for (int i = 0; i < population.length; i++) {
			for (int j = 0; j < population[0].length; j++) {
				// Update the person's state using probabilities
				switch (population[i][j].getState()) {
					case CLEAN: // Clean handler
						// Check whether adjacent squares are infectious and not social distancing
						if (adjacentInfectious(i, j) && !population[i][j].isSocialDistancing()) {
							// Random chance of getting infected
							if (Math.random() < 0.75) {
								population[i][j].changeState(Person.State.INFECTED);
							}
						}
						break;
					case INFECTED: // Infected handler
						// Probability of infected person dying
						// Dying depends on age
						// Death roll
						double chance = Math.random();
						// Threshold for dying probability
						double deathThreshold = 0.97;
						if (population[i][j].getAge() < 18) {
							deathThreshold = 0.99;
						}

						// Dying or stay infected
						if (chance > deathThreshold) {
							population[i][j].changeState(Person.State.DEAD);
						}

						// Increment infected time of person
						population[i][j].increaseInfectedTime();
						break;
					case DEAD: // Break handler
						// Do nothing if dead lol
						break;
					case RECOVERED: // Recovered handler
						// Do nothing if recovered either
						break;
					default:
						break;
				}
			}
		}
	}

	private boolean adjacentInfectious(int i, int j) {
		// Check each adjacent square
		for (int dx = -1; dx <= 1; dx++) {
			for (int dy = -1; dy <= 1; dy++) {
				// Get position of adjacent square
				int adjX = j + dx, adjY = i + dy;
				// Check whether indexes are out of bounds
				if (adjY >= 0 && adjY < population.length) {
					if (adjX >= 0 && adjX < population[0].length) {
						// Check whether the square is actually itself
						if (adjX != j && adjY != i) {
							// Check whether the square is infected AND not social distancing
							if (population[adjY][adjX].getState() == Person.State.INFECTED && !population[adjY][adjX].isSocialDistancing()) {
								return true;
							}
						}
					}
				}
			}
		}

		return false;
	}
}
