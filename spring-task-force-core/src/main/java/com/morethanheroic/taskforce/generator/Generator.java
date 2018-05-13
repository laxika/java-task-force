package com.morethanheroic.taskforce.generator;

import java.util.Optional;

/**
 * The generator is responsible for providing entries to process to the {@link com.morethanheroic.taskforce.task.Task}s.
 */
public interface Generator {

    /**
     * Provides new objects to be processed by the registered {@link com.morethanheroic.taskforce.task.Task}s is a
     * {@link com.morethanheroic.taskforce.job.Job}.
     * <p>
     * The generator is called for new items to process until an empty {@link Optional} is returned.
     *
     * @return an items to be processed by the registered tasks
     */
    Optional<?> generate();
}
