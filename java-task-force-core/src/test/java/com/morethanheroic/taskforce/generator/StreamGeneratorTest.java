package com.morethanheroic.taskforce.generator;

import org.junit.Test;

import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class StreamGeneratorTest {

    @Test
    public void testGenerate() {
        final Stream<Integer> integerStream = Stream.of(10);
        final StreamGenerator<Integer> streamGenerator = StreamGenerator.of(integerStream);

        final Optional<Integer> firstResult = streamGenerator.generate();
        assertThat(firstResult.isPresent(), is(true));
        assertThat(firstResult.get(), is(10));

        final Optional<Integer> secondResult = streamGenerator.generate();
        assertThat(secondResult.isPresent(), is(false));
    }
}
