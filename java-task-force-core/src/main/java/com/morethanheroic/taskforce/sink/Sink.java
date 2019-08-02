package com.morethanheroic.taskforce.sink;

/**
 * Sink is the closing element of a {@link com.morethanheroic.taskforce.job.Job}. It acquire the previously handled
 * elements from the {@link com.morethanheroic.taskforce.task.Task}s in the job.
 *
 * @param <INPUT> the type of the handled element
 */
@FunctionalInterface
public interface Sink<INPUT> {

    /**
     * Consume an element. This method is called every time after the final
     * {@link com.morethanheroic.taskforce.task.Task} in the {@link com.morethanheroic.taskforce.job.Job} finish the
     * processing of the element.
     *
     * @param input the end result of the tasks in the job
     */
    void consume(INPUT input);

    /**
     * This method is called before the first element is created by the generator. The control is not going to
     * be returned to the executor of the job until this method is successfully finish the execution.
     */
    default void open() {
    }

    /**
     * This method is called after the last element is successfully processed by the job. The control is not going to
     * be returned to the executor of the job until this method is successfully finish the execution.
     */
    default void close() {
    }
}
