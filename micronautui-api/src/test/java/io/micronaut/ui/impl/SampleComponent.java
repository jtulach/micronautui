package io.micronaut.ui.impl;

/*-
 * #%L
 * Micronaut UI API - a library from the Micronaut project
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

import io.micronaut.ui.ObservableUI;
import io.micronaut.context.annotation.Executable;

@ObservableUI
public class SampleComponent {
    public SampleComponent() {
    }

    boolean ok = true;
    private String fine = "ok";
    private final String immutable = "Hi";

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getFine() {
        return fine;
    }

    public void setFine(String fine) {
        this.fine = fine;
    }

    public String getImmutable() {
        return immutable;
    }
    int counter;
    Object ev;

    private void callback(Object ev) {
        counter++;
        this.ev = ev;
    }

    @Executable
    void noArgCallback() {
        counter++;
        this.ev = null;
    }

    @Executable
    void actionCallback(Object ev) {
        counter++;
        this.ev = ev;
    }

    /*
    void actionDataCallback(ActionDataEvent ev) {
    counter += ev.getProperty(Number.class, null).intValue();
    this.ev = ev;
    }
     */
    @Executable
    int notAnAction() {
        return 0;
    }

    static void ignore() {
    }

    @Override
    public boolean equals(Object obj) {
        // MicroHack: Need a way for a bean to find out one of its interceptors
        // MicroHack: Using equals(QueryProto) trick
        return super.equals(obj);
    }
}
