// Davin Yoon and Kiefer Menard CS 142 Professor Julio Garabay

import javax.swing.*;
import java.awt.*;

// Graphical user interface for the ecosystem simulation
public class GUI {
    private static final int PIXEL_SIZE = 15; // Size of each grid cell in pixels
    private Model model; // Reference to the simulation model
    private JFrame window; // Main window for the GUI
    private JPanel gridPanel; // Panel to display the grid
    private final static int SIZE = 60; // Default grid size (used in main)

    // Constructor initializes the GUI with the given model
    public GUI(Model model) {
        this.model = model;
        window = new JFrame("Ecosystem Simulator"); // Create main window
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close window on exit

        // Create menu bar for user interactions
        JMenuBar menuBar = new JMenuBar();

        // Button to regenerate the terrain
        JButton regenerateTerrainBtn = new JButton("Regenerate Terrain");
        regenerateTerrainBtn.addActionListener(e -> model.generateTerrain()); // Regenerate terrain on click
        menuBar.add(regenerateTerrainBtn); // Add button to menu bar

        // Menu for adding terrain types
        JMenu addMenu = new JMenu("Add");

        // Button to add food to a random empty cell
        JButton addFoodBtn = new JButton("Food");
        addFoodBtn.addActionListener(e -> model.replaceTerrain(new Food())); // Add Food on click
        addMenu.add(addFoodBtn); // Add button to menu

        // Button to add an animal to a random empty cell
        JButton addHogBtn = new JButton("Hog");
        addHogBtn.addActionListener(e -> model.replaceTerrain(new Hog())); // Add Hog on click
        addMenu.add(addHogBtn); // Add button to menu

        // Button to add an animal to a random empty cell
        JButton addMouseBtn = new JButton("Mouse");
        addMouseBtn.addActionListener(e -> model.replaceTerrain(new Mouse())); // Add Animal on click
        addMenu.add(addMouseBtn); // Add button to menu

        // Button to add an animal to a random empty cell
        JButton addTigerBtn = new JButton("Tiger");
        addTigerBtn.addActionListener(e -> model.replaceTerrain(new Tiger())); // Add Animal on click
        addMenu.add(addTigerBtn); // Add button to menu

        menuBar.add(addMenu); // Add menu to menu bar
        
        window.setJMenuBar(menuBar); // Set the menu bar for the window

        // Create panel to render the grid
        gridPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g); // Clear the panel
                // Draw each cell based on its terrain type's color
                for (int i = 0; i < model.getRows(); i++) {
                    for (int j = 0; j < model.getCols(); j++) {
                        g.setColor(model.getColor(i, j)); // Get color from terrain
                        g.fillRect(j * PIXEL_SIZE, i * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE); // Draw cell
                    }
                }
            }
        };

        // Set panel size based on grid dimensions and pixel size
        gridPanel.setPreferredSize(new Dimension(model.getCols() * PIXEL_SIZE, model.getRows() * PIXEL_SIZE));
        window.add(gridPanel); // Add panel to window
        window.pack(); // Size window to fit contents
        window.setLocationRelativeTo(null); // Center window on screen
        window.setVisible(true); // Show the window
        // Start a timer to update the simulation every 500ms
        new Timer(500, e -> update()).start();
    }

    // Updates the model and repaints the grid
    public void update() {
        model.update(); // Update simulation state
        gridPanel.repaint(); // Redraw the grid
    }

    // Main method to launch the GUI
    public static void main(String[] args) {
        // Run GUI creation on the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> {
            Model model = new Model(SIZE, SIZE); // Create a new model
            new GUI(model); // Create and show the GUI
        });
    }
}