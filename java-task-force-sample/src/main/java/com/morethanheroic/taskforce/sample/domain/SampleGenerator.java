package com.morethanheroic.taskforce.sample.domain;

import com.morethanheroic.taskforce.generator.Generator;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class SampleGenerator implements Generator<String> {

    private int value = 0;

    @Override
    public Optional<String> generate() {
        value++;

        if (value < 50) {
            log.info("Adding value: " + value + " to the working set.");

            return Optional.of(String.valueOf(value));
        }

        return Optional.empty();
    }
}
