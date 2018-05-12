package com.morethanheroic.taskforce.job;

import com.morethanheroic.taskforce.task.AsyncTask;
import com.morethanheroic.taskforce.task.Task;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class JobExecutor {

    public void execute(final Job job) {
        final Map<Integer, ExecutorService> executorServiceHashMap = new HashMap<>();

        for (Task task : job.getTasks()) {
            if (task instanceof AsyncTask) {
                executorServiceHashMap.put(System.identityHashCode(task), Executors.newFixedThreadPool(((AsyncTask) task).getParallelism()));
            } else {
                executorServiceHashMap.put(System.identityHashCode(task), Executors.newSingleThreadExecutor());
            }
        }

        final AtomicInteger atomicInteger = new AtomicInteger();

        boolean shouldRun = true;
        while (shouldRun) {
            Optional<?> optional = job.getGenerator().generate();

            if (!optional.isPresent()) {
                shouldRun = false;
                continue;
            }

            atomicInteger.incrementAndGet();

            CompletableFuture completableFuture = CompletableFuture.supplyAsync(() -> optional.get());

            for (Task task : job.getTasks()) {
                completableFuture = completableFuture.thenApplyAsync((xyz) -> task.execute(xyz), executorServiceHashMap.get(System.identityHashCode(task)));
            }

            completableFuture = completableFuture.thenAccept((asd) -> job.getSink().consume(asd));
            completableFuture.thenAccept((asd)->atomicInteger.decrementAndGet());
        }

        //Block until everything finish
        while(atomicInteger.get() != 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
