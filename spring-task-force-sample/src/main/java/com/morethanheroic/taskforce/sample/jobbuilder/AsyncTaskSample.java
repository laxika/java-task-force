package com.morethanheroic.taskforce.sample.jobbuilder;

import com.morethanheroic.taskforce.job.Job;
import com.morethanheroic.taskforce.job.builder.JobBuilder;
import com.morethanheroic.taskforce.sample.domain.SampleGenerator;
import com.morethanheroic.taskforce.sample.domain.SampleSink;
import com.morethanheroic.taskforce.sample.domain.SlowSampleTask;
import com.morethanheroic.taskforce.task.AsyncTask;

public class AsyncTaskSample {

    public Job buildJob() {
        return JobBuilder.newBuilder()
                .generator(new SampleGenerator())
                .task(new AsyncTask<>(10, new SlowSampleTask()))
                .sink(new SampleSink())
                .build();
    }
}
