package com.morethanheroic.taskforce.sink;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggingSink<INPUT> implements Sink<INPUT> {

    private final String format;

    public static <WORK_ITEM_TYPE> LoggingSink<WORK_ITEM_TYPE> of(final String format) {
        return new LoggingSink<>(format);
    }

    @Override
    public void consume(INPUT input) {
        log.info(format, input);
    }
}
