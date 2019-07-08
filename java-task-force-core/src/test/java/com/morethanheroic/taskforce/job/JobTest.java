package com.morethanheroic.taskforce.job;

import com.morethanheroic.taskforce.executor.context.JobContext;
import com.morethanheroic.taskforce.executor.task.TaskExecutor;
import com.morethanheroic.taskforce.generator.Generator;
import com.morethanheroic.taskforce.sink.Sink;
import com.morethanheroic.taskforce.task.domain.TaskDescriptor;
import lombok.Builder;
import lombok.Getter;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JobTest {

    @Test
    public void testJobInitialization() {
        final Generator<?> mockGenerator = mock(Generator.class);
        final Sink<?> mockSink = mock(Sink.class);

        final Job job = TestJob.builder()
                .generator(mockGenerator)
                .sink(mockSink)
                .build();

        job.initialize();

        verify(mockGenerator).open();
        verify(mockSink).open();
    }

    @Test
    public void testJobCleanup() {
        final Generator<?> mockGenerator = mock(Generator.class);
        final Sink<?> mockSink = mock(Sink.class);
        final TaskExecutor mockTaskExecutor = mock(TaskExecutor.class);

        final Job job = TestJob.builder()
                .generator(mockGenerator)
                .sink(mockSink)
                .taskExecutor(mockTaskExecutor)
                .build();

        job.cleanup();

        final InOrder mockTaskExecutorOrder = inOrder(mockTaskExecutor);
        mockTaskExecutorOrder.verify(mockTaskExecutor).waitUntilFinished();
        mockTaskExecutorOrder.verify(mockTaskExecutor).shutdown();

        verify(mockGenerator).close();
        verify(mockSink).close();
    }

    @Test
    public void testIsFinished() {
        final JobContext mockJobContext = mock(JobContext.class);
        when(mockJobContext.isLastItemReached()).thenReturn(true);

        final Job job = TestJob.builder()
                .jobContext(mockJobContext)
                .build();

        final boolean result = job.isFinished();

        assertThat(result, is(true));

        verify(mockJobContext).isLastItemReached();
    }

    @Getter
    @Builder
    private static class TestJob extends Job {

        private final Generator<?> generator;
        private final Sink<?> sink;
        private final TaskExecutor taskExecutor;
        private final JobContext jobContext;

        @Override
        public List<TaskDescriptor<?, ?>> getTaskDescriptors() {
            return null;
        }
    }
}