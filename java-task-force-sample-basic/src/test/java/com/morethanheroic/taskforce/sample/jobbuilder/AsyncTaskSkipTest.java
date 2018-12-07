package com.morethanheroic.taskforce.sample.jobbuilder;

import com.morethanheroic.taskforce.executor.JobExecutor;
import com.morethanheroic.taskforce.generator.StreamGenerator;
import com.morethanheroic.taskforce.job.Job;
import com.morethanheroic.taskforce.job.builder.JobBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class AsyncTaskSkipTest {

    @Test
    public void integrationTestForSkipTest() {
        final Job job = JobBuilder.newBuilder()
                .generator(StreamGenerator.of(IntStream.range(0, 1000000)))
                .task("task-one", (value) -> {
                    if (value == 5) {
                        return Optional.of(value);
                    }

                    return Optional.empty();
                })
                .task("task-two", Optional::of)
                .sink((value) -> assertThat(value, is(5)))
                .build();

        final JobExecutor jobExecutor = new JobExecutor();

        jobExecutor.execute(job);
    }
}
