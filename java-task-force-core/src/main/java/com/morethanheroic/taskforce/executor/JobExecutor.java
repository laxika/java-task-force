package com.morethanheroic.taskforce.executor;

import com.morethanheroic.taskforce.job.Job;
import com.morethanheroic.taskforce.task.domain.TaskDescriptor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class JobExecutor {

    public void execute(final Job job) {
        execute(job, Runtime.getRuntime().availableProcessors());
    }

    public void execute(final Job job, final int threadCount) {
        final Semaphore semaphore = new Semaphore(threadCount);
        final ExecutorService taskExecutorService = Executors.newFixedThreadPool(threadCount);

        final AtomicBoolean calculator = new AtomicBoolean(true);

        //TODO: Clean this up!

        // Open the generator before start the processing
        job.getGenerator().open();
        while (calculator.get()) {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                //TODO!
                e.printStackTrace();
            }

            try {
                final Optional<?> generationResult = job.getGenerator().generate();

                if (!generationResult.isPresent()) {
                    job.getGenerator().close();

                    calculator.set(false);
                }

                taskExecutorService.submit(() -> {
                    try {
                        Optional<?> workItem = generationResult;

                        for (TaskDescriptor taskDescriptor : job.getTaskDescriptors()) {
                            //Skip empty working items
                            if (!workItem.isPresent()) {
                                return;
                            }

                            workItem = taskDescriptor.getTask().execute(workItem.get());
                        }

                        if (!workItem.isPresent()) {
                            return;
                        }

                        job.getSink().consume(workItem.get());

                    } catch (Exception e) {
                        //TODO: I don't think we need this here!
                        e.printStackTrace();
                    } finally {
                        semaphore.release();
                    }
                });
            } catch (Exception e) {
                // This catch is handling the generator's exceptions!
                //TODO: I don't think we need this here!
                e.printStackTrace();
            }
        }

        // Cleaning up the executor services.
        try {
            semaphore.acquire(threadCount);

            job.getSink().close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            taskExecutorService.shutdown();
        }
    }
}
