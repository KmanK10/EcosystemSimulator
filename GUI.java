import javax.swing.*;
import java.awt.*;

// Runs the Simulation in a Swing GUI
public class GUI {
    private static final int PIXEL_SIZE = 25;
    private Model model;    // The grid data
    private JFrame window;      // The window
    private JPanel gridPanel;   // The drawing area

    // Set up the GUI
    public GUI(Model model) {
        this.model = model;
        window = new JFrame("Ecosystem Simulator");
        gridPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g); // Clear the panel

                // Create the "pixels" of the GUI
                for (int i = 0; i < model.getRows(); i++) {
                    for (int j = 0; j < model.getCols(); j++) {
                        g.setColor(model.terrainAt(i, j).getColor());
                        g.fillRect(j * PIXEL_SIZE, i * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
                    }
                }
            }
        };

        gridPanel.setPreferredSize(new Dimension(model.getCols() * PIXEL_SIZE, model.getRows() * PIXEL_SIZE));
        window.add(gridPanel);
        window.pack();
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setVisible(true);
        new Timer(500, e -> update()).start(); // Update every 0.5s
    }

    // Update the model and repaint the grid
    public void update() {
        //model.update();
        gridPanel.repaint();
    }
}
