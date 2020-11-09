package io.micronaut.ui.tests;

import io.micronaut.context.annotation.Executable;
import io.micronaut.ui.ObservableUI;

// BEGIN: io.micronaut.ui.demo.BeanInfoCheck#CountingBean
@ObservableUI
public class CountingBean {
    int count;

    @Executable
    public void actionWithEvent(int ev) {
        count += ev;
    }

    @Executable
    public void actionWithoutParameters() {
        count++;
    }

    @Executable
    public void actionWithData(int ev1, int ev2) {
        count += ev1;
        count += ev2;
    }
}
// END: io.micronaut.ui.demo.BeanInfoCheck#CountingBean
