package com.morethanheroic.taskforce.generator;

import java.util.Optional;
import java.util.stream.BaseStream;
import java.util.stream.Stream;

/**
 * Generate new work items from the elements provided by a {@link Stream}.
 *
 * @param <RESULT> the type of the elements
 */
public class StreamGenerator<RESULT> implements Generator<RESULT> {

    private final IteratorGenerator<RESULT> iteratorGenerator;

    private StreamGenerator(final BaseStream<RESULT, ?> stream) {
        iteratorGenerator = IteratorGenerator.of(stream.iterator());
    }

    public static <WORK_ITEM_TYPE> StreamGenerator<WORK_ITEM_TYPE> of(final BaseStream<WORK_ITEM_TYPE, ?> stream) {
        return new StreamGenerator<>(stream);
    }

    @Override
    public Optional<RESULT> generate() {
        return iteratorGenerator.generate();
    }
}
