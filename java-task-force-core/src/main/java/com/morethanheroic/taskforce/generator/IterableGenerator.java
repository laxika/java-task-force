package com.morethanheroic.taskforce.generator;

import java.util.Optional;

/**
 * Generate new work items from the elements provided by an {@link Iterable}.
 *
 * @param <RESULT> the type of the elements
 */
public class IterableGenerator<RESULT> implements Generator<RESULT> {

    private final IteratorGenerator<RESULT> iteratorGenerator;

    private IterableGenerator(final Iterable<RESULT> iterable) {
        iteratorGenerator = IteratorGenerator.of(iterable.iterator());
    }

    public static <WORK_ITEM_TYPE> IterableGenerator<WORK_ITEM_TYPE> of(final Iterable<WORK_ITEM_TYPE> iterator) {
        return new IterableGenerator<>(iterator);
    }

    @Override
    public Optional<RESULT> generate() {
        return iteratorGenerator.generate();
    }
}
