package com.morethanheroic.taskforce.executor.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobExecutionContext {

    /**
     * Describes that how many work items should be prepared for invocation by the Task Force engine. Optimally this
     * value is as high as the highest maximum queue size in your tasks' executors.
     */
    @Builder.Default
    private final int preparedTaskCount = 10000;
}
