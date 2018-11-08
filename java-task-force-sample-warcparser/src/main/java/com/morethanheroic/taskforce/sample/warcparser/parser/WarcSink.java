package com.morethanheroic.taskforce.sample.warcparser.parser;

import com.mixnode.warcreader.record.WarcRecord;
import com.morethanheroic.taskforce.sink.Sink;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WarcSink implements Sink<WarcRecord> {

    @Override
    public void consume(final WarcRecord warcRecord) {
        log.info(warcRecord.getRecordID());
    }
}
