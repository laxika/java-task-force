package com.morethanheroic.taskforce.sample.jobbuilder;

import com.morethanheroic.taskforce.job.Job;
import com.morethanheroic.taskforce.job.builder.JobBuilder;
import com.morethanheroic.taskforce.sample.domain.SampleGenerator;
import com.morethanheroic.taskforce.sample.domain.SampleSink;
import com.morethanheroic.taskforce.sample.domain.SlowSampleTask;
import com.morethanheroic.taskforce.task.domain.TaskContext;

public class AsyncTaskSample {

    public Job buildJob() {
        return JobBuilder.newBuilder()
                .generator(new SampleGenerator())
                .task("Test Task", new SlowSampleTask(),
                        TaskContext.builder()
                                .statisticsCollectionEnabled(true)
                                .statisticsReportingEnabled(true)
                                .statisticsReportingRate(5)
                                .build()
                )
                .sink(new SampleSink())
                .build();
    }
}
