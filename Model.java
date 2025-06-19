// Davin Yoon and Kiefer Menard CS 142 Professor Julio Garabay

import java.util.*;
import java.awt.*;

// Represents a grid-based simulation model with various terrain types
public class Model {
    private static final int WATER_ELEVATION = 120; // The height that water comes up to
    private static final int FOOD_RANGE = 30; // The range of elevation vales that food can grow between
    private static final int BEACH_DISTANCE = 20; // The distance that the beach will extend from the water
    private Terrain[][] grid; // 2D array to store terrain objects
    private double[][] heightMap; // 2D array to store the height map
    private int rows, cols;   // Dimensions of the grid
    private Random random;    // Random number generator for terrain placement and movement

    // Constructor initializes the grid with given dimensions and generates terrain
    public Model(int width, int height) {
        random = new Random();
        rows = width;
        cols = height;
        generateTerrain(); // Populate grid with terrain
    }

    // Initializes the grid with a mix of terrain types based on probabilities
    public void generateTerrain() {
        heightMap = generateHeightMap(rows, cols, 0.1, 6 , 0.1, 2, System.currentTimeMillis()); // Generate height map

        // Calculate maximum counts for each terrain type based on grid size
        int size = rows * cols;
        int waterMax = size / 5;   
        int foodMax = size / 50;   
        int rockMax = size / 40;   
        int hogMax = size / 60; 
        int mouseMax = size / 50;
        int tigerMax = size / 70;

        // Initialize grid with empty terrain
        grid = new Terrain[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (heightAt(i, j) < WATER_ELEVATION) grid[i][j] = new Water();
                else if (heightAt(i, j) < WATER_ELEVATION + BEACH_DISTANCE) grid[i][j] = new Sand();
                else grid[i][j] = new Terrain();
            }
        }

        // Create a list of all grid positions and shuffle them for random placement
        ArrayList<int[]> positions = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                positions.add(new int[]{i, j});
            }
        }
        Collections.shuffle(positions, random);

        // Place terrain types randomly until max counts are reached
        int water = 0, food = 0, rock = 0, hog = 0, mouse = 0, tiger = 0;
        for (int[] pos : positions) {
            int i = pos[0], j = pos[1];
            if (grid[i][j] instanceof Water) continue;
            double r = random.nextDouble();
            if (r < 0.4 && food < foodMax && heightAt(i, j) > WATER_ELEVATION && heightAt(i, j) < WATER_ELEVATION + FOOD_RANGE) {
                grid[i][j] = new Food();
                food++;
            } else if (r < 0.6 && rock < rockMax) {
                grid[i][j] = new Rock();
                rock++;
            } else if (r < 0.8 && hog < hogMax) {
                int type = random.nextInt(0, 3);
                switch(type) {
                    case 0:
                        grid[i][j] = new Mouse();
                        mouse++;
                        break;
                    case 1: 
                        grid[i][j] = new Hog();
                        hog++;
                        break;
                    case 2:
                        grid[i][j] = new Tiger();
                        tiger++;
                        break;
                }
                
            }
        }
    }

    /**
     * Generates a 2D height map using Perlin noise with multiple octaves for natural terrain-like patterns.
     * The height map values are normalized to the range [0, 1] and scaled to a user-defined range (e.g., [0, 100]).
     * Note: Used AI to help write function.
     *
     * @param rows         The number of rows in the height map grid.
     * @param cols         The number of columns in the height map grid.
     * @param scale        The scaling factor for input coordinates, controlling the smoothness of the terrain.
     *                     Smaller values (e.g., 0.01) produce smoother terrain; larger values (e.g., 0.1) produce rougher terrain.
     * @param octaves      The number of noise layers to combine, adding detail to the terrain.
     *                     Higher values increase complexity but may slow computation.
     * @param persistence  The amplitude reduction factor per octave, typically in [0, 1].
     *                     Controls the influence of higher-frequency octaves (e.g., 0.5 halves amplitude each octave).
     * @param lacunarity   The frequency multiplier per octave, typically >= 1.
     *                     Controls the scale of details (e.g., 2.0 doubles frequency each octave).
     * @param seed         The random seed for the Z coordinate, ensuring a unique map for each seed.
     * @return A 2D array of doubles representing the height map, with values scaled to [0, 100].
     */
    public static double[][] generateHeightMap(int rows, int cols, double scale, int octaves, double persistence, double lacunarity, long seed) {
        double[][] heightMap = new double[rows][cols];
        double minNoiseHeight = Double.MAX_VALUE;
        double maxNoiseHeight = -Double.MAX_VALUE;

        // Use Random with the provided seed to generate a Z coordinate
        Random random = new Random(seed);
        double z = random.nextDouble() * 1000; // Large range for variety

        // First pass: Generate noise and track min/max
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double amplitude = 1.0;
                double frequency = 1.0;
                double noiseHeight = 0.0;

                // Sum multiple octaves of noise
                for (int o = 0; o < octaves; o++) {
                    double x = i * scale * frequency;
                    double y = j * scale * frequency;

                    // Use random z as seed
                    double sample = ImprovedNoise.noise(x, y, z);
                    noiseHeight += sample * amplitude;

                    amplitude *= persistence;
                    frequency *= lacunarity;
                }

                // Track min and max
                minNoiseHeight = Math.min(minNoiseHeight, noiseHeight);
                maxNoiseHeight = Math.max(maxNoiseHeight, noiseHeight);

                heightMap[i][j] = noiseHeight;
            }
        }

        // Second pass: Normalize to [0, 255] based on actual min/max
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Normalize to [0, 1]
                double normalized = (heightMap[i][j] - minNoiseHeight) / (maxNoiseHeight - minNoiseHeight);
                // Apply non-linear contrast
                normalized = Math.pow(normalized, 0.8);
                // Scale to [0, 255]
                heightMap[i][j] = normalized * 255.0;
            }
        }

        return heightMap;
    }

    // Getter for number of rows
    public int getRows() { return rows; }

    // Getter for number of columns
    public int getCols() { return cols; }

    // Returns the terrain at specified coordinates
    public Terrain terrainAt(int row, int col) { return grid[row][col]; }

    // Returns the height at specified coordinates
    public double heightAt(int row, int col) {return heightMap[row][col];}

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

    // Utility function for the Update function to set a grid location to either grass or sand depending on the elevation
    private void newTerrainUtil(int row, int col, Terrain[][] grid) {
        if (heightMap[row][col] < WATER_ELEVATION + BEACH_DISTANCE) grid[row][col] = new Sand();
        else grid[row][col] = new Terrain();
    }

    // Updates the grid by moving Animals to adjacent cells
    public void update() {
        // Create a new grid to store updated state
        Terrain[][] newGrid = new Terrain[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(grid[i], 0, newGrid[i], 0, cols);
        }

        // Process each cell in the grid
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Spawn food near water
                if (newGrid[i][j].getClass() == Terrain.class || newGrid[i][j] instanceof Sand) { // Check if were on grass or sand
                    if (heightMap[i][j] > WATER_ELEVATION && heightMap[i][j] < WATER_ELEVATION + FOOD_RANGE) { // Check if were in the band of acceptable elevations
                        if (Math.abs(heightMap[i][j] - WATER_ELEVATION) <= FOOD_RANGE && random.nextDouble() < 0.001 * (1.0 - Math.abs(heightMap[i][j] - WATER_ELEVATION) / FOOD_RANGE)) { // Check the probability of food to grow
                            newGrid[i][j] = new Food();
                        }
                    }
                } else if (newGrid[i][j] instanceof Mouse) {
                    Mouse mouse = (Mouse) newGrid[i][j];
                    // Possible moves: up, down, left, right
                    int[][] moves = {
                        {i-1, j}, {i+1, j}, {i, j-1}, {i, j+1}
                    };
                    double mRand = Math.random();
                    if (mRand < mouse.move()) {
                        int[] move = moves[random.nextInt(moves.length)];
                        int ni = move[0], nj = move[1];
                        // Check if the move is within bounds and not to a rock or water
                        if (ni >= 0 && ni < rows && nj >= 0 && nj < cols &&
                            !(newGrid[ni][nj] instanceof Rock || newGrid[ni][nj] instanceof Water || newGrid[ni][nj] instanceof Hog || newGrid[ni][nj] instanceof Tiger)) {
                            // If moving to a food cell, Mouse consumes it
                            if (newGrid[ni][nj] instanceof Food) {
                                mouse.eatFood();
                            }
                            // Move Mouse to new position, leave empty terrain behind
                            newTerrainUtil(i, j, newGrid);
                            newGrid[ni][nj] = mouse;
                            mouse.deathCounter++;
                            Color color = mouse.getColor();
                            int red = color.getRed();
                            int green = color.getGreen();
                            int blue = color.getBlue();
                            if (mouse.deathCounter == 50 || (red > 245 && green > 245 && blue > 245)) newGrid[ni][nj] = new Terrain();
                        }
                    }
                } else if (newGrid[i][j] instanceof Hog) {
                    Hog hog = (Hog) newGrid[i][j];
                    // Possible moves: up, down, left, right
                    int[][] moves = {
                        {i-1, j}, {i+1, j}, {i, j-1}, {i, j+1}
                    };
                    if (random.nextBoolean()) {
                        int[] move = moves[random.nextInt(moves.length)];
                        int ni = move[0], nj = move[1];
                        // Check if the move is within bounds and not to a rock or water
                        if (ni >= 0 && ni < rows && nj >= 0 && nj < cols &&
                            !(newGrid[ni][nj] instanceof Rock || newGrid[ni][nj] instanceof Water || newGrid[ni][nj] instanceof Tiger)) {
                            // If moving to a food cell, Hog consumes it
                            if (newGrid[ni][nj] instanceof Food) {
                                hog.eatFood();
                            }
                            // Move Hog to new position, leave empty terrain behind
                            newTerrainUtil(i, j, newGrid);
                            newGrid[ni][nj] = hog;
                            hog.deathCounter++;
                            Color color = hog.getColor();
                            int red = color.getRed();
                            int green = color.getGreen();
                            int blue = color.getBlue();
                            if (hog.deathCounter == 50 || (red > 245 && green > 245 && blue > 245)) newGrid[ni][nj] = new Terrain();
                        }
                    }
                } else if (newGrid[i][j] instanceof Tiger) {
                    Tiger tiger = (Tiger) newGrid[i][j];
                    // Possible moves: up, down, left, right
                    int[][] moves = {
                        {i-1, j}, {i+1, j}, {i, j-1}, {i, j+1}
                    };
                    double tRand = Math.random();
                    if (tRand < tiger.move()) {
                        int[] move = moves[random.nextInt(moves.length)];
                        int ni = move[0], nj = move[1];
                        // Check if the move is within bounds and not to a rock or water
                        if (ni >= 0 && ni < rows && nj >= 0 && nj < cols &&
                            !(newGrid[ni][nj] instanceof Rock || newGrid[ni][nj] instanceof Water)) {
                            // If moving to a food cell, Tiger consumes it
                            if (newGrid[ni][nj] instanceof Food) {
                                tiger.eatFood();
                            }
                            // Move Tiger to new position, leave empty terrain behind
                            newTerrainUtil(i, j, newGrid);
                            newGrid[ni][nj] = tiger;
                            tiger.deathCounter++;
                            Color color = tiger.getColor();
                            int red = color.getRed();
                            int green = color.getGreen();
                            if (tiger.deathCounter == 50 || (red > 245 && green > 245)) newGrid[ni][nj] = new Terrain();
                        }
                    }
                }
            }
        }

        // Update the grid with the new state
        grid = newGrid;
    }

    public Color getColor(int row, int col) {
        Terrain t = terrainAt(row, col);
        int h = (int)heightMap[row][col];
        double sandSub = 10000 / (h + 1);

        if (t.getClass() == Terrain.class) return new Color(0, h, 0);
        else if (t instanceof Water) return new Color(0, ((int)(h * 0.78)), h + 100);
        else if (t instanceof Rock) return new Color(h - 75, h - 75, h - 75);
        else if (t instanceof Sand) return new Color((int)(255 - sandSub), (int)(224 - sandSub), (int)(184 - sandSub));
        else return terrainAt(row, col).getColor();
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

    // Print the height map
    public void printHeightMap() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.printf("%.2f ", heightMap[i][j]);
            }
            System.out.println();
        }
    }
}