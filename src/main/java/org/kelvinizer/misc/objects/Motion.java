package org.kelvinizer.misc.objects;

/**
 * A motion is an exponent function with respect to time.
 * It is in the form of f(t)=a*t<sup>c</sup>+b where b and c are user-defined parameters.
 * It serves as the basis for all movement animation in the program, such as the
 * motion of {@code DynamicObject} and the motion of {@code note}.
 * @author Boyan Hu
 */
public class Motion {
    /**
     * /The starting time of the motion, in seconds.
     */
    private final double start;

    /**
     * The ending time of the motion, in seconds.
     */
    private final double end;

    /**
     * The starting point of a motion.
     */
    private final double startPoint;

    /**
     * The exponent c in f(t)=a*t<sup>c</sup>+b.
     * In the context below, we call this the
     * "acceleration" of the motion
     */
    private final double exp;

    /**
     * This constant is NOT set by the user, but calculated
     * using the formula in the constructor, so that the
     * function satisfies the given conditions.
     */
    private final double constant;

    /**
     * Create a new {@code Motion} object using the given parameters.
     * @param s The starting time of the motion
     * @param e The ending time of the motion
     * @param sp The starting point of the motion
     * @param ep The ending point of the motion
     * @param exp The "acceleration" (exponent) of the motion
     */
    public Motion(double s, double e, double sp, double ep, double exp){
        start = s;
        end = e;
        this.exp = exp;
        constant = (ep-sp)/Math.pow(e-s, exp);
        startPoint = sp;
    }

    /**
     * Construct a new linear {@code motion} object.
     * This means that the "acceleration" (exponent) is 1.
     * @param s The starting time of the motion
     * @param e The ending time of the motion
     * @param sp The starting point of the motion
     * @param ep The ending point of the motion
     */
    public Motion(double s, double e, double sp, double ep){
        this(s, e, sp, ep, 1);
    }

    /**
     * Parsing a {@code String} object into a motion. The
     * {@code String} object needs to be in the form
     * {s, e, sp, ep, exp} for the constructor to work, with
     * spaces instead of commas in between the double values.
     * @param s The string that contains data that can be parsed into a motion
     */
    public Motion(String s){
        this(
                Double.parseDouble(s.split(" ")[0]),
                Double.parseDouble(s.split(" ")[1]),
                Double.parseDouble(s.split(" ")[2]),
                Double.parseDouble(s.split(" ")[3]),
                Double.parseDouble(s.split(" ")[4])
        );
    }

    /**
     * Checks whether a time point passed in is within the
     * {@code Motion} object time frame, that is, a between
     * {@code start} and {@code end}.
     * @param time The time point to be processed
     * @return whether the given time is within the motion time frame
     */
    public boolean contains(double time){
        return start<time && time<end;
    }

    /**
     * Calculates the position of an object that has
     * this {@code Motion} object within it using the
     * given time stamp
     * @param time The current time
     * @return The position of the object associated with this motion
     */
    public double getPos(double time){
        double t = time - start;
        return constant*Math.pow(t, exp)+startPoint;
    }

    /**
     * Get the starting time of the motion
     * @return the starting time of the motion
     */
    public double getStart() {
        return start;
    }

    /**
     * Get the ending time of the motion
     * @return the ending time of the motion
     */
    public double getEnd() {
        return end;
    }
}