package com.morethanheroic.taskforce.job;

import com.morethanheroic.taskforce.task.AsyncTask;
import com.morethanheroic.taskforce.task.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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


        Optional<?> optional = job.getGenerator().generate();

        while (optional.isPresent()) {
            CompletableFuture completableFuture = CompletableFuture.supplyAsync(() -> optional.get());

            for (Task task : job.getTasks()) {
                completableFuture = completableFuture.thenApplyAsync((xyz) -> task.execute(xyz), executorServiceHashMap.get(System.identityHashCode(task)));
            }

            completableFuture.thenAccept((asd) -> job.getSink().consume(asd));
        }
    }
}
