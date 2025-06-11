import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        // Scanner input = new Scanner(System.in);
        // System.out.print("Welcome to the Ecosystem Simulator.\nPlease specify the dimensions\nWidth: ");
        // int width = input.nextInt();
        // System.out.print("Height: ");
        // Model model = new Model(width, input.nextInt());

        Model model = new Model(30, 30);
        model.generateTerrain();

        //input.close();

        System.out.print(model.toString());   
        
        GUI gui = new GUI(model);
    }
}