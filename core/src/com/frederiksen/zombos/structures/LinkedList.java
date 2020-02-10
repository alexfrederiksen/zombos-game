package com.frederiksen.zombos.structures;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * An improved linked list that implements resetable iterators, cleaning up garbage.
 * (Not thread safe)
 *
 * @param <T>
 */
public class LinkedList<T> implements Collection<T>, Iterator.Iterable<T> {
    public class Node {
        public T value;
        public Node next;

        public Node(T value) {
            this.value = value;
        }
    }

    private Node head;
    private LinkedIterator iterator = new LinkedIterator();

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        iterator.reset();
        for (T obj : iterator) {
            if (obj.equals(o)) return true;
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];

        iterator.reset();
        int i = 0;
        for (T obj : iterator) {
            arr[i] = obj;
            i++;
        }

        return arr;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (a.length < size) throw new ArrayStoreException("Not enough space in array.");

        iterator.reset();
        int i = 0;
        for (T obj : iterator) {
            a[i] = (T1) obj;
            i++;
        }

        return a;
    }

    @Override
    public boolean add(T t) {
        Node node = new Node(t);
        node.next = head;
        head = node;

        return true;
    }

    @Override
    public boolean remove(Object o) {
        iterator.reset();
        for (T obj : iterator) {
            if (obj.equals(o)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T o : c) {
            add(o);
        }

        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        iterator.reset();
        for (T o : iterator) {
            if (c.contains(o)) {
                iterator.remove();
            }
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        iterator.reset();
        for (T o : iterator) {
            if (!c.contains(o)) {
                iterator.remove();
            }
        }

        return true;
    }

    @Override
    public void clear() {
        head = null;
    }

    @Override
    public Iterator<T> iterator() {
        return iterator;
    }

    @Override
    public Iterator<T> newIterator() {
        return new LinkedIterator();
    }

    private class LinkedIterator implements Iterator<T> {
        private Node cur;
        private Node parent;
        private boolean calledRemove;

         public LinkedIterator() {
             reset();
         }

        @Override
        public void reset() {
            cur = null;
            parent = null;
            calledRemove = true;
        }

        @Override
        public boolean hasNext() {
            return head != null && (cur != null || parent == null);
        }

        @Override
        public T next() {
            parent = cur;
            if (cur == null) {
                cur = head;
            } else {
                cur = cur.next;
            }

            if (cur == null) throw new NoSuchElementException("No more elements.");

            calledRemove = false;

            return cur.value;
        }

        @Override
        public void remove() {
            if (calledRemove) throw new IllegalStateException("Remove already called on this element.");

            if (parent != null) {
                parent.next = cur.next;
                cur = parent;
                parent = null;
            } else {
                head = cur.next;
                cur = null;
                parent = null;
            }

            calledRemove = true;
            size--;
        }
    }
}
