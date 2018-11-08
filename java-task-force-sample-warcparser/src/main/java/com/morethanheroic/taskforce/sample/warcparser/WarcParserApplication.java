package com.morethanheroic.taskforce.sample.warcparser;

import com.morethanheroic.taskforce.executor.JobExecutor;
import com.morethanheroic.taskforce.executor.domain.JobExecutionContext;
import com.morethanheroic.taskforce.job.Job;
import com.morethanheroic.taskforce.job.builder.JobBuilder;
import com.morethanheroic.taskforce.sample.warcparser.parser.domain.ContentType;
import com.morethanheroic.taskforce.sample.warcparser.parser.domain.RecordType;
import com.morethanheroic.taskforce.sample.warcparser.parser.generator.WarcGenerator;
import com.morethanheroic.taskforce.sample.warcparser.parser.stream.WarcStreamFactory;
import com.morethanheroic.taskforce.sample.warcparser.parser.task.RecordContentTypeFilterTask;
import com.morethanheroic.taskforce.sample.warcparser.parser.task.RecordTypeFilterTask;
import com.morethanheroic.taskforce.sample.warcparser.parser.task.UrlProtocolFilterTask;
import com.morethanheroic.taskforce.sample.warcparser.parser.task.WarcUrlParserTask;
import com.morethanheroic.taskforce.sink.LoggingSink;

import java.io.IOException;

public class WarcParserApplication {

    public static void main(final String... args) throws IOException {
        final WarcStreamFactory warcStreamFactory = new WarcStreamFactory();

        final Job parserJob = JobBuilder.newBuilder()
                .generator(new WarcGenerator(warcStreamFactory.newWarcStream()))
                .task(new RecordTypeFilterTask(RecordType.RESPONSE))
                .task(new RecordContentTypeFilterTask(
                        ContentType.TEXT_HTML,
                        ContentType.APPLICATION_HTML,
                        ContentType.APPLICATION_HTTP
                ))
                .asyncTask(new WarcUrlParserTask(), 50, 1000)
                .task(new UrlProtocolFilterTask("mailto"))
                .sink(LoggingSink.of("Logged url: {}", false))
                .build();

        final JobExecutor jobExecutor = new JobExecutor();
        jobExecutor.execute(
                JobExecutionContext.builder()
                        .preparedTaskCount(1000)
                        .build(),
                parserJob
        );
    }
}
