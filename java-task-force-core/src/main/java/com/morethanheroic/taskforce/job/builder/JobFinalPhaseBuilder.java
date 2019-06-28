package com.morethanheroic.taskforce.job.builder;

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

    private final TaskStageJobContext jobContext;

    public Job build() {
        return new SimpleJob(jobContext.getGenerator(), Collections.unmodifiableList(jobContext.getTaskDescriptors()),
                jobContext.getSink());
    }
}
