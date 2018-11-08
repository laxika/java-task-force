package com.morethanheroic.taskforce.sample.warcparser.parser.task;

import com.morethanheroic.taskforce.task.Task;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UrlProtocolFilterTask implements Task<List<URL>, List<URL>> {

    private final List<String> allowedProtocols;

    public UrlProtocolFilterTask(final String... allowedProtocols) {
        this.allowedProtocols = Arrays.asList(allowedProtocols);
    }

    @Override
    public Optional<List<URL>> execute(final List<URL> urls) {
        return Optional.of(
                urls.stream()
                        .filter(url -> allowedProtocols.contains(url.getProtocol()))
                        .collect(Collectors.toList())
        );
    }
}
