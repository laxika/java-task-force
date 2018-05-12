package com.morethanheroic.taskforce.job.builder;

import com.morethanheroic.taskforce.job.builder.domain.TaskStageJobContext;
import com.morethanheroic.taskforce.job.builder.domain.GeneratorStageJobContext;
import com.morethanheroic.taskforce.sink.Sink;
import com.morethanheroic.taskforce.task.Task;
import com.morethanheroic.taskforce.task.TaskDescriptor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Initialize the second phase of the {@link com.morethanheroic.taskforce.job.Job} creation process. The users can add
 * new tasks to the processing pipeline and close the whole job with a {@link Sink}.
 */
@RequiredArgsConstructor
public class JobTaskPhaseBuilder {

    private final GeneratorStageJobContext jobContext;
    private final List<TaskDescriptor> tasks = new ArrayList<>();

    /**
     * Adds a {@link Task} to the {@link com.morethanheroic.taskforce.job.Job}. The task will be run with parallelism
     * level 1 in it's own thread pool.
     *
     * @param task the task to add
     * @return this builder
     */
    public JobTaskPhaseBuilder task(final Task task) {
        tasks.add(
                TaskDescriptor.builder()
                        .parallelismLevel(1)
                        .task(task)
                        .build()
        );

        return this;
    }

    /**
     * Adds a {@link Task} to the {@link com.morethanheroic.taskforce.job.Job}. The task will be run with the provided
     * level of parallelism in it's own thread pool.
     *
     * @param task             the task to add
     * @param parallelismLevel the parallelism level of the task
     * @return this builder
     */
    public JobTaskPhaseBuilder asyncTask(final Task task, final int parallelismLevel) {
        tasks.add(
                TaskDescriptor.builder()
                        .parallelismLevel(parallelismLevel)
                        .task(task)
                        .build()
        );

        return this;
    }

    /**
     * Adds a {@link Sink} that will terminate the {@link com.morethanheroic.taskforce.job.Job}.
     *
     * @param sink the sink to add
     * @return the builder for the third step of job creation
     */
    public JobFinalPhaseBuilder sink(final Sink sink) {
        return new JobFinalPhaseBuilder(
                TaskStageJobContext.builder()
                        .generator(jobContext.getGenerator())
                        .tasks(tasks)
                        .sink(sink)
                        .build()
        );
    }
}
