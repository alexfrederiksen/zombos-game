package com.frederiksen.zombos.structures;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicVector2 {
    public class Descriptor {
        public int x;
        public int y;
    }

    public class Atom {
        public AtomicInteger value = new AtomicInteger();
        public AtomicReference<Descriptor> descriptor = new AtomicReference<>();
    }

    public Atom x;
    public Atom y;

    public void swap(int x, int y) {

    }

    public boolean cas(Descriptor d) {

        boolean fail = false;
        while (true) {
            x.descriptor.compareAndSet(null, d);
            Descriptor v = x.descriptor.get();
            if (v == d) {
                break;
            } else if (v != null) {
                cas(v);
            } else {
                fail = true;
                break;
            }
        }


        return false;
    }
}
