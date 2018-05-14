package com.morethanheroic.taskforce.sample.domain;

import com.morethanheroic.taskforce.task.Task;

import java.util.Optional;

public class SampleTask implements Task<String, String> {

    @Override
    public Optional<String> execute(String s) {
        return Optional.of(s.toUpperCase());
    }
}
