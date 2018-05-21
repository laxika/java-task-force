package com.morethanheroic.taskforce.job.builder;

import com.morethanheroic.taskforce.job.builder.domain.TaskStageJobContext;
import com.morethanheroic.taskforce.job.builder.domain.GeneratorStageJobContext;
import com.morethanheroic.taskforce.sink.Sink;
import com.morethanheroic.taskforce.task.Task;
import com.morethanheroic.taskforce.task.decorator.StatisticsDecoratorTask;
import com.morethanheroic.taskforce.task.domain.TaskContext;
import com.morethanheroic.taskforce.task.domain.TaskDescriptor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Initialize the second phase of the {@link com.morethanheroic.taskforce.job.Job} creation process. The users can add
 * new tasks to the processing pipeline and close the whole job with a {@link Sink}.
 */
@RequiredArgsConstructor
public class JobTaskPhaseBuilder<NEXT_INPUT> {

    private final GeneratorStageJobContext jobContext;
    private final List<TaskDescriptor<?, ?>> taskDescriptors = new ArrayList<>();

    /**
     * Adds a {@link Task} to the {@link com.morethanheroic.taskforce.job.Job}. The task will be added based on the
     * provided {@link TaskContext}. The task's name will be an unique randomly generated Id (UUID).
     *
     * @param task        the task to add
     * @param taskContext the context we want to add the task with
     * @param <OUTPUT>    the result type of the added task
     * @return this builder
     */
    @SuppressWarnings("unchecked")
    public <OUTPUT> JobTaskPhaseBuilder<OUTPUT> task(final Task<NEXT_INPUT, OUTPUT> task,
            final TaskContext taskContext) {
        final String taskName = UUID.randomUUID().toString();

        Task<NEXT_INPUT, OUTPUT> resultTask = task;
        if (taskContext.isStatisticsCollectionEnabled() || taskContext.isStatisticsReportingEnabled()) {
            resultTask = new StatisticsDecoratorTask<>(taskName, task, taskContext.isStatisticsReportingEnabled(),
                    taskContext.getStatisticsReportingRate());
        }

        taskDescriptors.add(
                TaskDescriptor.<NEXT_INPUT, OUTPUT>builder()
                        .parallelismLevel(taskContext.getParallelismLevel())
                        .maxQueueSize(taskContext.getMaxQueueSize())
                        .taskName(taskName)
                        .task(resultTask)
                        .build()
        );

        return (JobTaskPhaseBuilder<OUTPUT>) this;
    }

    /**
     * Adds a {@link Task} to the {@link com.morethanheroic.taskforce.job.Job}. The task will be added based on the
     * provided {@link TaskContext}.
     *
     * @param taskName    the name of the task
     * @param task        the task to add
     * @param taskContext the context we want to add the task with
     * @param <OUTPUT>    the result type of the added task
     * @return this builder
     */
    @SuppressWarnings("unchecked")
    public <OUTPUT> JobTaskPhaseBuilder<OUTPUT> task(final String taskName, final Task<NEXT_INPUT, OUTPUT> task,
            final TaskContext taskContext) {
        Task<NEXT_INPUT, OUTPUT> resultTask = task;

        if (taskContext.isStatisticsCollectionEnabled() || taskContext.isStatisticsReportingEnabled()) {
            resultTask = new StatisticsDecoratorTask<>(taskName, task, taskContext.isStatisticsReportingEnabled(),
                    taskContext.getStatisticsReportingRate());
        }

        taskDescriptors.add(
                TaskDescriptor.<NEXT_INPUT, OUTPUT>builder()
                        .parallelismLevel(taskContext.getParallelismLevel())
                        .maxQueueSize(taskContext.getMaxQueueSize())
                        .taskName(taskName)
                        .task(resultTask)
                        .build()
        );

        return (JobTaskPhaseBuilder<OUTPUT>) this;
    }

    /**
     * Adds a {@link Task} to the {@link com.morethanheroic.taskforce.job.Job}. The task will be run with parallelism
     * level 1 in it's own thread pool. The task's name will be an unique randomly generated Id (UUID).
     *
     * @param task     the task to add
     * @param <OUTPUT> the result type of the added task
     * @return this builder
     */
    @SuppressWarnings("unchecked")
    public <OUTPUT> JobTaskPhaseBuilder<OUTPUT> task(final Task<NEXT_INPUT, OUTPUT> task) {
        return asyncTask(UUID.randomUUID().toString(), task, 1);
    }

    /**
     * Adds a {@link Task} to the {@link com.morethanheroic.taskforce.job.Job}. The task will be run with parallelism
     * level 1 in it's own thread pool.
     *
     * @param taskName the name of the task
     * @param task     the task to add
     * @param <OUTPUT> the result type of the added task
     * @return this builder
     */
    @SuppressWarnings("unchecked")
    public <OUTPUT> JobTaskPhaseBuilder<OUTPUT> task(final String taskName, final Task<NEXT_INPUT, OUTPUT> task) {
        return asyncTask(taskName, task, 1);
    }

    /**
     * Adds a {@link Task} to the {@link com.morethanheroic.taskforce.job.Job}. The task will be run with the provided
     * level of parallelism in it's own thread pool. The task's name will be an unique randomly generated Id (UUID).
     *
     * @param task             the task to add
     * @param parallelismLevel the parallelism level of the task
     * @param <OUTPUT>         the result type of the added task
     * @return this builder
     */
    @SuppressWarnings("unchecked")
    public <OUTPUT> JobTaskPhaseBuilder<OUTPUT> asyncTask(final Task<NEXT_INPUT, OUTPUT> task,
            final int parallelismLevel) {
        return asyncTask(UUID.randomUUID().toString(), task, parallelismLevel, 10000);
    }

    /**
     * Adds a {@link Task} to the {@link com.morethanheroic.taskforce.job.Job}. The task will be run with the provided
     * level of parallelism in it's own thread pool.
     *
     * @param taskName         the name of the task
     * @param task             the task to add
     * @param parallelismLevel the parallelism level of the task
     * @param <OUTPUT>         the result type of the added task
     * @return this builder
     */
    @SuppressWarnings("unchecked")
    public <OUTPUT> JobTaskPhaseBuilder<OUTPUT> asyncTask(final String taskName, final Task<NEXT_INPUT, OUTPUT> task,
            final int parallelismLevel) {
        return asyncTask(taskName, task, parallelismLevel, 10000);
    }

    /**
     * Adds a {@link Task} to the {@link com.morethanheroic.taskforce.job.Job}. The task will be run with the provided
     * level of parallelism in it's own thread pool. The task's name will be an unique randomly generated Id (UUID).
     * The size of the queue that holds the data in the thread pool can also be set. When the generator or the task
     * above create more entries that allowed by the queue it will be blocked.
     *
     * @param task             the task to add
     * @param parallelismLevel the parallelism level of the task
     * @param maxQueueSize     the maximum queue size of the queue in the thread pool
     * @param <OUTPUT>         the result type of the added task
     * @return this builder
     */
    @SuppressWarnings("unchecked")
    public <OUTPUT> JobTaskPhaseBuilder<OUTPUT> asyncTask(final Task<NEXT_INPUT, OUTPUT> task,
            final int parallelismLevel, final int maxQueueSize) {
        taskDescriptors.add(
                TaskDescriptor.<NEXT_INPUT, OUTPUT>builder()
                        .parallelismLevel(parallelismLevel)
                        .maxQueueSize(maxQueueSize)
                        .taskName(UUID.randomUUID().toString())
                        .task(task)
                        .build()
        );

        return (JobTaskPhaseBuilder<OUTPUT>) this;
    }

    /**
     * Adds a {@link Task} to the {@link com.morethanheroic.taskforce.job.Job}. The task will be run with the provided
     * level of parallelism in it's own thread pool. The size of the queue that holds the data in the thread pool can
     * also be set. When the generator or the task above create more entries that allowed by the queue it will be
     * blocked.
     *
     * @param taskName         the name of the task
     * @param task             the task to add
     * @param parallelismLevel the parallelism level of the task
     * @param maxQueueSize     the maximum queue size of the queue in the thread pool
     * @param <OUTPUT>         the result type of the added task
     * @return this builder
     */
    @SuppressWarnings("unchecked")
    public <OUTPUT> JobTaskPhaseBuilder<OUTPUT> asyncTask(final String taskName, final Task<NEXT_INPUT, OUTPUT> task,
            final int parallelismLevel, final int maxQueueSize) {
        taskDescriptors.add(
                TaskDescriptor.<NEXT_INPUT, OUTPUT>builder()
                        .parallelismLevel(parallelismLevel)
                        .maxQueueSize(maxQueueSize)
                        .taskName(taskName)
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
                        .taskDescriptors(taskDescriptors)
                        .sink(sink)
                        .build()
        );
    }
}
