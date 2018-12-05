package com.morethanheroic.taskforce.executor.pool.cache;

import com.morethanheroic.taskforce.task.domain.TaskDescriptor;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * This class is responsible for creating pre-initialized {@link ThreadPoolCache} instances based on the provided
 * {@link TaskDescriptor} data.
 */
@RequiredArgsConstructor
public class ThreadPoolCacheFactory {

    private final ExecutorServiceFactory executorServiceFactory;

    /**
     * Creates a new {@link ThreadPoolCache} that will contain the {@link ExecutorService}s for the task provided.
     *
     * @param taskDescriptors the descriptions of the tasks that needs a cache
     * @return the cache that holds the pre-initialized executors for the threads
     */
    public ThreadPoolCache newThreadPoolCache(final List<TaskDescriptor<?, ?>> taskDescriptors) {
        final Map<Integer, ExecutorService> executorServiceHashMap = new HashMap<>();

        for (final TaskDescriptor<?, ?> taskDescriptor : taskDescriptors) {
            final ExecutorService executorService = taskDescriptor.getExecutor()
                    .orElseGet(() -> executorServiceFactory.newExecutorService(taskDescriptor));

            executorServiceHashMap.put(System.identityHashCode(taskDescriptor.getTask()), executorService);
        }

        return new ThreadPoolCache(Collections.unmodifiableMap(executorServiceHashMap));
    }
}
