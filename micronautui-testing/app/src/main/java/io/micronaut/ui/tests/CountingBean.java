package io.micronaut.ui.tests;

/*-
 * #%L
 * MicronautUI - a library from the Micronaut project
 * %%
 * Copyright (C) 2020 OracleLabs
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import io.micronaut.context.annotation.Executable;
import io.micronaut.ui.Observable;

// BEGIN: io.micronaut.ui.demo.BeanInfoCheck#CountingBean
@Observable.UI
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
