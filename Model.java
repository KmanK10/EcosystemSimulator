import java.io.*;
import java.util.*;

// Houses the data and logic for the simulation.
public class Model {
    private Terrain[][] grid; // 2D array to store the grid with the terrain chunks
    private int rows;     // Number of rows in the grid
    private int cols;     // Number of columns in the grid
    private int size;
    private int waterMax;
    private int foodMax;
    private int rockMax;
    private int animalMax;
    private Random random;

    /**
     * Default constructor
     * @param width The width of the simulation 
     * @param height The height of the simulation
     */
    public Model(int width, int height) {
        random = new Random();
        size = width * height;
        rows = width;
        cols = height; 


        grid = new Terrain[cols][rows]; // Set the # of rows and columns in the grid
        for (Terrain[] a : grid) for (int i = 0; i < rows; i++) a[i] = new Terrain(); // Set each terrain to default

        // Set terrain max sizes
        waterMax = size / 5;
        foodMax = size / 50;
        rockMax = size / 40;
        animalMax = size / 30;
    }

    public int getRows() {return rows;}
    public int getCols() {return cols;}

    public Terrain terrainAt(int row, int col) {return grid[row][col];}
    public void setTerrain(Terrain t, int row, int col) {grid[row][col] = t;}
    
    public void generateTerrain() {
        int multiplier = 7;

        int water = 0;
        int food = 0;
        int rock = 0;
        int animal = 0;
        
        int waterT = 0;
        int foodT = 0;
        int rockT = 0;
        int animalT = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j += random.nextInt(1, 3)) {
                switch (random.nextInt(5)) {
                    case 0: if (water <= waterMax && waterT < 1) {
                        grid[i][j] = new Water();
                        water++;
                        waterT = size/(waterMax * multiplier);
                    }
                    
                    break;
                        
                    case 1: if (food <= foodMax && foodT < 1) {
                        grid[i][j] = new Food();
                        food++;
                        foodT = size/(foodMax * multiplier);
                    }
                    
                    break;
                        
                    case 2: if (rock <= rockMax && rockT < 1) {
                        grid[i][j] = new Rock();
                        rock++;
                        rockT = size/(rockMax * multiplier);
                    }
                    
                    break;

                    case 3: if (animal <= animalMax && animalT < 1) {
                        grid[i][j] = new Animal();
                        animal++;
                        animalT = size/(animalMax * multiplier);
                    }
                    
                    break;
                    
                    default: grid[i][j] = new Terrain();
                    waterT--;
                    foodT--;
                    rockT--;
                    animalT--;
                }
            }
        }
    }

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
