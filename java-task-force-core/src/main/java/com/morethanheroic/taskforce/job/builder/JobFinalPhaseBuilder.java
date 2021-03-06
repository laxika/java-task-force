package com.morethanheroic.taskforce.job.builder;

import com.morethanheroic.taskforce.executor.context.JobContext;
import com.morethanheroic.taskforce.executor.task.TaskExecutor;
import com.morethanheroic.taskforce.job.Job;
import com.morethanheroic.taskforce.job.SimpleJob;
import com.morethanheroic.taskforce.job.builder.domain.TaskStageJobContext;
import lombok.RequiredArgsConstructor;

import java.util.Collections;

/**
 * This builder is the ending point of a job building chain. It is responsible for closing the building and actually
 * creating the final job.
 */
@RequiredArgsConstructor
public class JobFinalPhaseBuilder {

    private static final int DEFAULT_THREAD_COUNT = 0;

    private final TaskStageJobContext taskStageJobContext;

    private int threadCount;

    /**
     * The number of threads used to run the job in parallel. By default it's the amount of processor cores the host
     * system has.
     *
     * @param threadCount the number of threads to run the job on
     * @return this builder instance
     */
    public JobFinalPhaseBuilder withThreadCount(final int threadCount) {
        if (threadCount < 0) {
            throw new IllegalArgumentException("The threadCount should not be negative!");
        }

        this.threadCount = threadCount;

        return this;
    }


    /**
     * Run the job with single thread
     *
     * @return this builder instance
     */
    public JobFinalPhaseBuilder withSingleThread() {
        return withThreadCount(1);
    }


    /**
     * Build the job.
     *
     * @return the built job
     */
    public Job build() {
        final TaskExecutor taskExecutor = newExecutor(threadCount);
        final JobContext jobContext = newContext();

        return new SimpleJob(taskStageJobContext.getGenerator(), Collections.unmodifiableList(
                taskStageJobContext.getTaskDescriptors()), taskStageJobContext.getSink(), taskExecutor, jobContext);
    }

    private TaskExecutor newExecutor(final int threadCount) {
        if (threadCount == DEFAULT_THREAD_COUNT) {
            return TaskExecutor.builder()
                    .threadCount(Runtime.getRuntime().availableProcessors())
                    .build();
        } else {
            return TaskExecutor.builder()
                    .threadCount(threadCount)
                    .build();
        }
    }

    private JobContext newContext() {
        return JobContext.builder().build();
    }
}
