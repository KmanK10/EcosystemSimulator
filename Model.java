import java.util.*;

// Represents a grid-based simulation model with various terrain types
public class Model {
    private Terrain[][] grid; // 2D array to store terrain objects
    private int rows, cols;   // Dimensions of the grid
    private Random random;    // Random number generator for terrain placement and movement

    // Constructor initializes the grid with given dimensions and generates terrain
    public Model(int width, int height) {
        random = new Random();
        rows = width;
        cols = height;
        generateTerrain(); // Populate grid with terrain
    }

    // Getter for number of rows
    public int getRows() { return rows; }

    // Getter for number of columns
    public int getCols() { return cols; }

    // Returns the terrain at specified coordinates
    public Terrain terrainAt(int row, int col) { return grid[row][col]; }

    /**
     * Replaces a random empty (base Terrain) cell with the specified terrain.
     * If no empty cells exist, no action is taken.
     */
    public void replaceTerrain(Terrain terrain) {
        // Collect coordinates of all empty (base Terrain) cells
        ArrayList<int[]> gridCoords = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Check if the terrain is exactly the base Terrain class (not subclasses)
                if (grid[i][j].getClass() == Terrain.class) {
                    gridCoords.add(new int[2]);
                    gridCoords.get(gridCoords.size() - 1)[0] = i;
                    gridCoords.get(gridCoords.size() - 1)[1] = j;
                }
            }
        }

        // If empty cells exist, randomly select one and replace its terrain
        if (!gridCoords.isEmpty()) {
            int[] temp = gridCoords.get(random.nextInt(gridCoords.size()));
            grid[temp[0]][temp[1]] = terrain;
        }
    }

    // Initializes the grid with a mix of terrain types based on probabilities
    public void generateTerrain() {
        // Calculate maximum counts for each terrain type based on grid size
        int size = rows * cols;
        int waterMax = size / 5;   // 20% of grid can be water
        int foodMax = size / 50;   // 2% of grid can be food
        int rockMax = size / 40;   // 2.5% of grid can be rocks
        int animalMax = size / 30; // ~3.3% of grid can be animals

        // Initialize grid with empty terrain
        grid = new Terrain[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Terrain();
            }
        }

        // Create a list of all grid positions and shuffle them for random placement
        List<int[]> positions = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                positions.add(new int[]{i, j});
            }
        }
        Collections.shuffle(positions, random);

        // Place terrain types randomly until max counts are reached
        int water = 0, food = 0, rock = 0, animal = 0;
        for (int[] pos : positions) {
            int i = pos[0], j = pos[1];
            double r = random.nextDouble();
            if (r < 0.2 && water < waterMax) {
                grid[i][j] = new Water();
                water++;
            } else if (r < 0.4 && food < foodMax) {
                grid[i][j] = new Food();
                food++;
            } else if (r < 0.6 && rock < rockMax) {
                grid[i][j] = new Rock();
                rock++;
            } else if (r < 0.8 && animal < animalMax) {
                grid[i][j] = new Animal();
                animal++;
            }
        }
    }

    // Updates the grid by moving animals to adjacent cells
    public void update() {
        // Create a new grid to store updated state
        Terrain[][] newGrid = new Terrain[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(grid[i], 0, newGrid[i], 0, cols);
        }

        // Process each cell in the grid
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (newGrid[i][j] instanceof Animal) {
                    Animal animal = (Animal) newGrid[i][j];
                    // Possible moves: up, down, left, right
                    int[][] moves = {
                        {i-1, j}, {i+1, j}, {i, j-1}, {i, j+1}
                    };
                    // 50% chance for the animal to move
                    if (random.nextBoolean()) {
                        int[] move = moves[random.nextInt(moves.length)];
                        int ni = move[0], nj = move[1];
                        // Check if the move is within bounds and not to a rock or water
                        if (ni >= 0 && ni < rows && nj >= 0 && nj < cols &&
                            !(newGrid[ni][nj] instanceof Rock || newGrid[ni][nj] instanceof Water)) {
                            // If moving to a food cell, animal consumes it
                            if (newGrid[ni][nj] instanceof Food) {
                                animal.eatFood();
                            }
                            // Move animal to new position, leave empty terrain behind
                            newGrid[i][j] = new Terrain();
                            newGrid[ni][nj] = animal;
                        }
                    }
                }
            }
        }

        // Update the grid with the new state
        grid = newGrid;
    }

    // Returns a string representation of the grid
    public String toString() {
        StringBuilder output = new StringBuilder(rows + " x " + cols + "\n");
        for (Terrain[] row : grid) {
            for (Terrain t : row) {
                output.append(t.toString()).append(" ");
            }
            output.append("\n");
        }
        return output.toString();
    }
}