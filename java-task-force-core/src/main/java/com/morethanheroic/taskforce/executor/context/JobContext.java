package com.morethanheroic.taskforce.executor.context;

import lombok.Builder;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is responsible for holding every contextual information about a
 * {@link com.morethanheroic.taskforce.job.Job}.
 */
@Builder
public class JobContext {

    private final AtomicBoolean lastItemReached = new AtomicBoolean();

    public void setLastItemReached() {
        lastItemReached.set(true);
    }

    public boolean isLastItemReached() {
        return lastItemReached.get();
    }
}
