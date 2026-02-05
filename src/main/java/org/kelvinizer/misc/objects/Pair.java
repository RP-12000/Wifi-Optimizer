package org.kelvinizer.misc.objects;

/**
 * A Pair is a set of two objects.
 * @param <A> The type of the first object
 * @param <B> The type of the second object
 * @author Boyan Hu
 */
public class Pair<A, B> {
    /**
     * The first object with type A
     */
    public A first;

    /**
     * The second object with type B
     */
    public B second;

    /**
     * Construct a {@code Pair} with the two types defined.
     * @param a the first object
     * @param b the second object
     */
    public Pair(A a, B b){
        first = a;
        second = b;
    }
}