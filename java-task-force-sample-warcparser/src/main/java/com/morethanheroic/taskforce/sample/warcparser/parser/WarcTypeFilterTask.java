package com.morethanheroic.taskforce.sample.warcparser.parser;

import com.mixnode.warcreader.record.WarcRecord;
import com.morethanheroic.taskforce.task.Task;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class WarcTypeFilterTask implements Task<WarcRecord, WarcRecord> {

    private final List<WarcRecord.WarcType> allowedTypes;

    public WarcTypeFilterTask(final WarcRecord.WarcType... allowedTypes) {
        this.allowedTypes = Arrays.asList(allowedTypes);
    }

    @Override
    public Optional<WarcRecord> execute(final WarcRecord warcRecord) {
        if (allowedTypes.contains(warcRecord.getType())) {
            return Optional.of(warcRecord);
        }

        return Optional.empty();
    }
}
