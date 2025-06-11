import java.awt.*;

public class Terrain {
    private Color color;

    public Terrain() {color = new Color(16, 177, 80);}
    public Terrain(Color color) {this.color = color;}
    
    public Color getColor() {return color;}

    public String toString() {return "[ ]";}
}