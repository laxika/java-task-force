package com.morethanheroic.taskforce.sample.warcparser.parser.task;

import com.morethanheroic.taskforce.sample.warcparser.parser.domain.ContentType;
import com.morethanheroic.taskforce.sample.warcparser.parser.domain.Record;
import com.morethanheroic.taskforce.task.Task;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RecordContentTypeFilterTask implements Task<Record, Record> {

    private final List<ContentType> allowedContentTypes;

    public RecordContentTypeFilterTask(final ContentType... contentTypes) {
        this.allowedContentTypes = Arrays.asList(contentTypes);
    }

    @Override
    public Optional<Record> execute(final Record record) {
        final String contentType = record.getHeaders().getOrDefault("Content-Type", "")
                .replaceAll(";.*", "");

        if (contentType.isEmpty() || !ContentType.getByMimeType(contentType).isPresent() ||
                !allowedContentTypes.contains(ContentType.getByMimeType(contentType).get())) {
            return Optional.empty();
        }

        return Optional.of(record);
    }
}
