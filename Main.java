import java.io.*;
import java.util.*;

// Main class to run a console-based simulation of the ecosystem model
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        // Create a 10x10 model for the simulation
        Model model = new Model(10, 10);

        // Generate initial terrain and print the starting state
        model.generateTerrain();
        System.out.println("Initial State:");
        System.out.println(model.toString());

        // Run the simulation for 5 steps
        for (int step = 1; step <= 5; step++) {
            model.update(); // Update the model (move animals, etc.)
            System.out.println("Step " + step + ":");
            System.out.println(model.toString()); // Print the grid after each step
            try {
                Thread.sleep(1000); // Pause for 1 second to make output readable
            } catch (InterruptedException e) {
                e.printStackTrace(); // Handle interruption during sleep
            }
        }
    }
}