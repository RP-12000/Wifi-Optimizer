package org.kelvinizer.animation;

import javax.swing.*;
import java.util.concurrent.*;

/**
 * The {@code AnimatableFrame} class provides an abstract base for creating a frame with animated
 * panels for the PianoTilesPro game. It extends {@link JFrame} and uses a scheduled executor
 * to run the game loop at a fixed frame rate.
 * Subclasses are required to implement the {@link #boot()} and {@link #runGame()} methods
 * to define initialization behavior and the game loop logic.
 *
 * @author Boyan Hu
 */
public abstract class AnimatableFrame extends JFrame {

    /**
     * The currently displayed panel within the frame.
     */
    protected AnimatablePanel display;

    /**
     * The index of the last panel displayed.
     * This helps in tracking panel transitions.
     */
    protected int lastPanel = 0;

    /**
     * Constructs an {@code AnimatableFrame} and initializes the application window.
     * This includes setting the window size, initializing the game through {@link #boot()},
     * and starting the game loop using a scheduled executor.
     * The frame is made visible upon construction.
     */
    public AnimatableFrame() {
        setSize(Params.REF_WIN_W, Params.REF_WIN_H);
        boot();
        ScheduledExecutorService gameLoop = Executors.newSingleThreadScheduledExecutor();
        gameLoop.scheduleAtFixedRate(this::runGame, 0, 1000 / Params.FPS, TimeUnit.MILLISECONDS);
        setVisible(true);
    }

    /**
     * Initializes the application window and sets up the initial display panel.
     * Subclasses should implement this method to define the game environment,
     * load resources, and configure the initial state.
     */
    public abstract void boot();

    /**
     * Executes the game loop logic. This method is called at a fixed rate by the
     * scheduled executor to update the game state, render the display panel,
     * and handle panel transitions.
     * Implementations should ensure proper rendering and resource management.
     */
    public abstract void runGame();
}