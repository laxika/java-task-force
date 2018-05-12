package com.morethanheroic.taskforce.sample.jobbuilder;

import com.morethanheroic.taskforce.job.Job;
import com.morethanheroic.taskforce.job.builder.JobBuilder;
import com.morethanheroic.taskforce.sample.domain.SampleGenerator;
import com.morethanheroic.taskforce.sample.domain.SampleSink;
import com.morethanheroic.taskforce.sample.domain.SlowSampleTask;

public class AsyncTaskSample {

    public Job buildJob() {
        return JobBuilder.newBuilder()
                .generator(new SampleGenerator())
                .asyncTask(new SlowSampleTask(), 10)
                .sink(new SampleSink())
                .build();
    }
}
