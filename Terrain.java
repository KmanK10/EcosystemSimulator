import java.awt.*;

public class Terrain {
    private Color color;
    

    public Terrain() {color = Color.GREEN;}
    public Terrain(Color color) {this.color = color;}
    
    public Color getColor() {return color;}

    // Utility function to clamp a value between 0 and 255
    public int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}