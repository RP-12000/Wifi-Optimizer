package org.kelvinizer.animation;

/**
 * The {@code Params} class holds global parameters for the animation settings.
 */
public class Params {
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

    /**
     * Initializes the parameters with the specified values.
     *
     * @param FPS the frames per second
     * @param REF_WIN_W the reference window width
     * @param REF_WIN_H the reference window height
     */
    public static void init(long FPS, int REF_WIN_W, int REF_WIN_H) {
        Params.FPS = FPS;
        Params.REF_WIN_W = REF_WIN_W;
        Params.REF_WIN_H = REF_WIN_H;
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
        Params.FPS = 60;
        Params.REF_WIN_W = 800;
        Params.REF_WIN_H = 600;
    }
}