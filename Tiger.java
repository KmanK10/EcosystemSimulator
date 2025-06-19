import java.awt.*;

public class Tiger extends Terrain {
    private int foodEaten;
    private int lightenCounter;
    private double moveChance;
    private static final int MAX_DARKEN = 3;
    private static final int DARKEN_STEP = 50;
    private static final int LIGHTEN_STEP = 2;
    public int deathCounter = 0;

    public Tiger() {
        super(Color.ORANGE);
        this.foodEaten = 0;
        this.moveChance = 0.5;
        this.lightenCounter = 0;
        this.deathCounter = 0;
    }

    public void eatFood() {
        if (foodEaten < MAX_DARKEN) {
            foodEaten++;
        }
        moveChance *= 1.25;
        deathCounter -= 15;
    }

    public Color getColor() {
        lightenCounter++;

        // Base tiger orange: RGB(255, 165, 0)
        int red = clamp(255 - foodEaten * DARKEN_STEP + lightenCounter * LIGHTEN_STEP);
        int green = clamp(165 - foodEaten * DARKEN_STEP + lightenCounter * LIGHTEN_STEP);
        int blue = 0;

        return new Color(red, green, blue);
    }

    public double move() {
        return moveChance;
    }
}
