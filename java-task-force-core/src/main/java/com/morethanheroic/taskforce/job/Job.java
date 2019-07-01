package com.morethanheroic.taskforce.job;

import com.morethanheroic.taskforce.executor.context.JobContext;
import com.morethanheroic.taskforce.executor.task.TaskExecutor;
import com.morethanheroic.taskforce.generator.Generator;
import com.morethanheroic.taskforce.sink.Sink;
import com.morethanheroic.taskforce.task.domain.TaskDescriptor;

import java.util.List;

/**
 * An unit of work that contains of a {@link Generator} that create the work entities, any number of
 * {@link com.morethanheroic.taskforce.task.Task}s that process the generated items, and a {@link Sink} that
 * consumes the end result of the processing.
 */
public abstract class Job {

    /**
     * The generator used to create new entities for processing.
     *
     * @return the generator for the job
     */
    public abstract Generator<?> getGenerator();

    /**
     * The sink used to consume the result entities of this job.
     *
     * @return the sink for the job
     */
    public abstract Sink<?> getSink();

    /**
     * The tasks that should be run between the {@link Generator} and the {@link Sink} in the job.
     *
     * @return the tasks for the job
     */
    public abstract List<TaskDescriptor<?, ?>> getTaskDescriptors();

    /**
     * The executor used to run the tasks and the sink.
     *
     * @return the executor for the job
     */
    public abstract TaskExecutor getTaskExecutor();

    /**
     * The context used for the job. Contains status information etc.
     *
     * @return the context for the job
     */
    public abstract JobContext getJobContext();

    /**
     * Returns true if the job is finished.
     *
     * @return true if the job is finished
     */
    public boolean isFinished() {
        return getJobContext().isLastItemReached();
    }

    /**
     * Initialize the job before processing. Should be invoked before any processing happening.
     */
    public void initialize() {
        getGenerator().open();
        getSink().open();
    }

    /**
     * Cleanup the job after processing. Should be invoked after any processing happening.
     */
    public void cleanup() {
        getTaskExecutor().waitUntilFinished();
        getTaskExecutor().shutdown();

        getGenerator().close();
        getSink().close();
    }
}
