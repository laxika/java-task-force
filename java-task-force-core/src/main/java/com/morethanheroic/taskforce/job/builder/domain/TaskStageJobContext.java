package com.morethanheroic.taskforce.job.builder.domain;

import com.morethanheroic.taskforce.generator.Generator;
import com.morethanheroic.taskforce.sink.Sink;
import com.morethanheroic.taskforce.task.domain.TaskDescriptor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Contains the data for the second step of the {@link com.morethanheroic.taskforce.job.Job} creation.
 */
@Getter
@Builder
public class TaskStageJobContext {

    private final Generator generator;
    private final Sink sink;
    private final List<TaskDescriptor<?, ?>> taskDescriptors;
}
