import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

// Java FX app
public class Main extends Application implements EventHandler<ActionEvent> {
	Simulation sim; // Simulation instance
	Button startButton; // Button for starting simulation
	Slider slider; // Slider for percentage of population that is social distancing

	public static void main(String[] args) {
		// Launch java FX application
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Set window settings
		primaryStage.setTitle("COVID19 Simulation");
		primaryStage.setResizable(false);

		// Set simulation
		startSim();

		// Set button
		startButton = new Button("Reset Sim");
		startButton.setOnAction(this);

		// Set canvas for drawing population
		Canvas canvas = new Canvas(400, 400);
		GraphicsContext context = canvas.getGraphicsContext2D();

		// Set label of slider
		Label sliderLabel = new Label("Percentage of population that is social distancing.");

		// Set slider for social distancing stuff
		slider = new Slider(0, 1.0, 0);
		// Show tick marks on the slider
		slider.setShowTickMarks(true);
		slider.setShowTickLabels(true);
		slider.setMajorTickUnit(0.2);
		slider.setBlockIncrement(0.01);
		// Set slider update handler when slider changes using lambda function
		slider.setOnMouseReleased(e -> sim.updateSocialDistancing(slider.getValue()));

		// Set VBox for displaying slider and button
		VBox controlVBox = new VBox(5);
		// Add the sliderLabel and the slider to the vbox
		controlVBox.getChildren().addAll(sliderLabel, slider, startButton);

		// Labels for the data on the population
		Label infectedLabel = new Label("Infected: " + sim.getByState(Person.State.INFECTED));
		Label deadLabel = new Label("Dead: " + sim.getByState(Person.State.DEAD));
		Label healthyLabel = new Label("Recovered: " + sim.getByState(Person.State.RECOVERED));

		// Set HBox for displaying the data about the population
		HBox dataHBox = new HBox(30);
		// Add labels for data to the HBox
		dataHBox.getChildren().addAll(infectedLabel, deadLabel, healthyLabel);

		// Specify layout of window
		BorderPane layout = new BorderPane();
		// Put canvas, data hbox, and vbox in appropriate places
		layout.setCenter(canvas);
		layout.setRight(controlVBox);
		layout.setBottom(dataHBox);

		// Scene is contents of window
		Scene scene = new Scene(layout, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();

		// Animation of simulation
		// Anonymous subclass of AnimationTimer for handling
		new AnimationTimer() {
			private long previousTime = 0; // For adding durations for each frame of the simulation animation

			@Override
			public void handle(long now) {
				if (now - previousTime >= 1000000000) {
					// Update previous time
					previousTime = now;

					// Calculate dimensions of each square on canvas
					double w = canvas.getWidth() / sim.getWidth();
					double h = canvas.getHeight() / sim.getHeight();

					// Update canvas
					for (int i = 0; i < sim.getHeight(); i++) {
						for (int j = 0; j < sim.getWidth(); j++) {
							// Draw person on canvas according to status
							switch (sim.getPerson(i, j).getState()) {
								case CLEAN: // Clean is green
									context.setFill(Color.rgb(13, 255, 0));
									break;
								case INFECTED: // Infected is red
									context.setFill(Color.rgb(255, 0, 0));
									break;
								case DEAD: // Dead is black
									context.setFill(Color.rgb(0, 0, 0));
									break;
								case RECOVERED: // Recovered in yellow
									context.setFill(Color.rgb(255, 221, 0));
									break;
								default:
									break;
							}
							// Draw the rectangle
							context.fillRect(w * j, h * i, w, h);
						}
					}

					// Update the state of the simulation
					sim.next();

					// Update the data labels about the simulation
					infectedLabel.setText("Infected: " + sim.getByState(Person.State.INFECTED));
					deadLabel.setText("Dead: " + sim.getByState(Person.State.DEAD));
					healthyLabel.setText("Recovered: " + sim.getByState(Person.State.RECOVERED));
				}
			}
		}.start();
	}

	@Override
	public void handle(ActionEvent event) {
		// Check for sources of event to handle
		if (event.getSource() == startButton) {
			startSim();
		}
	}

	public void startSim() {
		// Reset simulation
		// Start a simulation with a population of 100x100 squares
		sim = new Simulation(100, 100);

		// Set the social distancing percentage if the slider is defined
		if (slider != null) {
			sim.updateSocialDistancing(slider.getValue());
		}
	}
}
