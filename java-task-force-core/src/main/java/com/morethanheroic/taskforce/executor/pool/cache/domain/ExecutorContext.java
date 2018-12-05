package com.morethanheroic.taskforce.executor.pool.cache.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExecutorContext {

    /**
     * How many threads do we want to use when running the wrapped task.
     */
    private final int parallelismLevel;

    /**
     * How many items do we want in the queue that sits before the wrapped task. Setting this value too high can cause
     * {@link OutOfMemoryError}s. It should be 0 or a higher than 0 positive integer.
     */
    private final int maxQueueSize;
}
