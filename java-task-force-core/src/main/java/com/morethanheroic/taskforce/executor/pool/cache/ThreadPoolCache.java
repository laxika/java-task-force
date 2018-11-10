package com.morethanheroic.taskforce.executor.pool.cache;

import com.morethanheroic.taskforce.task.Task;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Contains the {@link ExecutorService} instances required for running the {@link Task}s.
 */
@RequiredArgsConstructor
public class ThreadPoolCache {

    private final Map<Integer, ExecutorService> executorServiceMap;

    /**
     * Returns the {@link ExecutorService} that belongs to the provided task.
     *
     * @param task the task to get the executor for
     * @return the executor of the task
     */
    public ExecutorService getExecutor(final Task<?, ?> task) {
        final int identity = System.identityHashCode(task);

        if (!executorServiceMap.containsKey(identity)) {
            throw new RuntimeException("Missing ExecutorService for Task " + task + "!");
        }

        return executorServiceMap.get(identity);
    }

    /**
     * Shutdown all of the {@link ExecutorService}s in the cache.
     */
    public void shutdown() {
        executorServiceMap.values().forEach(ExecutorService::shutdown);
    }
}
