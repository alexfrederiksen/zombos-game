package com.frederiksen.zombos.structures;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class Group<T> implements Iterable<T> {
    public LinkedList<T>                  elements;
    public HashSet<Group<? extends T>>    subgroups;

    public Group() {
        elements = new LinkedList<>();
        subgroups = new HashSet<>();
    }

    public int size() {
        int size = elements.size();
        for (Group g : subgroups) {
            size += g.size();
        }
        return size;
    }

    public boolean add(T element) {
        return elements.add(element);
    }

    public boolean addSubgroup(Group<? extends T> group) {
        return subgroups.add(group);
    }

    /**
     * Recursively collapses groups so that they are only one level deep,
     * guaranteeing no diamonds or other funny business.
     */
    public void collapse() {
        for (Group<? extends T> g : subgroups) {
            g.collapse();
            subgroups.addAll(g.subgroups);
        }
    }

    public boolean move(T element, Group<? super T> dest) {
        boolean removed = elements.remove(element);

        if (removed) dest.add(element);
        else return false;

        return true;
    }

    @Override
    public Iter iterator() {
        return new Iter();
    }

    /**
     * Iterates only on elements and not recursively into subgroups
     * @return
     */
    public Iterator<T> localIterator() {
        return elements.iterator();
    }

    public class Iter implements Iterator<T> {
        Iterator<? extends T> elementIterator = elements.iterator();
        Iterator<Group<? extends T>> groupIterator = subgroups.iterator();

        public void reset() {
            elementIterator = null;
            groupIterator = null;
        }

        @Override
        public boolean hasNext() {
            if (elementIterator.hasNext()) {
                return true;
            } else {
                if (groupIterator.hasNext()) {
                    elementIterator = groupIterator.next().localIterator();
                    return hasNext();
                } else {
                    return false;
                }
            }
        }


        @Override
        public T next() {
            if (elementIterator.hasNext()) {
                return elementIterator.next();
            } else if (groupIterator.hasNext()) {
                elementIterator = groupIterator.next().localIterator();
                return next();
            }
            // no next element
            return null;
        }

        @Override
        public void remove() {
            elementIterator.remove();
        }
    }
}
