package com.morethanheroic.taskforce.sample.warcparser.parser.stream;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;

import java.io.BufferedInputStream;
import java.io.InputStream;

public class WarcStreamFactory {

    // For production use a more robust stream creation logic should be provided.
    public InputStream newWarcStream() {
        final AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .build();

        final S3Object s3Object = amazonS3.getObject("commoncrawl",
                "crawl-data/CC-MAIN-2018-39/segments/1537267155413.17/warc/CC-MAIN-20180918130631-20180918150631-00009.warc.gz");

        return new AvailableInputStream(new BufferedInputStream(s3Object.getObjectContent(), 1048576));
    }
}
