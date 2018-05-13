package com.morethanheroic.taskforce.task;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaskDescriptor {

    private final int parallelismLevel;
    private final int maxQueueSize;
    private final Task<Object, Object> task;
}
