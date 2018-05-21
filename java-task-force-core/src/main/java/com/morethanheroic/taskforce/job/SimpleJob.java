package com.morethanheroic.taskforce.job;

import com.morethanheroic.taskforce.generator.Generator;
import com.morethanheroic.taskforce.sink.Sink;
import com.morethanheroic.taskforce.task.domain.TaskDescriptor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * The default implementation of a {@link Job}.
 */
@Getter
@RequiredArgsConstructor
public class SimpleJob implements Job {

    private final Generator generator;
    private final List<TaskDescriptor<?, ?>> taskDescriptors;
    private final Sink sink;
}
