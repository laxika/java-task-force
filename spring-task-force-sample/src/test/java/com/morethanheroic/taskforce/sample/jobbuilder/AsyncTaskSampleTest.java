package com.morethanheroic.taskforce.sample.jobbuilder;

import com.morethanheroic.taskforce.job.JobExecutor;
import com.morethanheroic.taskforce.job.builder.JobBuilder;
import com.morethanheroic.taskforce.sample.domain.SampleGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class AsyncTaskSampleTest {

    private AsyncTaskSample underTest;

    @Before
    public void setup() {
        underTest = new AsyncTaskSample();
    }

    @Test
    public void integrationTestForAsyncSample() {
        final JobExecutor jobExecutor = new JobExecutor();

        jobExecutor.execute(underTest.buildJob());

        /*
        JobBuilder.newBuilder()
                .generator(new SampleGenerator())
                .task(Optional::of)
                .task((xyz) -> {


                    return Optional.of(Integer.valueOf(xyz));
                })
                .task(xyz->{

                })
                .sink((sink) -> {
        });
        */
    }
}
