package com.morethanheroic.taskforce.sink;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LoggingSink.class, LoggerFactory.class})
public class LoggingSinkTest {

    private static final String TEST_FORMAT = "format";
    private static final String TEST_LOG_MESSAGE = "message";

    private Logger logger;

    @Before
    public void setup() {
        mockStatic(LoggerFactory.class);
        logger = mock(Logger.class);
        when(LoggerFactory.getLogger(LoggingSink.class)).thenReturn(logger);
    }

    @Test
    public void testConsumeWhenMessageNotEmptyAndLogEmptyEnabled() {
        final LoggingSink<String> loggingSink = LoggingSink.of(TEST_FORMAT);

        loggingSink.consume(TEST_LOG_MESSAGE);

        verify(logger).info(TEST_FORMAT, TEST_LOG_MESSAGE);
    }

    @Test
    public void testConsumeWhenMessageEmptyStringAndLogEmptyEnabled() {
        final LoggingSink<String> loggingSink = LoggingSink.of(TEST_FORMAT);

        loggingSink.consume("");

        verify(logger).info(TEST_FORMAT, "");
    }

    @Test
    public void testConsumeWhenMessageEmptyCollectionAndLogEmptyEnabled() {
        final LoggingSink<List<?>> loggingSink = LoggingSink.of(TEST_FORMAT);

        loggingSink.consume(Collections.EMPTY_LIST);

        verify(logger).info(TEST_FORMAT, Collections.EMPTY_LIST);
    }

    @Test
    public void testConsumeWhenMessageNullAndLogEmptyEnabled() {
        final LoggingSink<String> loggingSink = LoggingSink.of(TEST_FORMAT);
        final String testNull = null;

        loggingSink.consume(testNull);

        verify(logger).info(TEST_FORMAT, testNull);
    }

    @Test
    public void testConsumeWhenMessageEmptyStringAndLogEmptyDisabled() {
        final LoggingSink<String> loggingSink = LoggingSink.of(TEST_FORMAT, false);

        loggingSink.consume("");

        verify(logger, never()).info(TEST_FORMAT, "");
    }

    @Test
    public void testConsumeWhenMessageEmptyCollectionAndLogEmptyDisabled() {
        final LoggingSink<List<?>> loggingSink = LoggingSink.of(TEST_FORMAT, false);

        loggingSink.consume(Collections.EMPTY_LIST);

        verify(logger, never()).info(TEST_FORMAT, Collections.EMPTY_LIST);
    }

    @Test
    public void testConsumeWhenMessageNullAndLogEmptyDisabled() {
        final LoggingSink<String> loggingSink = LoggingSink.of(TEST_FORMAT, false);
        final String testNull = null;

        loggingSink.consume(testNull);

        verify(logger, never()).info(TEST_FORMAT, testNull);
    }
}
