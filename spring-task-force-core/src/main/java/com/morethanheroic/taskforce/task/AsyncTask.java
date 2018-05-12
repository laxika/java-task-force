package com.morethanheroic.taskforce.task;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncTask<INPUT, OUTPUT> implements Task<INPUT, OUTPUT> {

    private final Task<INPUT, OUTPUT> delegate;
    private final ExecutorService executor;

    public AsyncTask(final int parallelism, final Task<INPUT, OUTPUT> delegate) {
        this.delegate = delegate;

        this.executor =  Executors.newFixedThreadPool(parallelism);
    }

    @Override
    public OUTPUT execute(INPUT input) {
        try {
            return executor.submit(() -> delegate.execute(input)).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
