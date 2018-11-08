package com.morethanheroic.taskforce.sample.warcparser.parser;

import com.morethanheroic.taskforce.task.Task;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class WarcUrlParserTask implements Task<Document, List<String>> {

    @Override
    public Optional<List<String>> execute(final Document document) {
        log.info("Parsing document: " + document.location());

        final List<String> urls = document.select("a").stream()
                .map(tag -> tag.attr("abs:href"))
                .filter(string -> !string.isEmpty())
                .collect(Collectors.toList());

        return Optional.of(urls);
    }
}
