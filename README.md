# Java Task Force

**Java Task Force (JTF)** is a minimalistic batch framework that aims to radically simplify the writing and running of single-instance batch processing applications.

## Domain Language

### Job

Jobs are the unit of work for JTF. It is a simple processing pipeline. A job contains exactly one **generator**, exactly one **sink** and any number of **tasks** in-between. A job is responsible for the processing of **work items**.

A job can be created using the `JobBuilder.newBuilder()` method.

### Generator

The generator's responsibility is to provide work items to the tasks to work on. It is queried for new work items as long as it doesn't reply with an empty one.

Creating a generator could be achieved by implementing the `com.morethanheroic.taskforce.generator.Generator` interface.

For example:
```
import com.morethanheroic.taskforce.generator.Generator;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class SampleGenerator implements Generator<String> {

    private int value = 0;

    @Override
    public Optional<String> generate() {
        value++;

        if (value < 50) {
            log.info("Adding value: " + value + " to the working set.");

            return Optional.of(String.valueOf(value));
        }

        return Optional.empty();
    }
}
```

A couple of pre-defined generators are provided by the framework: EnumerationGenerator, IterableGenerator, IteratorGenerator, StreamGenerator.

For example:
```
StreamGenerator.of(streamOfItems);
```

### Task

Tasks are doing any kind of work on the provided work items. This could be transformation, filtering, counting etc. A task could be created by implementing the `com.morethanheroic.taskforce.task.Task` interface.

For example:
```
import com.morethanheroic.taskforce.task.Task;

import java.util.Optional;

public class SampleTask implements Task<String, String> {

    @Override
    public Optional<String> execute(String s) {
        return Optional.of(s.toUpperCase());
    }
}
```

If the task return an empty `Optional` then the item is not going to be processed any further and not being handled by the `Sink` on the end of the pipeline.

### Sink

The sink is responsible to do the final processing of an item. For example save it to a database, send it out as an e-mail etc.

Creating a generator could be achieved by implementing the `com.morethanheroic.taskforce.sink.Sink` interface.

For example:
```
import com.morethanheroic.taskforce.sink.Sink;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SampleSink implements Sink<String> {

    @Override
    public void consume(String input) {
        log.info(input);
    }
}
```

A couple of pre-defined generators are provided by the framework: LoggingSink, DiscardingSink.

For example:
```
DiscardingSink.of(streamOfItems);
```

### Job Executor

The job executor is responsible to run a job.

```
final JobExecutor jobExecutor = new JobExecutor();
jobExecutor.execute(job);
```

## Example

An fully working basic example is available in the `java-task-force-sample` project. A much more interesting but complicated example is available in the `java-task-force-sample-warcparser`.

```
import com.morethanheroic.taskforce.executor.JobExecutor;
import com.morethanheroic.taskforce.job.Job;
import com.morethanheroic.taskforce.job.builder.JobBuilder;
import com.morethanheroic.taskforce.sample.domain.SampleGenerator;
import com.morethanheroic.taskforce.sample.domain.SampleSink;
import com.morethanheroic.taskforce.sample.domain.SampleTask;
import com.morethanheroic.taskforce.sample.domain.SlowSampleTask;
import com.morethanheroic.taskforce.task.domain.TaskContext;

public class SampleApplication {

    public static void main(final String... args) {
        final Job job = JobBuilder.newBuilder()
                .generator(new SampleGenerator())
                .task("task-one", new SampleTask())
                .task("task-two", new SlowSampleTask(),
                        TaskContext.builder()
                                .statisticsCollectionEnabled(true)
                                .statisticsReportingEnabled(true)
                                .statisticsReportingRate(5)
                                .build()
                )
                .sink(new SampleSink())
                .build();

        final JobExecutor jobExecutor = new JobExecutor();
        jobExecutor.execute(job);
    }
}
```
