package com.morethanheroic.taskforce.sample.warcparser.parser;

import com.morethanheroic.taskforce.sink.Sink;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class WarcSink implements Sink<List<String>> {

    @Override
    public void consume(final List<String> warcRecord) {
        warcRecord.forEach(log::info);
    }
}
