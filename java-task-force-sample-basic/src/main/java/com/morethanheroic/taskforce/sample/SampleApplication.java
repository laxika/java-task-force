package com.morethanheroic.taskforce.sample;

import com.morethanheroic.taskforce.executor.JobExecutor;
import com.morethanheroic.taskforce.job.Job;
import com.morethanheroic.taskforce.job.builder.JobBuilder;
import com.morethanheroic.taskforce.sample.domain.SampleGenerator;
import com.morethanheroic.taskforce.sample.domain.SampleSink;
import com.morethanheroic.taskforce.sample.domain.SampleTask;
import com.morethanheroic.taskforce.sample.domain.SlowSampleTask;
import com.morethanheroic.taskforce.task.domain.TaskContext;

public class SampleApplication {

    public static void main(final String... args) {
        final Job job = JobBuilder.newBuilder()
                .generator(new SampleGenerator())
                .task("task-one", new SampleTask())
                .task("task-two", new SlowSampleTask(),
                        TaskContext.builder()
                                .statisticsCollectionEnabled(true)
                                .statisticsReportingEnabled(true)
                                .statisticsReportingRate(5)
                                .build()
                )
                .sink(new SampleSink())
                .build();

        final JobExecutor jobExecutor = new JobExecutor();
        jobExecutor.execute(job);
    }
}
