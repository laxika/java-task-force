package com.morethanheroic.taskforce.sample.warcparser.parser;

import com.mixnode.warcreader.WarcReader;
import com.mixnode.warcreader.record.WarcRecord;
import com.morethanheroic.taskforce.generator.Generator;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class WarcGenerator implements Generator<WarcRecord> {

    private final WarcReader warcReader;

    @Override
    public Optional<WarcRecord> generate() {
        try {
            return Optional.ofNullable(warcReader.readRecord());
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
