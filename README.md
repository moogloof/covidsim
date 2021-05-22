# COVID19 Spread Simulation
A simulation of a virus spreading throughout a population.
### How it works.
This project is based on the stanford nifty project [here](http://nifty.stanford.edu/2021/bitner-covid-sim/). Since the rules outlined in the original were a bit vague, I altered them slightly for my project.
1. A population will start with around 5% infected.
2. Green are healthy, red are infected, yellow are recovered, and black are dead.
3. At each step:
	- Healthy people will have a 75% chance of getting infected if they are adjacent to an infected person AND both them and the infected adjacent people are not social distancing.
	- Infected people will have a 3% chance of dying. After 4 cycles of the simulation, infected people will go back to being healthy. However, like the effects of COVID19, younger people (<18 years old) will have a decreased 1% chance of dying.
	- Dead people will stay dead.
	- Recovered people will stay recovered. They will not be infected again.
4. People who are social distancing will continue to do so, even if you increase the percentage of the population social distancing.

**NOTE: The percentage of people social distancing is not always reliable, which is by design, since the simulation is meant to be as realistic as possible.**
### Compiling
Simply change into the `src` directory in the shell and compile.
```sh
cd path/to/src
javac *.java
```
### Running
Just do `java Main` in the src directory after compiling.
### Interface
The interface is fairly simple and there are labels, so you should be able to figure it out by simply looking at the app.
