package com.morethanheroic.taskforce.job.builder;

import com.morethanheroic.taskforce.generator.Generator;
import com.morethanheroic.taskforce.job.builder.domain.GeneratorStageJobContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Initialize the creation of a new {@link com.morethanheroic.taskforce.job.Job}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JobBuilder {

    /**
     * Creates a new builder.
     *
     * @return the new builder to build a job with
     */
    public static JobBuilder newBuilder() {
        return new JobBuilder();
    }

    /**
     * Adds a {@link Generator} to the {@link com.morethanheroic.taskforce.job.Job}.
     *
     * Returns a {@link JobTaskPhaseBuilder} to ban the addition of other creators.
     *
     * @param generator the generator to add to the job
     * @param <RESULT> the return type of the generator
     * @return the second phase of the building of a job
     */
    public <RESULT> JobTaskPhaseBuilder<RESULT> generator(final Generator<RESULT> generator) {
        return new JobTaskPhaseBuilder<>(
                GeneratorStageJobContext.builder()
                        .generator(generator)
                        .build()
        );
    }
}
