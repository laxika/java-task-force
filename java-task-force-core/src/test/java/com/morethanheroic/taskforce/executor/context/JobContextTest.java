package com.morethanheroic.taskforce.executor.context;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JobContextTest {

    private JobContext jobContext;

    @Before
    public void setup() {
        jobContext = new JobContext();
    }

    @Test
    public void testIsLastItemReachedWhenNotReached() {
        assertThat(jobContext.isLastItemReached(), is(false));
    }

    @Test
    public void testSetLastItemReachedWhenNotReached() {
        jobContext.setLastItemReached();

        assertThat(jobContext.isLastItemReached(), is(true));
    }
}
