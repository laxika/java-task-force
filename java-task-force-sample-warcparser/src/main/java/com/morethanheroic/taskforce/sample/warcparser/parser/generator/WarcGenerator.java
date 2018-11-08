package com.morethanheroic.taskforce.sample.warcparser.parser.generator;

import com.mixnode.warcreader.WarcReader;
import com.mixnode.warcreader.record.WarcRecord;
import com.morethanheroic.taskforce.generator.Generator;
import com.morethanheroic.taskforce.sample.warcparser.parser.domain.Record;
import com.morethanheroic.taskforce.sample.warcparser.parser.domain.RecordType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This generator is creating {@link Record} instances form the requests/responses found in a WARC file.
 */
@Slf4j
public class WarcGenerator implements Generator<Record> {

    private final WarcReader warcReader;

    public WarcGenerator(final InputStream inputStream) throws IOException {
        warcReader = new WarcReader(inputStream);
    }

    @Override
    public Optional<Record> generate() {
        try {
            final WarcRecord warcRecord = warcReader.readRecord();

            // No more records to read, terminating the generator
            if (warcRecord == null) {
                return Optional.empty();
            }

            // The records should be parsed immediately and NOT in an async context (because that could mess up the
            // stream reading).
            if (warcRecord.getType() == WarcRecord.WarcType.response) {
                final Map<String, String> headers = parseHeaders(warcRecord);
                final String content = parseResponseContent(warcRecord);

                return Optional.of(
                        Record.builder()
                                .type(RecordType.RESPONSE)
                                .headers(headers)
                                .body(content)
                                .build()
                );
            } else if (warcRecord.getType() == WarcRecord.WarcType.request) {
                final Map<String, String> headers = parseHeaders(warcRecord);

                return Optional.of(
                        Record.builder()
                                .type(RecordType.REQUEST)
                                .headers(headers)
                                .build()
                );
            }

            // Not a WARC record type we are interested in so parsing the next entry and returning it instead.
            return generate();
        } catch (IOException e) {
            log.error("Error happened while parsing document!", e);

            return generate();
        }
    }

    private Map<String, String> parseHeaders(final WarcRecord warcRecord) {
        return Arrays.stream(warcRecord.getWarcHeaders().getAllHeaders())
                .collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));
    }

    private String parseResponseContent(final WarcRecord warcRecord) throws IOException {
        final HttpResponse httpResponse = (HttpResponse) warcRecord.getWarcContentBlock();

        return EntityUtils.toString(httpResponse.getEntity());
    }
}
