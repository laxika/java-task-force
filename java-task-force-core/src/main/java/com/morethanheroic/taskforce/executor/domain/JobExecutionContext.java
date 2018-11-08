package com.morethanheroic.taskforce.executor.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobExecutionContext {

    private final int preparedTaskCount;
}
