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

import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.core.beans.BeanProperty;
import java.util.List;
import net.java.html.BrwsrCtx;
import org.netbeans.html.json.spi.Proto;

@Prototype
public final class ObservableInterceptor<T> implements MethodInterceptor<T, Object> {
    Proto proto;
    MicroHtml4Java<T> micro;
    private final BeanContext context;

    public ObservableInterceptor(BeanContext context) {
        this.context = context;
    }

    private Proto proto(Class<T> type, T bean) {
        if (proto == null) {
            BeanIntrospection<T> intro = BeanIntrospection.getIntrospection(type);
            micro = MicroHtml4Java.find(context, bean, type, intro);
            proto = micro.createProto(bean, BrwsrCtx.findDefault(type));
        }
        return proto;
    }

    @Override
    public Object intercept(MethodInvocationContext<T, Object> context) {
        QueryProto qp = QueryProto.handleEqualsQuery(context);
        if (qp != null) {
            qp.assignProto(proto);
            return true;
        }
        boolean[] setterGetter = { false, false };
        final Proto proto1 = proto(context.getDeclaringType(), context.getTarget());
        BeanProperty<? extends Object, Object> prop = micro.findProperty(context, setterGetter);
        if (setterGetter[1]) {
            proto1.accessProperty(prop.getName());
            proto1.acquireLock(prop.getName());
        }
        Object res = context.proceed();
        if (setterGetter[1]) {
            proto1.releaseLock();
        }
        if (setterGetter[0]) {
            proto1.valueHasMutated(prop.getName());
        }

        if (List.class.equals(context.getReturnType().getType())) {
            return ObservableList.wrap(prop.getName(), proto1, (List<?>) res);
        }

        return res;
    }
}
