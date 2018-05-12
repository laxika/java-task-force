package com.morethanheroic.taskforce.sample.domain;

import com.morethanheroic.taskforce.task.Task;

public class SlowSampleTask implements Task<String, String> {

    @Override
    public String execute(String s) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return s.toUpperCase();
    }
}
