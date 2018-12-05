package com.morethanheroic.taskforce.task.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.Optional;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Contains the attributes of a task.
 */
@Getter
@Builder
public class TaskContext {

    /**
     * How many thread should run the task in parallel.
     */
    @Builder.Default
    private final int parallelismLevel = 1;

    /**
     * How big should be the queue that's feeding the working threads.
     */
    @Builder.Default
    private final int maxQueueSize = 1;

    /**
     * Should the task collect statistics about it's work.
     */
    @Builder.Default
    private final boolean statisticsCollectionEnabled = false;

    /**
     * Should the task report statistics about it's work.
     */
    @Builder.Default
    private final boolean statisticsReportingEnabled = false;

    /**
     * How many items should be processed between two statistics reporting. If the {@link #statisticsReportingEnabled}
     * is false then no reporting will take place.
     */
    @Builder.Default
    private final int statisticsReportingRate = 100;

    /**
     * The executor that should run the task. If an executor is provided then the {@link #parallelismLevel} and the
     * {@link #maxQueueSize} variables will be disregarded.
     */
    private final ThreadPoolExecutor executor;

    public Optional<ThreadPoolExecutor> getExecutor() {
        return Optional.ofNullable(executor);
    }
}
