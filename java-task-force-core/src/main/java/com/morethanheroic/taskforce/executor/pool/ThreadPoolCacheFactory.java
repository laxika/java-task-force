package com.morethanheroic.taskforce.executor.pool;

import com.morethanheroic.taskforce.task.domain.TaskDescriptor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * This class is responsible for creating pre-initialized {@link ThreadPoolCache} instances based on the provided
 * {@link TaskDescriptor} data.
 */
public class ThreadPoolCacheFactory {

    /**
     * Creates a new {@link ThreadPoolCache} that will contain the {@link Executor}s for the task provided.
     *
     * @param taskDescriptors the descriptions of the tasks that needs a cache
     * @return the cache that holds the pre-initialized executors for the threads
     */
    public ThreadPoolCache newThreadPoolCache(final List<TaskDescriptor<?, ?>> taskDescriptors) {
        final Map<Integer, ExecutorService> executorServiceHashMap = new HashMap<>();

        for (final TaskDescriptor<?, ?> taskDescriptor : taskDescriptors) {
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

        return new ThreadPoolCache(Collections.unmodifiableMap(executorServiceHashMap));
    }
}
