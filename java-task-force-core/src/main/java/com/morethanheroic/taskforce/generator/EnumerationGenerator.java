package com.morethanheroic.taskforce.generator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Enumeration;
import java.util.Optional;

/**
 * Generate new work items from the elements provided by an {@link java.util.Enumeration}.
 *
 * @param <RESULT> the type of the elements
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumerationGenerator<RESULT> implements Generator<RESULT> {

    private final Enumeration<RESULT> enumeration;

    public static <WORK_ITEM_TYPE> EnumerationGenerator<WORK_ITEM_TYPE> of(final Enumeration<WORK_ITEM_TYPE> enumeration) {
        return new EnumerationGenerator<>(enumeration);
    }

    @Override
    public Optional<RESULT> generate() {
        return enumeration.hasMoreElements() ? Optional.of(enumeration.nextElement()) : Optional.empty();
    }
}
