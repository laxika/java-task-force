package com.morethanheroic.taskforce.sample.warcparser;

import com.morethanheroic.taskforce.executor.JobExecutor;
import com.morethanheroic.taskforce.executor.domain.JobExecutionContext;
import com.morethanheroic.taskforce.executor.pool.TaskExecutor;
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
import com.morethanheroic.taskforce.sink.DiscardingSink;
import com.morethanheroic.taskforce.sink.LoggingSink;
import com.morethanheroic.taskforce.task.domain.TaskContext;

import java.io.IOException;

public class WarcParserApplication {

    public static void main(final String... args) throws IOException {
        final WarcStreamFactory warcStreamFactory = new WarcStreamFactory();

        final Job parserJob = JobBuilder.newBuilder()
                .generator(new WarcGenerator(warcStreamFactory.newWarcStream()))
                .task("record-type-filter", new RecordTypeFilterTask(RecordType.RESPONSE),
                        TaskContext.builder()
                                .statisticsCollectionEnabled(true)
                                .statisticsReportingEnabled(true)
                                .build()
                )
                .task("record-content-type-filter", new RecordContentTypeFilterTask(
                        ContentType.TEXT_HTML,
                        ContentType.APPLICATION_HTML,
                        ContentType.APPLICATION_HTTP
                ))
                .task("url-parser-task", new WarcUrlParserTask(),
                        TaskContext.builder()
                                .statisticsCollectionEnabled(true)
                                .statisticsReportingEnabled(true)
                                .executor(TaskExecutor.compute())
                                .build()
                )
                .task("url-protocol-filter", new UrlProtocolFilterTask("mailto"))
                .sink(LoggingSink.of("Logged url: {}", false))
                .build();

        final JobExecutor jobExecutor = new JobExecutor();
        jobExecutor.execute(
                JobExecutionContext.builder()
                        .preparedTaskCount(10000)
                        .build(),
                parserJob
        );
    }
}
