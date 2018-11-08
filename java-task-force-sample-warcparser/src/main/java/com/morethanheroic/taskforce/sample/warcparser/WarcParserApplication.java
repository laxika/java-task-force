package com.morethanheroic.taskforce.sample.warcparser;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.morethanheroic.taskforce.executor.JobExecutor;
import com.morethanheroic.taskforce.executor.domain.JobExecutionContext;
import com.morethanheroic.taskforce.job.Job;
import com.morethanheroic.taskforce.job.builder.JobBuilder;
import com.morethanheroic.taskforce.sample.warcparser.parser.domain.ContentType;
import com.morethanheroic.taskforce.sample.warcparser.parser.domain.RecordType;
import com.morethanheroic.taskforce.sample.warcparser.parser.task.RecordContentTypeFilterTask;
import com.morethanheroic.taskforce.sample.warcparser.parser.task.UrlProtocolFilterTask;
import com.morethanheroic.taskforce.sample.warcparser.parser.task.RecordTypeFilterTask;
import com.morethanheroic.taskforce.sample.warcparser.parser.task.WarcUrlParserTask;
import com.morethanheroic.taskforce.sample.warcparser.parser.generator.WarcGenerator;
import com.morethanheroic.taskforce.sample.warcparser.stream.AvailableInputStream;
import com.morethanheroic.taskforce.sink.LoggingSink;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class WarcParserApplication {

    public static void main(final String... args) throws IOException {
        final AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .build();

        final S3Object s3Object = amazonS3.getObject("commoncrawl",
                "crawl-data/CC-MAIN-2018-39/segments/1537267155413.17/warc/CC-MAIN-20180918130631-20180918150631-00009.warc.gz");

        final InputStream warcReader = new AvailableInputStream(
                new BufferedInputStream(s3Object.getObjectContent(), 1048576));

        final Job parserJob = JobBuilder.newBuilder()
                .generator(new WarcGenerator(warcReader))
                .task(new RecordTypeFilterTask(RecordType.RESPONSE))
                .task(new RecordContentTypeFilterTask(ContentType.TEXT_HTML, ContentType.APPLICATION_HTML, ContentType.APPLICATION_HTTP))
                .asyncTask(new WarcUrlParserTask(), 30, 1000)
                .task(new UrlProtocolFilterTask("mailto"))
                .sink(LoggingSink.of("Logged url: {}"))
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
