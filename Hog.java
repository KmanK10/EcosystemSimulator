import java.awt.*;

public class Hog extends Terrain {
    private int foodEaten;
    private int lightenCounter;
    private static final int MAX_DARKEN = 3;
    private static final int DARKEN_STEP = 50;  // slightly smaller for smoother changes
    private static final int LIGHTEN_STEP = 3;
    public int deathCounter = 0;

    public Hog() {
        this.foodEaten = 0;
        this.lightenCounter = 0;
        this.deathCounter = 0;
    }

    public void eatFood() {
        if (foodEaten < MAX_DARKEN) {
            foodEaten++;
        }
        deathCounter -= 15;
    }

    public Color getColor() {
        lightenCounter++;  // brighten gradually every turn

        // Base brown: RGB(139, 69, 19)
        int red = clamp(139 - foodEaten * DARKEN_STEP + lightenCounter * LIGHTEN_STEP);
        int green = clamp(69 - foodEaten * DARKEN_STEP + lightenCounter * LIGHTEN_STEP);
        int blue = clamp(19 - foodEaten * DARKEN_STEP + lightenCounter * LIGHTEN_STEP);

        return new Color(red, green, blue);
    }
}