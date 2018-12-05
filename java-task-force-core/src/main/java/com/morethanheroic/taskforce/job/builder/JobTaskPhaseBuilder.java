package com.morethanheroic.taskforce.job.builder;

import com.morethanheroic.taskforce.executor.pool.cache.ExecutorServiceFactory;
import com.morethanheroic.taskforce.executor.pool.cache.domain.ExecutorContext;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Initialize the second phase of the {@link com.morethanheroic.taskforce.job.Job} creation process. The users can add
 * new tasks to the processing pipeline and close the whole job with a {@link Sink}.
 */
@RequiredArgsConstructor
public class JobTaskPhaseBuilder<NEXT_INPUT> {

    private final GeneratorStageJobContext jobContext;
    private final List<TaskDescriptor<?, ?>> taskDescriptors = new ArrayList<>();
    //TODO: Move this factory out to the JobBuilder.builder(...) method so it can be configured by the users
    private final ExecutorServiceFactory executorServiceFactory = new ExecutorServiceFactory();


    /**
     * Adds a {@link Task} to the {@link com.morethanheroic.taskforce.job.Job}. The task will be run with parallelism
     * level 1 in it's own thread pool. The task's name will be an unique randomly generated Id ({@link UUID}).
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
     * Adds a {@link Task} to the {@link com.morethanheroic.taskforce.job.Job}. The task will be added based on the
     * provided {@link TaskContext}. The task's name will be an unique randomly generated Id ({@link UUID}).
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
        if (!taskContext.getExecutor().isPresent()
                && taskContext.getParallelismLevel() > taskContext.getMaxQueueSize()) {
            throw new JobCreationException("Invalid arguments! A task's parallelism level should be higher than the " +
                    "max queue size");
        }

        final ThreadPoolExecutor threadPoolExecutor = taskContext.getExecutor()
                .orElse(
                        executorServiceFactory.newExecutorService(
                                ExecutorContext.builder()
                                        .parallelismLevel(taskContext.getParallelismLevel())
                                        .maxQueueSize(taskContext.getMaxQueueSize())
                                        .build()
                        )
                );
        Task<NEXT_INPUT, OUTPUT> resultTask = task;
        if (taskContext.isStatisticsCollectionEnabled() || taskContext.isStatisticsReportingEnabled()) {
            resultTask = new StatisticsDecoratorTask<>(taskName, task, taskContext.isStatisticsReportingEnabled(),
                    taskContext.getStatisticsReportingRate(), threadPoolExecutor);
        }

        taskDescriptors.add(
                TaskDescriptor.<NEXT_INPUT, OUTPUT>builder()
                        .executor(threadPoolExecutor)
                        .taskName(taskName)
                        .task(resultTask)
                        .build()
        );

        return (JobTaskPhaseBuilder<OUTPUT>) this;
    }

    /**
     * Adds a {@link Task} to the {@link com.morethanheroic.taskforce.job.Job}. The task will be run with the provided
     * level of parallelism in it's own thread pool. The task's name will be an unique randomly generated Id
     * ({@link UUID}).
     *
     * @param task             the task to add
     * @param parallelismLevel the parallelism level of the task
     * @param <OUTPUT>         the result type of the added task
     * @return this builder
     */
    @SuppressWarnings("unchecked")
    public <OUTPUT> JobTaskPhaseBuilder<OUTPUT> asyncTask(final Task<NEXT_INPUT, OUTPUT> task,
            final int parallelismLevel) {
        final int maxQueueSize = parallelismLevel > 10000 ? parallelismLevel : 10000;

        return asyncTask(task, parallelismLevel, maxQueueSize);
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
        final int maxQueueSize = parallelismLevel > 10000 ? parallelismLevel : 10000;

        return asyncTask(taskName, task, parallelismLevel, maxQueueSize);
    }

    /**
     * Adds a {@link Task} to the {@link com.morethanheroic.taskforce.job.Job}. The task will be run with the provided
     * level of parallelism in it's own thread pool. The task's name will be an unique randomly generated Id
     * ({@link UUID}). The size of the queue that holds the data in the thread pool can also be set. When the generator
     * or the task above create more entries that allowed by the queue it will be blocked.
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
        final String taskName = UUID.randomUUID().toString();

        return asyncTask(taskName, task, parallelismLevel, maxQueueSize);
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
        final ThreadPoolExecutor threadPoolExecutor = executorServiceFactory.newExecutorService(
                ExecutorContext.builder()
                        .parallelismLevel(parallelismLevel)
                        .maxQueueSize(maxQueueSize)
                        .build()
        );

        return asyncTask(taskName, task, threadPoolExecutor);
    }

    /**
     * Adds a {@link Task} to the {@link com.morethanheroic.taskforce.job.Job}. The task will run on the provided
     * {@link ExecutorService}. The task's name will be an unique randomly generated Id ({@link UUID}).
     *
     * @param task            the task to add
     * @param executorService the executor service to run the task on
     * @param <OUTPUT>        the result type of the added task
     * @return this builder
     */
    @SuppressWarnings("unchecked")
    public <OUTPUT> JobTaskPhaseBuilder<OUTPUT> asyncTask(final Task<NEXT_INPUT, OUTPUT> task,
            final ThreadPoolExecutor executorService) {
        final String taskName = UUID.randomUUID().toString();

        return asyncTask(taskName, task, executorService);
    }

    /**
     * Adds a {@link Task} to the {@link com.morethanheroic.taskforce.job.Job}. The task will run on the provided
     * {@link ExecutorService}.
     *
     * @param taskName        the name of the task
     * @param task            the task to add
     * @param executorService the executor service to run the task on
     * @param <OUTPUT>        the result type of the added task
     * @return this builder
     */
    @SuppressWarnings("unchecked")
    public <OUTPUT> JobTaskPhaseBuilder<OUTPUT> asyncTask(final String taskName, final Task<NEXT_INPUT, OUTPUT> task,
            final ThreadPoolExecutor executorService) {
        return task(taskName, task,
                TaskContext.builder()
                        .executor(executorService)
                        .build()
        );
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
