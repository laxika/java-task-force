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

    private final TaskStageJobContext taskStageJobContext;

    private int threadCount;

    public JobFinalPhaseBuilder withThreadCount(final int threadCount) {
        this.threadCount = threadCount;

        return this;
    }

    public Job build() {
        final TaskExecutor taskExecutor = newExecutor(threadCount);
        final JobContext jobContext = newContext();

        return new SimpleJob(taskStageJobContext.getGenerator(), Collections.unmodifiableList(taskStageJobContext.getTaskDescriptors()),
                taskStageJobContext.getSink(), taskExecutor, jobContext);
    }

    private TaskExecutor newExecutor(final int threadCount) {
        if (threadCount == 0) {
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
