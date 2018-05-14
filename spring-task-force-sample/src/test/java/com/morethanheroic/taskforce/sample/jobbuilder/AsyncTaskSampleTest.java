package com.morethanheroic.taskforce.sample.jobbuilder;

import com.morethanheroic.taskforce.job.JobExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

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
    }
}
