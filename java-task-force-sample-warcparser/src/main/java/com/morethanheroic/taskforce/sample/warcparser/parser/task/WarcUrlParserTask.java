package com.morethanheroic.taskforce.sample.warcparser.parser.task;

import com.morethanheroic.taskforce.task.Task;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class WarcUrlParserTask implements Task<Document, List<URL>> {

    @Override
    public Optional<List<URL>> execute(final Document document) {
        final List<URL> urls = document.select("a").stream()
                .map(tag -> tag.attr("abs:href"))
                .filter(string -> !string.isEmpty())
                .map(url -> {
                    try {
                        return new URL(url);
                    } catch (MalformedURLException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return Optional.of(urls);
    }
}
