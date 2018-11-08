package com.morethanheroic.taskforce.sample.warcparser;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.mixnode.warcreader.WarcReader;
import com.mixnode.warcreader.record.WarcRecord;
import com.morethanheroic.taskforce.executor.JobExecutor;
import com.morethanheroic.taskforce.job.Job;
import com.morethanheroic.taskforce.job.builder.JobBuilder;
import com.morethanheroic.taskforce.sample.warcparser.parser.WarcGenerator;
import com.morethanheroic.taskforce.sample.warcparser.parser.WarcSink;
import com.morethanheroic.taskforce.sample.warcparser.parser.WarcTypeFilterTask;

import java.io.IOException;

public class WarcParserApplication {

    public static void main(final String... args) throws IOException {
        final AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .build();

        final S3Object s3Object = amazonS3.getObject("commoncrawl",
                "crawl-data/CC-MAIN-2018-43/segments/1539583508988.18/warc/CC-MAIN-20181015080248-20181015101748-00000.warc.gz");

        WarcReader warcReader = new WarcReader(s3Object.getObjectContent());

        final Job parserJob = JobBuilder.newBuilder()
                .generator(new WarcGenerator(warcReader))
                .asyncTask(new WarcTypeFilterTask(WarcRecord.WarcType.response), 10, 300)
                .sink(new WarcSink())
                .build();

        final JobExecutor jobExecutor = new JobExecutor();
        jobExecutor.execute(parserJob);
    }
}
