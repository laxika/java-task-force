package com.morethanheroic.taskforce.sink;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * A sink implementation that discards (does nothing with) every work item.
 *
 * @param <INPUT> the type of the work item
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DiscardingSink<INPUT> implements Sink<INPUT> {

    public static <WORK_ITEM_TYPE> DiscardingSink<WORK_ITEM_TYPE> of() {
        return new DiscardingSink<>();
    }

    @Override
    public void consume(INPUT input) {

    }
}
