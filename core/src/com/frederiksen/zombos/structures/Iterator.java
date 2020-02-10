package com.frederiksen.zombos.structures;

/**
 * An improved Iterator that is both resetable and Iterable.
 *
 * @param <T>
 */
public interface Iterator<T> extends java.util.Iterator<T>, Iterable<T> {
    void reset();

    @Override
    default java.util.Iterator<T> iterator() {
        return this;
    }

    interface Iterable<T> extends java.lang.Iterable<T> {
        @Override
        Iterator<T> iterator();

        Iterator<T> newIterator();
    }
}
