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
public class JobTaskPhaseBuilder<NEXT_INPUT> {

    private final GeneratorStageJobContext jobContext;
    private final List<TaskDescriptor<?, ?>> tasks = new ArrayList<>();

    /**
     * Adds fully configured {@link Task} to the {@link com.morethanheroic.taskforce.job.Job}. The task is already
     * wrapped into a {@link TaskDescriptor}.
     *
     * @param taskDescriptor the task descriptor that contains the task
     * @return this builder
     */
    public <OUTPUT> JobTaskPhaseBuilder<OUTPUT> task(final TaskDescriptor<NEXT_INPUT, OUTPUT> taskDescriptor) {
        tasks.add(taskDescriptor);

        return (JobTaskPhaseBuilder<OUTPUT>) this;
    }

    /**
     * Adds a {@link Task} to the {@link com.morethanheroic.taskforce.job.Job}. The task will be run with parallelism
     * level 1 in it's own thread pool.
     *
     * @param task the task to add
     * @return this builder
     */
    public <OUTPUT> JobTaskPhaseBuilder<OUTPUT> task(final Task<NEXT_INPUT, OUTPUT> task) {
        asyncTask(task, 1);

        return (JobTaskPhaseBuilder<OUTPUT>) this;
    }

    /**
     * Adds a {@link Task} to the {@link com.morethanheroic.taskforce.job.Job}. The task will be run with the provided
     * level of parallelism in it's own thread pool.
     *
     * @param task             the task to add
     * @param parallelismLevel the parallelism level of the task
     * @return this builder
     */
    public <OUTPUT> JobTaskPhaseBuilder<OUTPUT> asyncTask(final Task<NEXT_INPUT, OUTPUT> task, final int parallelismLevel) {
        asyncTask(task, parallelismLevel, 10000);

        return (JobTaskPhaseBuilder<OUTPUT>) this;
    }

    /**
     * Adds a {@link Task} to the {@link com.morethanheroic.taskforce.job.Job}. The task will be run with the provided
     * level of parallelism in it's own thread pool. The size of the queue that holds the data in the thread pool can
     * also be set. When the generator or the task above create more entries that alloved by the queue it will be
     * blocked.
     *
     * @param task             the task to add
     * @param parallelismLevel the parallelism level of the task
     * @param maxQueueSize     the maximum queue size of the queue in the thread pool
     * @return this builder
     */
    public <OUTPUT> JobTaskPhaseBuilder<OUTPUT> asyncTask(final Task<NEXT_INPUT, OUTPUT> task, final int parallelismLevel, final int maxQueueSize) {
        tasks.add(
                TaskDescriptor.<NEXT_INPUT, OUTPUT>builder()
                        .parallelismLevel(parallelismLevel)
                        .maxQueueSize(maxQueueSize)
                        .task(task)
                        .build()
        );

        return (JobTaskPhaseBuilder<OUTPUT>) this;
    }

    /**
     * Adds a {@link Sink} that will terminate the {@link com.morethanheroic.taskforce.job.Job}.
     *
     * @param sink the sink to add
     * @return the builder for the third step of job creation
     */
    public JobFinalPhaseBuilder sink(final Sink<NEXT_INPUT> sink) {
        return new JobFinalPhaseBuilder(
                TaskStageJobContext.builder()
                        .generator(jobContext.getGenerator())
                        .tasks(tasks)
                        .sink(sink)
                        .build()
        );
    }
}
