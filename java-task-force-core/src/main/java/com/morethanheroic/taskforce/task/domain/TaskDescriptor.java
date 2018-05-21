package com.morethanheroic.taskforce.task.domain;

import com.morethanheroic.taskforce.task.Task;
import lombok.Builder;
import lombok.Getter;

/**
 * A task descriptor wraps a {@link Task} and define all of the runtime properties (like parallelism level etc.) of the
 * wrapped task.
 *
 * @param <INPUT>  the input type of the wrapped task
 * @param <OUTPUT> the output type of the wrapped task
 */
@Getter
@Builder
public class TaskDescriptor<INPUT, OUTPUT> {

    /**
     * the task that we are wrapping into this descriptor.
     */
    private final Task<INPUT, OUTPUT> task;

    /**
     * How many threads do we want to use when running the wrapped task.
     */
    private final int parallelismLevel;

    /**
     * How many items do we want in the queue that sits before the wrapped task. Setting this value too high can cause
     * {@link OutOfMemoryError}s. It should be 0 or a higher than 0 positive integer.
     */
    private final int maxQueueSize;

    /**
     * The name of the task that this descriptor describe.
     */
    private final String taskName;
}
