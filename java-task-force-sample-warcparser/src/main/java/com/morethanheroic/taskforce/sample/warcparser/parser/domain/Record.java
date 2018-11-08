package com.morethanheroic.taskforce.sample.warcparser.parser.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;

@Getter
@Builder
public class Record {

    private final RecordType type;
    private final Map<String, String> headers;
    private final String body;

    public Optional<String> getBody() {
        return Optional.ofNullable(body);
    }
}
