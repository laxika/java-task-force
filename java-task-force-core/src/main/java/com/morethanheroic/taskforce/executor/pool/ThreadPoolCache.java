package com.morethanheroic.taskforce.executor.pool;

import com.morethanheroic.taskforce.task.Task;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Contains the {@link Executor} instances required for running the {@link Task}s.
 */
@RequiredArgsConstructor
public class ThreadPoolCache {

    private final Map<Integer, Executor> executorServiceMap;

    /**
     * Returns the {@link Executor} that belongs to the provided task.
     *
     * @param task the task to get the executor for
     * @return the executor of the task
     */
    public Executor getExecutor(final Task<?, ?> task) {
        final int identity = System.identityHashCode(task);

        if (!executorServiceMap.containsKey(identity)) {
            throw new RuntimeException("Missing ExecutorService for Task " + task + "!");
        }

        return executorServiceMap.get(identity);
    }
}
