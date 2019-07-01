package com.morethanheroic.taskforce.executor;

import com.morethanheroic.taskforce.executor.context.JobContext;
import com.morethanheroic.taskforce.executor.task.TaskExecutor;
import com.morethanheroic.taskforce.generator.Generator;
import com.morethanheroic.taskforce.job.Job;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class JobExecutorTest {

    private JobExecutor jobExecutor;

    @Before
    public void setup() {
        jobExecutor = new JobExecutor();
    }

    @Test
    public void testExecuteWhenAlreadyFinished() {
        final Job mockJob = mock(Job.class);
        when(mockJob.isFinished()).thenReturn(true);

        jobExecutor.execute(mockJob);

        InOrder inOrder = inOrder(mockJob);

        inOrder.verify(mockJob).initialize();
        inOrder.verify(mockJob).isFinished();
        inOrder.verify(mockJob).cleanup();
    }

    @Test
    public void testExecuteWhenNotAlreadyFinished() {
        final Job mockJob = mock(Job.class);
        when(mockJob.isFinished()).thenReturn(false).thenReturn(false).thenReturn(true);
        final Generator mockGenerator = mock(Generator.class);
        when(mockJob.getGenerator()).thenReturn(mockGenerator);
        final Object mockEntity = mock(Object.class);
        when(mockGenerator.generate()).thenReturn(Optional.of(mockEntity), Optional.empty());
        final JobContext mockJobContext = mock(JobContext.class);
        when(mockJob.getJobContext()).thenReturn(mockJobContext);
        final TaskExecutor mockTaskExecutor = mock(TaskExecutor.class);
        when(mockJob.getTaskExecutor()).thenReturn(mockTaskExecutor);

        jobExecutor.execute(mockJob);

        final InOrder inOrder = inOrder(mockJob);
        inOrder.verify(mockJob).initialize();
        inOrder.verify(mockJob).isFinished();
        inOrder.verify(mockJob).cleanup();

        verify(mockJobContext).setLastItemReached();
        verify(mockTaskExecutor).submitTasks(any(), any(), any());
    }

    @Test(expected = RuntimeException.class)
    public void testWhenExceptionIsThrown() {
        final Job mockJob = mock(Job.class);
        when(mockJob.isFinished()).thenReturn(false);
        when(mockJob.getTaskExecutor()).thenThrow(new NullPointerException());

        jobExecutor.execute(mockJob);
    }
}
