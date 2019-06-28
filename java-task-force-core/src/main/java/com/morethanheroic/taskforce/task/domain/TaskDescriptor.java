package com.morethanheroic.taskforce.task.domain;

import com.morethanheroic.taskforce.task.Task;
import lombok.Builder;
import lombok.Getter;

/**
 * A task descriptor wraps a {@link Task} and define the runtime properties of the wrapped task.
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
     * The name of the task that this descriptor describe.
     */
    private final String taskName;
}
