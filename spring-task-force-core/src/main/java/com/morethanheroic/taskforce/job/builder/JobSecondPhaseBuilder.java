package com.morethanheroic.taskforce.job.builder;

import com.morethanheroic.taskforce.job.builder.domain.JobContext;
import com.morethanheroic.taskforce.sink.Sink;
import com.morethanheroic.taskforce.task.Task;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JobSecondPhaseBuilder {

    private final JobContext jobContext;

    public JobSecondPhaseBuilder task(final Task task) {
        jobContext.getTasks().add(task);

        return this;
    }

    public JobFinalPhaseBuilder sink(final Sink sink) {
        jobContext.setSink(sink);

        return new JobFinalPhaseBuilder(jobContext);
    }
}
