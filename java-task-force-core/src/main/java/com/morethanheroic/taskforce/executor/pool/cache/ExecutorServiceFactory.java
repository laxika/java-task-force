package com.morethanheroic.taskforce.executor.pool.cache;

import com.morethanheroic.taskforce.executor.pool.BlockingThreadPoolExecutor;
import com.morethanheroic.taskforce.task.domain.TaskDescriptor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A factory that build special {@link ExecutorService} instances for running
 * {@link com.morethanheroic.taskforce.task.Task}s on them.
 */
public class ExecutorServiceFactory {

    private static final int DEFAULT_KEEP_ALIVE_TIME = 0;

    /**
     * Creates a special {@link ExecutorService} instance. The instance will be created for the provided
     * {@link TaskDescriptor}.
     *
     * @param taskDescriptor the task's descriptor to create the executor service for
     * @return the created executor service
     */
    public ExecutorService newExecutorService(final TaskDescriptor<?, ?> taskDescriptor) {
        return new BlockingThreadPoolExecutor(
                taskDescriptor.getParallelismLevel(),
                taskDescriptor.getParallelismLevel(),
                DEFAULT_KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS,
                taskDescriptor.getMaxQueueSize(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
