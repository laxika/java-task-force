package com.morethanheroic.taskforce.task;

import java.util.Optional;

@FunctionalInterface
public interface Task<INPUT, OUTPUT> {

    Optional<OUTPUT> execute(INPUT input);
}
