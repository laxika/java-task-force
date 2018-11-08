package com.morethanheroic.taskforce.sample.jobbuilder;

import com.morethanheroic.taskforce.job.Job;
import com.morethanheroic.taskforce.job.builder.JobBuilder;
import com.morethanheroic.taskforce.sample.domain.SampleGenerator;
import com.morethanheroic.taskforce.sample.domain.SampleSink;
import com.morethanheroic.taskforce.sample.domain.SampleTask;

public class JobBuilderSample {

    public Job buildJob() {
        return JobBuilder.newBuilder()
                .generator(new SampleGenerator())
                .task("First Task", new SampleTask())
                .task("Test Task",new SampleTask())
                .sink(new SampleSink())
                .build();
    }
}
