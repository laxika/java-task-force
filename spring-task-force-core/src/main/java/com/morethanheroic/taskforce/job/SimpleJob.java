package com.morethanheroic.taskforce.job;

import com.morethanheroic.taskforce.generator.Generator;
import com.morethanheroic.taskforce.sink.Sink;
import com.morethanheroic.taskforce.task.TaskDescriptor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class SimpleJob implements Job {

  private final Generator generator;
  private final List<TaskDescriptor> tasks;
  private final Sink sink;
}
