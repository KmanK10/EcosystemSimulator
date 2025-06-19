# Ecosystem Simulation

## Overview

This project is a grid-based ecosystem simulation developed in Java for the CS 142 course under Professor Julio Garabay. It generates a procedurally created terrain using Perlin noise and simulates the growth of food (berries) and the behavior of animals (mice, hogs, and tigers) within the environment. The terrain consists of water, sandy beaches, grassy areas, and scattered rocks, with berries growing near water based on a probabilistic model. Animals move, eat, and die based on hunger and age, with a food chain where hogs eat mice, and tigers eat everything. A graphical user interface (GUI) built with Swing allows users to visualize the simulation and interact with it by regenerating the terrain or adding food and animals.

## Features

- **Procedural Terrain Generation**: Uses Perlin noise to create a natural-looking height map with values in `[0, 255]`, representing terrain elevation.
- **Terrain Types**:
  - **Water**: Areas below `WATER_ELEVATION` (120), colored blue.
  - **Sand**: Beaches within `BEACH_DISTANCE` (20) above water, colored sandy yellow.
  - **Grass**: Default terrain for higher elevations, colored green.
  - **Rocks**: Obstacles scattered randomly, colored gray.
- **Food Growth**: Berries (red) grow near water (within `FOOD_RANGE = 30` of `WATER_ELEVATION`) with a 0.1% base probability, scaled linearly by proximity to water.
- **Animal Behavior**:
  - **Mice** (light brown), **Hogs** (dark brown), and **Tigers** (orange) move randomly to adjacent cells (up, down, left, right).
  - Animals eat berries; hogs also eat mice, and tigers eat all animals and berries.
  - Animals have a hunger mechanic: their color lightens as they fail to eat, and they die if they become too light (RGB &gt; 245) or reach an age limit (50 updates).
- **GUI**:
  - Displays the grid with color-coded terrain and animals (15x15 pixels per cell).
  - Updates every 500ms to show movement and changes.
  - Includes a menu to:
    - Regenerate the terrain with a new random seed.
    - Add food, mice, hogs, or tigers to random empty (grass) cells.
- **Randomization**: Uses a random seed based on system time for varied terrain generation each run.

## Project Structure

The project consists of the following Java classes:

- `ImprovedNoise.java`: Implements Ken Perlin’s improved Perlin noise algorithm to generate smooth, natural height maps.
- `Terrain.java`: Base class for terrain types, defining color and a utility `clamp` method.
- **Terrain Subclasses**:
  - `Rock`: Gray obstacles that animals cannot move through.
  - `Water`: Blue areas below `WATER_ELEVATION`.
  - `Sand`: Yellow beaches near water.
  - `Food`: Red berries that grow probabilistically near water.
  - `Mouse`, `Hog`, `Tiger`: Animal classes with movement, eating, and death mechanics, differing in color and food chain behavior.
- `Model.java`: Manages the simulation logic, including terrain generation, food growth, animal movement, and grid updates.
- `GUI.java`: Provides a Swing-based interface to visualize the grid and interact with the simulation.

## Installation

1. **Prerequisites**:

   - Java Development Kit (JDK) 8 or higher.
   - A Java IDE (e.g., IntelliJ IDEA, Eclipse) or command-line tools for compilation and execution.

2. **Setup**:

   - Clone or download the project files to a local directory.
   - Ensure all Java files (`ImprovedNoise.java`, `GUI.java`, `Terrain.java`, `Model.java`, `Rock.java`, `Water.java`, `Sand.java`, `Food.java`, `Mouse.java`, `Hog.java`, `Tiger.java`) are in the same directory.

3. **Compilation**:

   - Compile the Java files using a command like:

     ```bash
     javac *.java
     ```
   - Or use your IDE’s build tools.

4. **Running the Simulation**:

   - Run the `GUI` class to start the simulation:

     ```bash
     java GUI
     ```
   - This creates a 60x60 grid (default size) and launches the GUI.

## Usage

- **Launch**: Run `GUI.java` to open the simulation window.
- **View**: The grid displays terrain and animals:
  - **Blue**: Water (height &lt; 120).
  - **Yellow**: Sand (height 120–140).
  - **Green**: Grass (height &gt; 140).
  - **Gray**: Rocks.
  - **Red**: Food (berries).
  - **Light Brown**: Mice.
  - **Dark Brown**: Hogs.
  - **Orange**: Tigers.
- **Interact**:
  - **Regenerate Terrain**: Click “Regenerate Terrain” in the menu to create a new map with a random seed.
  - **Add Items**: Use the “Add” menu to place food, mice, hogs, or tigers in random grass cells.
- **Simulation**: The grid updates every 500ms, showing animals moving, eating, and dying, and food growing near water.

## Implementation Details

- **Height Map**: Generated using Perlin noise with parameters:
  - `scale = 0.1`: Controls terrain smoothness.
  - `octaves = 6`: Adds detail through multiple noise layers.
  - `persistence = 0.1`: Reduces amplitude of higher octaves.
  - `lacunarity = 2.0`: Doubles frequency per octave.
  - Normalized to `[0, 255]` with a non-linear contrast adjustment (`Math.pow(normalized, 0.8)`).
- **Food Growth**: Berries spawn on grass or sand within `[90, 150]` (120 ± 30) with a probability of `0.001 * (1 - |height - 120| / 30)`, checked during updates.
- **Animal Mechanics**:
  - Move randomly with class-specific probabilities (e.g., `Mouse.move()`, `Tiger.move()`).
  - Cannot move to rocks, water, or certain animals (e.g., tigers block mice and hogs).
  - Consume food or prey, resetting hunger (color darkens).
  - Die after 50 updates or if color becomes too light (RGB &gt; 245).
- **GUI**: Uses Swing with a `JPanel` for rendering and a `Timer` for updates.

## Known Limitations

- **Sparse Food**: The 0.1% base probability for food growth may result in sparse berry placement. Adjust the probability in `Model.update()` (e.g., increase `0.001` to `0.01`) for more food.
- **Fixed Grid Size**: The default 60x60 grid is hardcoded in `GUI.java`. Modify `SIZE` for different dimensions.
- **Animal Density**: Maximum counts (`foodMax`, `rockMax`, etc.) limit entity placement, which may feel restrictive on smaller grids.
- **Performance**: Large grids or frequent updates may slow down due to the Perlin noise computation and grid updates.

## Future Improvements

**Dynamic Grid Size**: Allow users to specify grid dimensions via command-line arguments or GUI input.

- **Tunable Parameters**: Expose terrain generation parameters (e.g., `scale`, `persistence`) in the GUI.
- **Enhanced Visualization**: Add a height map overlay or color-coded probability map for food growth.
- **Animal Interactions**: Implement reproduction or more complex food chain dynamics.
- **Save/Load**: Add functionality to save and load terrain states.

## Authors

- Davin Yoon
- Kiefer Menard

## Acknowledgments

- Professor Julio Garabay, CS 142.
- Ken Perlin’s Improved Noise algorithm (2002) for terrain generation.
- AI assistance for writing the `generateHeightMap` function.