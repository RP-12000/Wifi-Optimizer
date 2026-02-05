package org.kelvinizer.misc.objects;

/**
 * A Triple is a set of three objects.
 * @param <A> The type of the first object
 * @param <B> The type of the second object
 * @param <C> The type of the third object
 * @author Boyan Hu
 */
public class Triple<A, B, C> {
    /**
     * The first object with type A
     */
    public A first;

    /**
     * The second object with type B
     */
    public B second;

    /**
     * The third object with type C
     */
    public C third;

    /**
     * Construct a {@code Triple} object with the three types defined.
     * @param a the first object
     * @param b the second object
     * @param c the third object
     */
    public Triple(A a, B b, C c){
        first = a;
        second = b;
        third = c;
    }
}