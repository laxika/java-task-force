package com.morethanheroic.taskforce.sample.warcparser.parser.task;

import com.morethanheroic.taskforce.sample.warcparser.parser.domain.Record;
import com.morethanheroic.taskforce.sample.warcparser.parser.domain.RecordType;
import com.morethanheroic.taskforce.task.Task;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RecordTypeFilterTask implements Task<Record, Record> {

    private final List<RecordType> allowedRecordTypes;

    public RecordTypeFilterTask(final RecordType... allowedRecordTypes) {
        this.allowedRecordTypes = Arrays.asList(allowedRecordTypes);
    }

    @Override
    public Optional<Record> execute(final Record record) {
        return allowedRecordTypes.contains(record.getType()) ? Optional.of(record) : Optional.empty();
    }
}
