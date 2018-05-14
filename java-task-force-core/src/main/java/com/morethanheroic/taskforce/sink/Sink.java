package com.morethanheroic.taskforce.sink;

public interface Sink<INPUT> {

    void consume(INPUT input);
}
