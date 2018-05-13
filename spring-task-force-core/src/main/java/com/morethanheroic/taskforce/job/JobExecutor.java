package com.morethanheroic.taskforce.job;

import com.morethanheroic.taskforce.task.TaskDescriptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class JobExecutor {

    public void execute(final Job job) {
        final Map<Integer, ExecutorService> executorServiceHashMap = new HashMap<>();

        for (TaskDescriptor taskDescriptor : job.getTasks()) {
            final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                    taskDescriptor.getParallelismLevel(),
                    taskDescriptor.getParallelismLevel(),
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(taskDescriptor.getMaxQueueSize()),
                    Executors.defaultThreadFactory(),
                    new ThreadPoolExecutor.CallerRunsPolicy()
            );

            executorServiceHashMap.put(System.identityHashCode(taskDescriptor.getTask()), threadPoolExecutor);
        }

        final Executor generatorExecutor = Executors.newSingleThreadExecutor();
        final Executor sinkExecutor = Executors.newSingleThreadExecutor();

        final AtomicInteger atomicInteger = new AtomicInteger();
        final AtomicBoolean calculator = new AtomicBoolean(true);

        while (calculator.get()) {
            CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(() -> {
                final Optional<?> generationResult = job.getGenerator().generate();

                if (!generationResult.isPresent()) {
                    calculator.set(false);

                    throw new RuntimeException();
                }

                //Increment the shutdown counter
                atomicInteger.incrementAndGet();

                return generationResult.get();
            }, generatorExecutor);

            for (TaskDescriptor taskDescriptor : job.getTasks()) {
                completableFuture = completableFuture.thenApplyAsync((xyz) -> taskDescriptor.getTask().execute(xyz),
                        executorServiceHashMap.get(System.identityHashCode(taskDescriptor.getTask())));
            }

            final CompletableFuture<Void> closingFuture = completableFuture.thenAcceptAsync((asd) -> job.getSink().consume(asd), sinkExecutor);
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
