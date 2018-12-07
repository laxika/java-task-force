package com.morethanheroic.taskforce.executor.pool;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Provides generic {@link ThreadPoolExecutor}s to run various {@link com.morethanheroic.taskforce.task.Task}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskExecutor {

    /**
     * The {@link ThreadPoolExecutor} provided by this method is a good fit for io-heavy workloads. The thread count in
     * this executor is non-fixed and new threads are automatically created/shut down based on the work available.
     * Because of this, running compute-heavy workloads in this {@link ThreadPoolExecutor} is not recommended.
     * <p>
     * The maximum amount of items in the work queue is 1000. After that the executor will block the threads
     * that try to submit new tasks to the executor. This is necessary to avoid {@link OutOfMemoryError}s if the
     * providers can generate items faster than the executors can finish the work available.
     *
     * @return an executor service fit for io-heavy workloads
     */
    public static ThreadPoolExecutor io() {
        return new BlockingThreadPoolExecutor(0, Integer.MAX_VALUE, 60,
                TimeUnit.SECONDS, 1000, Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * The {@link ThreadPoolExecutor} provided by this method is a good fit for compute-heavy workloads. The thread count
     * in this executor is equals to the amount of the processor cores available to the JVM. Because of this, running
     * io-heavy workloads in this {@link ThreadPoolExecutor} is not recommended.
     * <p>
     * The maximum amount of items in the work queue is 10000. After that the executor will block the threads
     * that try to submit new tasks to the executor. This is necessary to avoid {@link OutOfMemoryError}s if the
     * providers can generate items faster than the executors can finish the work available.
     *
     * @return an executor service fit for compute-heavy workloads
     */
    public static ThreadPoolExecutor compute() {
        return new BlockingThreadPoolExecutor(0, Runtime.getRuntime().availableProcessors(), 0,
                TimeUnit.SECONDS, 10000, Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    }
}
