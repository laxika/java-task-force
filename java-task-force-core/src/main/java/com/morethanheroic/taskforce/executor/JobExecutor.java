package com.morethanheroic.taskforce.executor;

import com.morethanheroic.taskforce.executor.context.JobContext;
import com.morethanheroic.taskforce.executor.task.TaskExecutor;
import com.morethanheroic.taskforce.job.Job;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * This class is responsible for the running of {@link Job} instances.
 */
@Slf4j
public class JobExecutor {

    /**
     * Execute a job.
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

    private void processEntry(final Job job) {
        final TaskExecutor taskExecutor = job.getTaskExecutor();
        final JobContext jobContext = job.getJobContext();

        final Optional<?> generationResult = job.getGenerator().generate();

        if (!generationResult.isPresent()) {
            job.getGenerator().close();

            jobContext.setLastItemReached();
        } else {
            taskExecutor.submitTasks(generationResult.get(), job.getTaskDescriptors(), job.getSink());
        }
    }
}
