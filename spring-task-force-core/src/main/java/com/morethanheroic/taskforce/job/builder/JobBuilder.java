package com.morethanheroic.taskforce.job.builder;

import com.morethanheroic.taskforce.generator.Generator;
import com.morethanheroic.taskforce.job.builder.domain.JobContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JobBuilder {

    public static JobBuilder newBuilder() {
        return new JobBuilder();
    }

    public JobSecondPhaseBuilder generator(final Generator generator) {
        final JobContext jobContext = new JobContext();
        jobContext.setGenerator(generator);

        return new JobSecondPhaseBuilder(jobContext);
    }
}
