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

import java.util.AbstractList;
import java.util.List;
import org.netbeans.html.json.spi.Proto;

final class ObservableList<T> extends AbstractList<T> {
    private final String name;
    private final Proto proto;
    private final List<T> delegate;

    private ObservableList(String name, Proto p, List<T> arr) {
        this.name = name;
        this.proto = p;
        this.delegate = arr;
    }

    static <T> List<T> wrap(String name, Proto p, List<T> res) {
        if (res instanceof ObservableList<?>) {
            ObservableList<?> ol = (ObservableList<?>) res;
            if (ol.proto == p && ol.name.equals(name)) {
                return res;
            }
        }
        return new ObservableList<>(name, p, res);
    }

    @Override
    public T get(int index) {
        notifyAccess();
        return delegate.get(index);
    }

    @Override
    public int size() {
        notifyAccess();
        return delegate.size();
    }

    @Override
    public T set(int index, T element) {
        T r = delegate.set(index, element);
        notifyChange();
        return r;
    }

    @Override
    public void add(int index, T element) {
        delegate.add(index, element);
        notifyChange();
    }

    @Override
    public T remove(int index) {
        T r = delegate.remove(index);
        notifyChange();
        return r;
    }

    private void notifyChange() {
        proto.valueHasMutated(name);
    }

    private void notifyAccess() {
        proto.accessProperty(name);
    }
}
