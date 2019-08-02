package com.morethanheroic.taskforce.executor;

import com.morethanheroic.taskforce.executor.context.JobContext;
import com.morethanheroic.taskforce.executor.task.TaskExecutor;
import com.morethanheroic.taskforce.job.Job;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This class is responsible for the running of {@link Job} instances.
 */
@Slf4j
public class JobExecutor {

    /**
     * Execute a job. The invoking thread will be blocked until the job execution is finished.
     *
     * @param job the job to execute
     */
    public void execute(final Job job) {
        job.initialize();

        while (!job.isFinished()) {
            try {
                processEntry(job);
            } catch (final Exception e) {
                throw new RuntimeException("Error while generating element!", e);
            }
        }

        job.cleanup();
    }

    /**
     * Execute a job in an asynchronous way. The running of the task will be done in a background thread.
     *
     * @param job the job to execute
     * @return the future that represents the result of an asynchronous computation
     */
    public Future<?> executeAsync(final Job job) {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();

        return executorService.submit(() -> execute(job));
    }

    private void processEntry(final Job job) {
        final TaskExecutor taskExecutor = job.getTaskExecutor();
        final JobContext jobContext = job.getJobContext();

        final Optional<?> generationResult = job.getGenerator().generate();

        if (!generationResult.isPresent()) {
            jobContext.setLastItemReached();
        } else {
            taskExecutor.submitTasks(generationResult.get(), job.getTaskDescriptors(), job.getSink());
        }
    }
}
