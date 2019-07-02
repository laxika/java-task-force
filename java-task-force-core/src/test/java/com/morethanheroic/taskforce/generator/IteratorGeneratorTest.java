package com.morethanheroic.taskforce.generator;

import org.junit.Test;

import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class IteratorGeneratorTest {

    @Test
    public void testGenerate() {
        final Iterator<Integer> integerStream = Stream.of(10).iterator();
        final IteratorGenerator<Integer> streamGenerator = IteratorGenerator.of(integerStream);

        final Optional<Integer> firstResult = streamGenerator.generate();
        assertThat(firstResult.isPresent(), is(true));
        assertThat(firstResult.get(), is(10));

        final Optional<Integer> secondResult = streamGenerator.generate();
        assertThat(secondResult.isPresent(), is(false));
    }
}
