package com.morethanheroic.taskforce.sink;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggingSink<INPUT> implements Sink<INPUT> {

    private final Logger log = LoggerFactory.getLogger(LoggingSink.class);

    private final String format;
    private final boolean logEmpty;

    public static <WORK_ITEM_TYPE> LoggingSink<WORK_ITEM_TYPE> of(final String format) {
        return new LoggingSink<>(format, true);
    }

    public static <WORK_ITEM_TYPE> LoggingSink<WORK_ITEM_TYPE> of(final String format, final boolean logEmpty) {
        return new LoggingSink<>(format, logEmpty);
    }

    @Override
    public void consume(INPUT input) {
        if (!logEmpty && isEmptyInput(input)) {
            return;
        }

        log.info(format, input);
    }

    @Override
    public void close() {
        log.info("Running job is finished.");
    }

    private boolean isEmptyInput(final INPUT input) {
        return input == null || isEmptyCollection(input) || isEmptyString(input);
    }

    private boolean isEmptyCollection(final INPUT input) {
        return input instanceof Collection && ((Collection) input).size() == 0;
    }

    private boolean isEmptyString(final INPUT input) {
        return input instanceof String && ((String) input).isEmpty();
    }
}
