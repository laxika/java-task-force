package com.morethanheroic.taskforce.job.builder.domain;

import com.morethanheroic.taskforce.generator.Generator;
import lombok.Builder;
import lombok.Getter;

/**
 * Contains the data for the first step of the {@link com.morethanheroic.taskforce.job.Job} creation.
 */
@Getter
@Builder
public class GeneratorStageJobContext {

    private final Generator generator;
}
