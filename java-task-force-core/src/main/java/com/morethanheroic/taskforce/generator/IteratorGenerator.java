package com.morethanheroic.taskforce.generator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.Optional;

/**
 * Generate new work items from the elements provided by an {@link Iterator}.
 *
 * @param <RESULT> the type of the elements
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class IteratorGenerator<RESULT> implements Generator<RESULT> {

    private final Iterator<RESULT> iterator;

    public static <WORK_ITEM_TYPE> IteratorGenerator<WORK_ITEM_TYPE> of(final Iterator<WORK_ITEM_TYPE> iterator) {
        return new IteratorGenerator<>(iterator);
    }

    @Override
    public Optional<RESULT> generate() {
        return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.empty();
    }
}
