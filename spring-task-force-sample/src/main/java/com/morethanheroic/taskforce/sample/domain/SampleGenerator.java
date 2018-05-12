package com.morethanheroic.taskforce.sample.domain;

import com.morethanheroic.taskforce.generator.Generator;

import java.util.Optional;

public class SampleGenerator implements Generator {

    private int value = 0;

    @Override
    public Optional<String> generate() {
        value++;

        if (value < 100) {
            return Optional.of(String.valueOf(value));
        }

        return Optional.empty();
    }
}
