package com.morethanheroic.taskforce.executor.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class BlockingThreadPoolExecutor extends ThreadPoolExecutor {

    private final Semaphore semaphore;

    public BlockingThreadPoolExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime,
            final TimeUnit unit, final int workQueueSize, final ThreadFactory threadFactory,
            final RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<>(workQueueSize),
                threadFactory, handler);

        semaphore = new Semaphore(workQueueSize);
    }


    @Override
    public void execute(final Runnable task) {
        boolean acquired = false;

        do {
            try {
                semaphore.acquire();
                acquired = true;
            } catch (final InterruptedException e) {
                log.warn("InterruptedException while acquiring semaphore in blocking queue!", e);
            }
        } while (!acquired);

        try {
            super.execute(task);
        } catch (final RejectedExecutionException e) {
            log.debug("Task rejected by blocking queue!");

            semaphore.release();

            throw e;
        }
    }

    @Override
    protected void afterExecute(final Runnable r, final Throwable t) {
        super.afterExecute(r, t);

        semaphore.release();
    }
}
