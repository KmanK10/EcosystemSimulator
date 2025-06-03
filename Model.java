import java.io.*;
import java.util.*;

// Houses the data and logic for the simulation.
public class Model {
    private boolean[][] grid; // 2D array to store the grid with living cells being true and dead ones being false
    private int rows;     // Number of rows in the grid
    private int cols;     // Number of columns in the grid

    // Reads file and stores input in grid
    public Model(String fileName) {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            rows = scanner.nextInt();
            cols = scanner.nextInt();
            
            scanner.nextLine(); 
            grid = new boolean[rows][cols];

            for (boolean[] x : grid) {
                String line = scanner.next();

                for (int i = 0; i < x.length; i++) {
                    x[i] = line.charAt(i) == 'X';
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found: " + fileName);
        }
    }

    public int getRows() {return rows;}
    public int getCols() {return cols;}

    // Checks if the cell at row, col is alive
    public boolean isAlive(int row, int col) {return row >= 0 && row < rows && col >= 0 && col < cols && grid[row][col];}

    // Updates grid based on the rules of "The Game of Life"
    public void update() {
        boolean[][] temp = new boolean[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = countNeighbors(i, j);
                if (x <= 1 || x >= 4) {
                    temp[i][j] = false;
                } else if (x == 3) {
                    temp[i][j] = true;
                } else {
                    temp[i][j] = grid[i][j];
                }
            }
        }
        grid = temp;
    }

    // Counts the number of alive neighbors for a cell, got help from Davin for this method
    private int countNeighbors(int row, int col) {
        int c = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                if (isAlive(row + i, col + j)) c++;
            }
        }
        return c;
    }

    // Returns the current state of the grid as a string
    public String toString() {
        String output = "";

        for (boolean[] x : grid) {
            for (boolean y: x) output += y ? "X" : "-";
            
            output += "\n";
        }

        return output;
    }
}
