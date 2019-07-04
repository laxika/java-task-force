package com.morethanheroic.taskforce.job.builder;

import com.morethanheroic.taskforce.generator.Generator;
import com.morethanheroic.taskforce.job.Job;
import com.morethanheroic.taskforce.sink.Sink;
import com.morethanheroic.taskforce.task.Task;
import com.morethanheroic.taskforce.task.decorator.StatisticsDecoratorTask;
import com.morethanheroic.taskforce.task.domain.TaskContext;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/*
 * We are testing the whole builder in one go. Otherwise we would need to do some heavy reflection hacking.
 */
public class JobBuilderTest {

    private static final String TEST_TASK_NAME = "TASK_NAME";
    private static final int DEFAULT_UUID_LENGTH = 36;
    private static final int DEFAULT_STATISTICS_REPORTING_RATE = 100;

    @Test
    public void testBuilderWithNoNameNoContextTask() {
        final Generator<Integer> mockGenerator = mock(Generator.class);
        final Task<Integer, Integer> mockTask = mock(Task.class);
        final Sink<Integer> mockSink = mock(Sink.class);

        final Job job = JobBuilder.newBuilder()
                .generator(mockGenerator)
                .task(mockTask)
                .sink(mockSink)
                .build();

        assertThat(job.getGenerator(), is(mockGenerator));
        assertThat(job.getTaskDescriptors().size(), is(1));
        assertThat(job.getTaskDescriptors().get(0).getTask(), is(mockTask));
        assertThat(job.getTaskDescriptors().get(0).getTaskName().length(), is(DEFAULT_UUID_LENGTH));
        assertThat(job.getSink(), is(mockSink));
    }

    @Test
    public void testBuilderWithNamedNoContextTask() {
        final Generator<Integer> mockGenerator = mock(Generator.class);
        final Task<Integer, Integer> mockTask = mock(Task.class);
        final Sink<Integer> mockSink = mock(Sink.class);

        final Job job = JobBuilder.newBuilder()
                .generator(mockGenerator)
                .task(TEST_TASK_NAME, mockTask)
                .sink(mockSink)
                .build();

        assertThat(job.getGenerator(), is(mockGenerator));
        assertThat(job.getTaskDescriptors().size(), is(1));
        assertThat(job.getTaskDescriptors().get(0).getTask(), is(mockTask));
        assertThat(job.getTaskDescriptors().get(0).getTaskName(), is(TEST_TASK_NAME));
        assertThat(job.getSink(), is(mockSink));
    }

    @Test
    public void testBuilderWithNoNameAndStatisticsCollectionEnabled() {
        final Generator<Integer> mockGenerator = mock(Generator.class);
        final Task<Integer, Integer> mockTask = mock(Task.class);
        final Sink<Integer> mockSink = mock(Sink.class);
        final TaskContext taskContext = TaskContext.builder()
                .statisticsCollectionEnabled(true)
                .build();

        final Job job = JobBuilder.newBuilder()
                .generator(mockGenerator)
                .task(mockTask, taskContext)
                .sink(mockSink)
                .build();

        assertThat(job.getGenerator(), is(mockGenerator));
        assertThat(job.getTaskDescriptors().size(), is(1));
        assertThat(job.getTaskDescriptors().get(0).getTask(), is(instanceOf(StatisticsDecoratorTask.class)));
        final StatisticsDecoratorTask statisticsDecoratorTask = (StatisticsDecoratorTask) job.getTaskDescriptors()
                .get(0).getTask();
        assertThat(statisticsDecoratorTask.getDelegate(), is(mockTask));
        assertThat(statisticsDecoratorTask.getReportingRate(), is(DEFAULT_STATISTICS_REPORTING_RATE));
        assertThat(statisticsDecoratorTask.getDelegateName().length(), is(DEFAULT_UUID_LENGTH));
        assertThat(statisticsDecoratorTask.isReportingEnabled(), is(false));
        assertThat(job.getTaskDescriptors().get(0).getTaskName().length(), is(DEFAULT_UUID_LENGTH));
        assertThat(job.getSink(), is(mockSink));
    }
}
