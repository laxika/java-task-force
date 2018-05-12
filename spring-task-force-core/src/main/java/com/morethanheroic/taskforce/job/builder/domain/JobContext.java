package com.morethanheroic.taskforce.job.builder.domain;


import com.morethanheroic.taskforce.generator.Generator;
import com.morethanheroic.taskforce.sink.Sink;
import com.morethanheroic.taskforce.task.Task;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JobContext {

  private Generator generator;
  private Sink sink;
  private final List<Task> tasks = new ArrayList<>();
}
