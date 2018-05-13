package com.morethanheroic.taskforce.sample.domain;

import com.morethanheroic.taskforce.task.Task;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SlowSampleTask implements Task<String, String> {

    @Override
    public String execute(String s) {
        log.info("Processing " + s + " by slow task.");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return s.toUpperCase();
    }
}
