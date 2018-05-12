package com.morethanheroic.taskforce.job;

import com.morethanheroic.taskforce.task.AsyncTask;
import com.morethanheroic.taskforce.task.Task;

import java.util.Optional;
import java.util.concurrent.Future;

public class JobExecutor {

    public void execute(final Job job) {
        Optional<?> netInput = job.getGenerator().generate();

        while (netInput.isPresent()) {
            Object value = netInput.get();

            for (Task task : job.getTasks()) {
                if (task instanceof AsyncTask) {
                    //RESOLVETHENCALL???
                } else {
                    value = task.execute(value);
                }
            }

            job.getSink().consume(value);

            netInput = job.getGenerator().generate();
        }
    }
}
