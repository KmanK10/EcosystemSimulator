import java.io.*;
import java.util.*;

// Houses the data and logic for the simulation.
public class Model {
    private Terrain[][] grid; // 2D array to store the grid with the terrain chunks
    private int rows;     // Number of rows in the grid
    private int cols;     // Number of columns in the grid

    /**
     * Default constructor
     * @param width The width of the simulation 
     * @param height The height of the simulation
     */
    public Model(int width, int height) {
        rows = width;
        cols = height;

        grid = new Terrain[cols][rows]; // Set the # of rows and columns in the grid
        for (Terrain[] a : grid) for (int i = 0; i < rows; i++) a[i] = new Terrain(); // Set each terrain to default
    }

    public int getRows() {return rows;}
    public int getCols() {return cols;}

    public void update() {
        
    }

    public String toString () {
        String output = rows + " x " + cols + "\n";

        for (Terrain[] a : grid) {
            for (Terrain b: a) output += b.toString();
            
            output += "\n";
        }

        return output;
    }
    
    
}
