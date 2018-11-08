package com.morethanheroic.taskforce.sample.warcparser.parser.generator;

import com.mixnode.warcreader.WarcReader;
import com.mixnode.warcreader.record.WarcRecord;
import com.morethanheroic.taskforce.generator.Generator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Optional;

/**
 * This generator is creating JSoup {@link Document} instances form the responses found in a WARC file. Splitting the
 * {@link WarcRecord} creation, the filtering of the valid records and the document building from a record is tempting
 * but impossible to do without creating a context object because the whole record should be read immediately (and
 * synchronously).
 */
@Slf4j
@RequiredArgsConstructor
public class WarcGenerator implements Generator<Document> {

    private final WarcReader warcReader;

    @Override
    public Optional<Document> generate() {
        try {
            final WarcRecord warcRecord = warcReader.readRecord();

            // No more records to read, terminating the generator
            if (warcRecord == null) {
                return Optional.empty();
            }

            // Valid WARC record
            if (warcRecord.getType() != WarcRecord.WarcType.response) {
                return generate();
            }

            // Valid response
            final HttpResponse httpResponse = (HttpResponse) warcRecord.getWarcContentBlock();
            final String contentType = httpResponse.containsHeader("Content-Type") ?
                    httpResponse.getFirstHeader("Content-Type").getValue() : null;

            if (contentType == null || ContentType.getByMimeType(contentType) == null ||
                    !ContentType.getByMimeType(contentType).equals(ContentType.TEXT_HTML)) {
                return generate();
            }

            final String location = httpResponse.containsHeader("Content-Location") ?
                    httpResponse.getFirstHeader("Content-Location").getValue() : "";
            final String content = EntityUtils.toString(httpResponse.getEntity());

            return Optional.of(Jsoup.parse(content, location));
        } catch (IOException e) {
            log.error("Error happened while parsing document!", e);

            return generate();
        }
    }
}
