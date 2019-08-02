package com.morethanheroic.taskforce.sample;

import com.morethanheroic.taskforce.executor.JobExecutor;
import com.morethanheroic.taskforce.generator.StreamGenerator;
import com.morethanheroic.taskforce.job.Job;
import com.morethanheroic.taskforce.job.builder.JobBuilder;
import com.morethanheroic.taskforce.sink.LoggingSink;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Random;

/**
 * An example with lambda.
 */
@Slf4j
public class LambdaSampleApplication {

    public static void main(final String... args) {
        final Random random = new Random();

        final Job job = JobBuilder.newBuilder()
                .generator(() -> {
                    final int randomValue = random.nextInt();

                    return randomValue < 2000000000 ? Optional.of(randomValue) : Optional.empty();
                })
                .task((integer) -> {
                    if (integer % 2 == 0) {
                        return Optional.of(integer);
                    }

                    return Optional.empty();
                })
                .sink((value) -> log.info("Value: " + value))
                .build();

        final JobExecutor jobExecutor = new JobExecutor();
        jobExecutor.execute(job);
    }
}
