package com.morethanheroic.taskforce.sink;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

public class DiscardingSinkTest {

    @Test
    public void testBuilder() {
        final DiscardingSink discardingSink = DiscardingSink.of();

        assertThat(discardingSink, is(notNullValue()));
    }
}
