package com.morethanheroic.taskforce.executor.item;

import com.morethanheroic.taskforce.sink.Sink;
import com.morethanheroic.taskforce.task.domain.TaskDescriptor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class WorkItemProcessor {

    @SuppressWarnings("unchecked") // The type checking is done in the builders
    public void processWorkItem(final Object rawWorkItem, final List<TaskDescriptor<?, ?>> taskDescriptors,
            final Sink sink) {
        Optional<?> workItem = Optional.of(rawWorkItem);

        for (TaskDescriptor taskDescriptor : taskDescriptors) {
            //Skip empty working items
            if (!workItem.isPresent()) {
                return;
            }

            workItem = taskDescriptor.getTask().execute(workItem.get());
        }

        if (!workItem.isPresent()) {
            return;
        }

        sink.consume(workItem.get());
    }
}
