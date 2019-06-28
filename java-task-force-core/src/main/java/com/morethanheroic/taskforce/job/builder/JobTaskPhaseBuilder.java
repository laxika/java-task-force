package com.morethanheroic.taskforce.job.builder;

import com.morethanheroic.taskforce.job.builder.domain.GeneratorStageJobContext;
import com.morethanheroic.taskforce.job.builder.domain.TaskStageJobContext;
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
     * Adds a {@link Task} to the {@link com.morethanheroic.taskforce.job.Job}. The task's name will be an unique
     * randomly generated Id ({@link UUID}).
     *
     * @param task     the task to add
     * @param <OUTPUT> the result type of the added task
     * @return this builder
     */
    public <OUTPUT> JobTaskPhaseBuilder<OUTPUT> task(final Task<NEXT_INPUT, OUTPUT> task) {
        return task(task, TaskContext.builder().build());
    }

    /**
     * Adds a {@link Task} to the {@link com.morethanheroic.taskforce.job.Job}.
     *
     * @param taskName the name of the task
     * @param task     the task to add
     * @param <OUTPUT> the result type of the added task
     * @return this builder
     */
    public <OUTPUT> JobTaskPhaseBuilder<OUTPUT> task(final String taskName, final Task<NEXT_INPUT, OUTPUT> task) {
        return task(taskName, task, TaskContext.builder().build());
    }

    /**
     * Adds a {@link Task} to the {@link com.morethanheroic.taskforce.job.Job}. The task will be added based on the
     * provided {@link TaskContext}. The task's name will be an unique randomly generated Id ({@link UUID}).
     *
     * @param task        the task to add
     * @param taskContext the context we want to add the task with
     * @param <OUTPUT>    the result type of the added task
     * @return this builder
     */
    public <OUTPUT> JobTaskPhaseBuilder<OUTPUT> task(final Task<NEXT_INPUT, OUTPUT> task,
            final TaskContext taskContext) {
        final String taskName = UUID.randomUUID().toString();

        return task(taskName, task, taskContext);
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
                        .taskName(taskName)
                        .task(resultTask)
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
