package com.morethanheroic.taskforce.job;

import com.morethanheroic.taskforce.executor.context.JobContext;
import com.morethanheroic.taskforce.executor.task.TaskExecutor;
import com.morethanheroic.taskforce.generator.Generator;
import com.morethanheroic.taskforce.sink.Sink;
import com.morethanheroic.taskforce.task.domain.TaskDescriptor;

import java.util.List;

public abstract class Job {

    public abstract Generator<?> getGenerator();

    public abstract Sink<?> getSink();

    public abstract List<TaskDescriptor<?, ?>> getTaskDescriptors();

    public abstract TaskExecutor getTaskExecutor();

    public abstract JobContext getJobContext();

    public boolean isFinished() {
        return getJobContext().isLastItemReached();
    }

    public void initialize() {
        getGenerator().open();
    }

    public void cleanup() {
        getTaskExecutor().waitUntilFinished();
        getTaskExecutor().shutdown();

        getSink().close();
    }
}
