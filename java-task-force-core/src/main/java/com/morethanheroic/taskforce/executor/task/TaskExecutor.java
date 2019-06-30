package com.morethanheroic.taskforce.executor.task;

import com.morethanheroic.taskforce.sink.Sink;
import com.morethanheroic.taskforce.task.domain.TaskDescriptor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * This class is responsible for running the tasks available in a {@link com.morethanheroic.taskforce.job.Job}.
 */
@Slf4j
public class TaskExecutor {

    private final Semaphore semaphore;
    private final ExecutorService taskExecutorService;
    private final int threadCount;

    private TaskExecutor(final int threadCount) {
        this.threadCount = threadCount;

        semaphore = new Semaphore(threadCount);
        taskExecutorService = Executors.newFixedThreadPool(threadCount);
    }

    @SuppressWarnings("unchecked") // The type checking is done in the builders
    public void submitTasks(final Object rawWorkItem, final List<TaskDescriptor<?, ?>> taskDescriptors, final Sink sink) {
        acquireWorkingSlot();

        taskExecutorService.submit(() -> {
            Optional<?> workItem = Optional.of(rawWorkItem);

            try {
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
            } catch (final Exception e) {
                log.warn("Error while processing tasks!", e);
            } finally {
                releaseWorkingSlot();
            }
        });
    }

    public void shutdown() {
        taskExecutorService.shutdown();
    }

    public void waitUntilFinished() {
        try {
            semaphore.acquire(threadCount);
        } catch (final InterruptedException e) {
            throw new RuntimeException("Unable to wait until all tasks are finished!", e);
        }
    }

    private void acquireWorkingSlot() {
        try {
            semaphore.acquire();
        } catch (final InterruptedException e) {
            throw new RuntimeException("Unable to acquire working slot!");
        }
    }

    private void releaseWorkingSlot() {
        semaphore.release();
    }

    public static TaskExecutor.TaskExecutorBuilder builder() {
        return new TaskExecutor.TaskExecutorBuilder();
    }

    public static class TaskExecutorBuilder {

        private int threadCount;

        public TaskExecutorBuilder threadCount(final int threadCount) {
            this.threadCount = threadCount;

            return this;
        }

        public TaskExecutor build() {
            return new TaskExecutor(threadCount);
        }
    }
}
