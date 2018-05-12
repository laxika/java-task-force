package com.morethanheroic.taskforce.task;

@FunctionalInterface
public interface Task<INPUT, OUTPUT> {

    OUTPUT execute(INPUT input);
}
