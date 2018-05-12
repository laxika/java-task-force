package com.morethanheroic.taskforce.sample.domain;

import com.morethanheroic.taskforce.task.AsyncTask;

public class SlowSampleTask extends AsyncTask<String, String> {

    public SlowSampleTask() {
        super(10);
    }

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
