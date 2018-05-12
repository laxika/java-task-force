package com.morethanheroic.taskforce.sample.domain;

import com.morethanheroic.taskforce.generator.Generator;

import java.util.Optional;
import java.util.UUID;

public class SampleGenerator implements Generator {

    private int value = 0;

    @Override
    public Optional<String> generate() {
        value++;

        if (value < 100) {
            return Optional.of(UUID.randomUUID().toString());
        }

        return Optional.empty();
    }
}
