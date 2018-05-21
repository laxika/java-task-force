package com.morethanheroic.taskforce.job.builder;

import com.morethanheroic.taskforce.job.Job;
import com.morethanheroic.taskforce.job.SimpleJob;

import java.util.Collections;
import java.util.List;

import com.morethanheroic.taskforce.job.builder.domain.TaskStageJobContext;
import com.morethanheroic.taskforce.task.Task;
import lombok.RequiredArgsConstructor;

/**
 * This builder is the ending point of a job building chain. It is responsible for closing the building and actually
 * creating the final job.
 */
@RequiredArgsConstructor
public class JobFinalPhaseBuilder {

    private final TaskStageJobContext jobContext;

    public Job build() {
        return new SimpleJob(jobContext.getGenerator(), Collections.unmodifiableList(jobContext.getTaskDescriptors()),
                jobContext.getSink());
    }
}
