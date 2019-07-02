package com.morethanheroic.taskforce.generator;

import org.junit.Test;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class IterableGeneratorTest {

    @Test
    public void testGenerate() {
        final Iterable<Integer> integerStream = Collections.singletonList(10);
        final IterableGenerator<Integer> streamGenerator = IterableGenerator.of(integerStream);

        final Optional<Integer> firstResult = streamGenerator.generate();
        assertThat(firstResult.isPresent(), is(true));
        assertThat(firstResult.get(), is(10));

        final Optional<Integer> secondResult = streamGenerator.generate();
        assertThat(secondResult.isPresent(), is(false));
    }
}
