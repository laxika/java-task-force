package com.morethanheroic.taskforce.job;

import com.morethanheroic.taskforce.task.Task;
import com.morethanheroic.taskforce.task.TaskDescriptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class JobExecutor {

    public void execute(final Job job) {
        final Map<Integer, ExecutorService> executorServiceHashMap = new HashMap<>();

        for (TaskDescriptor taskDescriptor : job.getTasks()) {
            final Task task = taskDescriptor.getTask();
            final int parallelismLevel = taskDescriptor.getParallelismLevel();

            if (parallelismLevel == 1) {
                executorServiceHashMap.put(System.identityHashCode(task), Executors.newSingleThreadExecutor());
            } else {
                executorServiceHashMap.put(System.identityHashCode(task),
                        Executors.newFixedThreadPool(parallelismLevel));
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

            CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(optional::get);

            for (TaskDescriptor taskDescriptor : job.getTasks()) {
                completableFuture = completableFuture.thenApplyAsync((xyz) -> taskDescriptor.getTask().execute(xyz),
                        executorServiceHashMap.get(System.identityHashCode(taskDescriptor.getTask())));
            }

            final CompletableFuture<Void> closingFuture = completableFuture.thenAccept((asd) -> job.getSink().consume(asd));
            closingFuture.thenAccept((asd) -> atomicInteger.decrementAndGet());
        }

        //Block until everything finish
        while (atomicInteger.get() != 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
