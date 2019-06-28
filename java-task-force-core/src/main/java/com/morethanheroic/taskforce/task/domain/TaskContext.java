package com.morethanheroic.taskforce.task.domain;

import lombok.Builder;
import lombok.Getter;

/**
 * Contains the attributes of a task.
 */
@Getter
@Builder
public class TaskContext {

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
}
