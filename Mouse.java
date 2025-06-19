import java.awt.*;

public class Mouse extends Terrain {
    private int foodEaten;
    private double moveChance;
    private int lightenCounter;
    private static final int DARKEN_STEP = 50;   // Smooth, not too sharp
    private static final int LIGHTEN_STEP = 3;   // Gradual brightening
    public int deathCounter;

    public Mouse() {
        super(new Color(196, 164, 132));  // Light brown base
        this.foodEaten = 0;
        this.moveChance = 0.5;
        this.lightenCounter = 0;
        this.deathCounter = 0;
    }

    public void eatFood() {
        moveChance /= 2;
        foodEaten++;  // darken when eating
        deathCounter -= 15;
    }

    public double move() {
        return moveChance;
    }

    public Color getColor() {
        lightenCounter++;  // lighten each turn

        // Base color: RGB(196, 164, 132)
        int red = clamp(196 - foodEaten * DARKEN_STEP + lightenCounter * LIGHTEN_STEP);
        int green = clamp(164 - foodEaten * DARKEN_STEP + lightenCounter * LIGHTEN_STEP);
        int blue = clamp(132 - foodEaten * DARKEN_STEP + lightenCounter * LIGHTEN_STEP);

        return new Color(red, green, blue);
    }
}
