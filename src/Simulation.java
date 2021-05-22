import java.util.*;

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

	// Get width of population
	public int getWidth() {
		return population[0].length;
	}

	// Get height of population
	public int getHeight() {
		return population.length;
	}

	// Get person at specific spot on population grid
	public Person getPerson(int i, int j) {
		return population[i][j];
	}

	public void next() {
		// Set time forward
		t += 1;

		// Change states of the board
		Person[][] newPopulation = new Person[population.length][population[0].length];

		for (int i = 0; i < population.length; i++) {
			for (int j = 0; j < population[0].length; j++) {
				// Copy current population to to-be new population
				newPopulation[i][j] = new Person(population[i][j]);

				// Update the person's state using probabilities
				switch (population[i][j].getState()) {
					case CLEAN: // Clean handler
						// Check whether adjacent squares are infectious and not social distancing
						if (adjacentInfectious(i, j) && !population[i][j].isSocialDistancing()) {
							// Random chance of getting infected
							if (Math.random() < 0.75) {
								newPopulation[i][j].changeState(Person.State.INFECTED);
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

						// Recover if the person has survived for 4 cycles
						if (population[i][j].getInfectedTime() >= 4) {
							newPopulation[i][j].changeState(Person.State.RECOVERED);
						} else if (chance > deathThreshold) {
							// Dying or stay infected
							newPopulation[i][j].changeState(Person.State.DEAD);
						} else {
							// Increment infected time of person
							newPopulation[i][j].increaseInfectedTime();
						}

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

		// Update the real population
		population = newPopulation;
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

	public void updateSocialDistancing(double percent) {
		// Update the people in the population who are social distancing according to percentage
		// Current social distancing
		int currentSD = 0;
		// Target social distancing amount
		int targetSD = (int)(percent * population.length * population[0].length);
		// Indexes of currently social distancing people
		List<Integer[]> currentSDIndexes = new ArrayList<Integer[]>();

		// Get amount that is current social distancing
		for (int i = 0; i < population.length; i++) {
			for (int j = 0; j < population[0].length; j++) {
				if (population[i][j].isSocialDistancing()) {
					currentSD++;
					// Add to current SD indexes list
					currentSDIndexes.add(new Integer[] {i, j});
				}
			}
		}

		// Compare currently social distancing with target
		if (currentSD > targetSD) {
			// If there are more that are social distancing that needed, decrease
			for (int i = 0; i < currentSD - targetSD; i++) {
				// Get and make a random person not social distance
				int toRemove = (int)(Math.random() * currentSDIndexes.size());
				Integer[] personIndex = currentSDIndexes.get(toRemove);
				population[personIndex[0]][personIndex[1]].setSocialDistancing(false);
				currentSDIndexes.remove(toRemove);
			}
		} else if (currentSD < targetSD) {
			// If there are less that are social distancing that needed, increase
			for (int i = 0; i < targetSD - currentSD; i++) {
				// Make random do social distancing I guess
				// Not at all reliable, but realistic
				population[(int)(Math.random() * population.length)][(int)(Math.random() * population[0].length)].setSocialDistancing(true);
			}
		}
	}

	public int getByState(Person.State state) {
		// Get amount of people with the specified state
		int count = 0;

		// Check each person in population for state
		for (int i = 0; i < population.length; i++) {
			for (int j = 0; j < population[0].length; j++) {
				if (population[i][j].getState() == state) {
					count++;
				}
			}
		}

		return count;
	}
}
