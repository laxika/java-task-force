package com.morethanheroic.taskforce.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AsyncTask<INPUT, OUTPUT> implements Task<INPUT, OUTPUT> {

    @Getter
    private final int parallelism;
}
