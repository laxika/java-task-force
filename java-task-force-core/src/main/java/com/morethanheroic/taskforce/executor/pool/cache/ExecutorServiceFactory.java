package com.morethanheroic.taskforce.executor.pool.cache;

import com.morethanheroic.taskforce.executor.pool.BlockingThreadPoolExecutor;
import com.morethanheroic.taskforce.executor.pool.cache.domain.ExecutorContext;
import com.morethanheroic.taskforce.task.domain.TaskDescriptor;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A factory that build special {@link ThreadPoolExecutor} instances for running
 * {@link com.morethanheroic.taskforce.task.Task}s on them.
 */
public class ExecutorServiceFactory {

    private static final int DEFAULT_KEEP_ALIVE_TIME = 0;

    /**
     * Creates a special {@link ThreadPoolExecutor} instance. The instance will be created for the provided
     * {@link TaskDescriptor}.
     *
     * @param executorContext the task's executor context to create the executor service for
     * @return the created executor service
     */
    public ThreadPoolExecutor newExecutorService(final ExecutorContext executorContext) {
        return new BlockingThreadPoolExecutor(
                executorContext.getParallelismLevel(),
                executorContext.getParallelismLevel(),
                DEFAULT_KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS,
                executorContext.getMaxQueueSize(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
