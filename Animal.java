import java.awt.*;

public class Animal extends Terrain {
    private int foodEaten;
    private static final int MAX_DARKEN = 3;
    private static final int DARKEN_STEP = 30;

    public Animal() {
        this.foodEaten = 0;
    }

    public void eatFood() {
        if (foodEaten < MAX_DARKEN) {
            foodEaten++;
        }
    }

    public Color getColor() {
        // Base brown color: RGB(139, 69, 19)
        int red = Math.max(0, 139 - foodEaten * DARKEN_STEP);
        int green = Math.max(0, 69 - foodEaten * DARKEN_STEP);
        int blue = Math.max(0, 19 - foodEaten * DARKEN_STEP);
        return new Color(red, green, blue);
    }

    public String toString() {
        return "[A]";
    }
}