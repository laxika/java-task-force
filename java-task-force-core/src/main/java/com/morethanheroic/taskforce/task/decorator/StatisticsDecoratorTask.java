package com.morethanheroic.taskforce.task.decorator;

import com.morethanheroic.taskforce.task.Task;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * This class is a decorator for a {@link Task}. It wraps the underlying task and adds a statistics collecting
 * and reporting functionality.
 *
 * @param <INPUT>  the input of the underlying task
 * @param <OUTPUT> the output of the underlying task
 */
@Slf4j
@RequiredArgsConstructor
public class StatisticsDecoratorTask<INPUT, OUTPUT> implements Task<INPUT, OUTPUT> {

    @Getter
    private final String delegateName;

    @Getter
    private final Task<INPUT, OUTPUT> delegate;

    @Getter
    private final boolean reportingEnabled;

    @Getter
    private final int reportingRate;

    //TODO: Move these out to the builder/task descriptor when we want to enable statistics gathering for a job.
    private final AtomicLong totalItemCount = new AtomicLong();
    private final LongAdder successfulItemCount = new LongAdder();
    private final LongAdder unsuccessfulItemCount = new LongAdder();

    @Override
    public Optional<OUTPUT> execute(INPUT input) {
        final long totalCount = totalItemCount.incrementAndGet();

        try {
            final Optional<OUTPUT> result = delegate.execute(input);

            successfulItemCount.increment();

            doLogging(totalCount);

            return result;
        } catch (Exception e) {
            unsuccessfulItemCount.increment();

            doLogging(totalCount);

            throw e;
        }
    }

    private void doLogging(final long totalResultCount) {
        if (reportingEnabled && totalResultCount % reportingRate == 0) {
            final long successfulCount = successfulItemCount.sum();
            final long unsuccessfulCount = unsuccessfulItemCount.sum();
            final long totalInvocation = successfulCount + unsuccessfulCount;

            log.info("The '{}' task ran {} times. Successful invocations: {} unsuccessful invocations: {}.",
                    delegateName, totalInvocation, successfulCount, unsuccessfulCount);
        }
    }
}
