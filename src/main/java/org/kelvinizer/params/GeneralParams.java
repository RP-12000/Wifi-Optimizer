package org.kelvinizer.params;

/**
 * The {@code Params} class holds global parameters for the animation back.
 */
public class GeneralParams {
    /**
     * Frames per second for the animation.
     */
    public static long FPS;

    /**
     * Reference window width.
     */
    public static int REF_WIN_W;

    /**
     * Reference window height.
     */
    public static int REF_WIN_H;

    public static int extraWidth = 12;

    public static int extraHeight = 35;

    public static int panelIndex = 0;

    /**
     * Initializes the parameters with the specified values.
     *
     * @param FPS the frames per second
     * @param REF_WIN_W the reference window width
     * @param REF_WIN_H the reference window height
     */
    public static void init(long FPS, int REF_WIN_W, int REF_WIN_H) {
        GeneralParams.FPS = FPS;
        GeneralParams.REF_WIN_W = REF_WIN_W;
        GeneralParams.REF_WIN_H = REF_WIN_H;
    }

    /**
     * Initializes the parameters with default values.
     * Default values are:
     * <ul>
     *   <li>FPS: 60</li>
     *   <li>REF_WIN_W: 800</li>
     *   <li>REF_WIN_H: 600</li>
     * </ul>
     */
    public static void init() {
        GeneralParams.FPS = 60;
        GeneralParams.REF_WIN_W = 800;
        GeneralParams.REF_WIN_H = 600;
    }
}