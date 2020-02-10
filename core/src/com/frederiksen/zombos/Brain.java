package com.frederiksen.zombos;

import java.util.concurrent.atomic.AtomicInteger;

public class Brain {
    private AtomicInteger progress;


    public void start() {

    }

    public void run() {
        progress.addAndGet(4);
    }
}
