package com.morethanheroic.taskforce.executor;

import com.morethanheroic.taskforce.job.Job;
import com.morethanheroic.taskforce.task.TaskDescriptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

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

        final AtomicBoolean calculator = new AtomicBoolean(true);

        while (calculator.get()) {
            CompletableFuture<Optional<?>> completableFuture = CompletableFuture.supplyAsync(() -> {
                final Optional<?> generationResult = job.getGenerator().generate();

                if (!generationResult.isPresent()) {
                    calculator.set(false);
                }

                return generationResult;
            }, generatorExecutor);

            for (TaskDescriptor taskDescriptor : job.getTasks()) {
                completableFuture = completableFuture.thenApplyAsync(
                        (workingItem) -> {
                            if (!workingItem.isPresent()) {
                                return Optional.empty();
                            }

                            return taskDescriptor.getTask().execute( workingItem.get());
                        },
                        executorServiceHashMap.get(System.identityHashCode(taskDescriptor.getTask())));
            }

            completableFuture.thenAcceptAsync((asd) -> {
                if (asd.isPresent()) {
                    job.getSink().consume(asd);
                }
            }, sinkExecutor);
        }
    }
}
