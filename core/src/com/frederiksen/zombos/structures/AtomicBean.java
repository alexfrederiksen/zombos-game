package com.frederiksen.zombos.structures;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class AtomicBean {
    public static class Descriptor {
        private Atom oldState, newState;

    }

    public static class Atom<T> {
        private T               value;
        private AtomicReference<Descriptor> tag;

        public void swap(T newValue) {
        }

        public void tag(Descriptor d) {
            tag.compareAndSet(tag.get(), d);
        }


        public AtomicReference<Descriptor> getTag() {
            return tag;
        }
    }


}
