package com.morethanheroic.taskforce.generator;

import org.junit.Test;

import java.util.Enumeration;
import java.util.Optional;
import java.util.Vector;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class EnumerationGeneratorTest {

    @Test
    public void testGenerate() {
        final Vector<Integer> vector = new Vector<>();
        vector.add(10);
        final Enumeration<Integer> integerStream = vector.elements();
        final EnumerationGenerator<Integer> streamGenerator = EnumerationGenerator.of(integerStream);

        final Optional<Integer> firstResult = streamGenerator.generate();
        assertThat(firstResult.isPresent(), is(true));
        assertThat(firstResult.get(), is(10));

        final Optional<Integer> secondResult = streamGenerator.generate();
        assertThat(secondResult.isPresent(), is(false));
    }
}
