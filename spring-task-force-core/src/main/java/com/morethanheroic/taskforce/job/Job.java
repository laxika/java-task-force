package com.morethanheroic.taskforce.job;

import com.morethanheroic.taskforce.generator.Generator;
import com.morethanheroic.taskforce.sink.Sink;
import com.morethanheroic.taskforce.task.Task;

import java.util.List;

public interface Job {

    Generator getGenerator();

    Sink getSink();

    List<Task> getTasks();
}
