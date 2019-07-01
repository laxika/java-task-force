package com.morethanheroic.taskforce.generator;

import java.util.Optional;

/**
 * The generator is responsible for providing entries to process to the {@link com.morethanheroic.taskforce.task.Task}s.
 */
public interface Generator<RESULT> {

    /**
     * Provides new objects to be processed by the registered {@link com.morethanheroic.taskforce.task.Task}s is a
     * {@link com.morethanheroic.taskforce.job.Job}.
     * <p>
     * The generator is called for new items to process until an empty {@link Optional} is returned.
     *
     * @return an items to be processed by the registered tasks
     */
    Optional<RESULT> generate();

    /**
     * This method is called before the first element is created by the generator. The control is not going to
     * be returned to the executor of the job until this method is successfully finish the execution.
     */
    default void open() {
    }

    /**
     * This method is called after the last element is successfully created by the generator. The control is not going to
     * be returned to the executor of the job until this method is successfully finish the execution.
     */
    default void close() {
    }
}
