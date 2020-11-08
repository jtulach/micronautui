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

import io.micronaut.aop.MethodInvocationContext;
import java.lang.reflect.Method;
import org.netbeans.html.json.spi.Proto;

final class QueryProto {
    private Proto found;
    
    void assignProto(Proto p) {
        found = p;
    }
    
    static Proto findFor(Object obj) {
        QueryProto q = new QueryProto();
        if (obj.equals(q)) {
            return q.found;
        } else {
            return null;
        }
    }
    
    static <T> QueryProto handleEqualsQuery(MethodInvocationContext<T, Object> context) {
        if (!"equals".equals(context.getMethodName())) {
            return null;
        }
        final Method m = context.getTargetMethod();
        if (m.getParameterCount() != 1) {
            return null;
        }
        if (m.getParameterTypes()[0] != Object.class) {
            return null;
        }
        if (m.getReturnType() != boolean.class) {
            return null;
        }
        final Object arg0 = context.getParameterValues()[0];
        if (arg0 instanceof QueryProto) {
            return (QueryProto) arg0;
        }
        return null;
    }
}
