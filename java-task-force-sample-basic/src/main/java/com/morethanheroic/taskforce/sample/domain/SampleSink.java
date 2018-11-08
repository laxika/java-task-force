package com.morethanheroic.taskforce.sample.domain;

import com.morethanheroic.taskforce.sink.Sink;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SampleSink implements Sink<String> {

    @Override
    public void consume(String input) {
        log.info(input);
    }
}
