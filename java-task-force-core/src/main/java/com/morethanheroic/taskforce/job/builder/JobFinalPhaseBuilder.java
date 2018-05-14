package com.morethanheroic.taskforce.job.builder;

import com.morethanheroic.taskforce.job.Job;
import com.morethanheroic.taskforce.job.SimpleJob;

import java.util.Collections;

import com.morethanheroic.taskforce.job.builder.domain.TaskStageJobContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JobFinalPhaseBuilder {

  private final TaskStageJobContext jobContext;

  public Job build() {
    return new SimpleJob(jobContext.getGenerator(),
        Collections.unmodifiableList(jobContext.getTasks()), jobContext.getSink());
  }
}
