package com.morethanheroic.taskforce.sample.warcparser.parser.domain;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
public enum ContentType {

    TEXT_HTML("text/html"),
    APPLICATION_HTML("application/html"),
    // The web is full of strange (and probably invalid) content types.
    APPLICATION_HTTP("application/http");

    private final String mimeType;

    public static Optional<ContentType> getByMimeType(final String mimeType) {
        return Arrays.stream(ContentType.values())
                .filter(contentType -> contentType.mimeType.equals(mimeType))
                .findFirst();
    }
}
