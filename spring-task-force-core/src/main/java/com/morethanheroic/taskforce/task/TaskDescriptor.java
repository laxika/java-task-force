package com.morethanheroic.taskforce.task;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaskDescriptor<INPUT, OUTPUT> {

    private final int parallelismLevel;
    private final int maxQueueSize;
    private final Task<INPUT, OUTPUT> task;
}
