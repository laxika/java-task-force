package com.morethanheroic.taskforce.job.builder;

import com.morethanheroic.taskforce.job.Job;
import com.morethanheroic.taskforce.job.SimpleJob;
import com.morethanheroic.taskforce.job.builder.domain.JobContext;
import java.util.Collections;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JobFinalPhaseBuilder {

  private final JobContext jobContext;

  public Job build() {
    return new SimpleJob(jobContext.getGenerator(),
        Collections.unmodifiableList(jobContext.getTasks()), jobContext.getSink());
  }
}
