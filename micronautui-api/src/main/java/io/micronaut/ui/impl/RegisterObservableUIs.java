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

import io.micronaut.context.BeanContext;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.inject.BeanDefinition;
import io.micronaut.inject.qualifiers.Qualifiers;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.ui.ObservableUI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Singleton;

@Singleton
public class RegisterObservableUIs {
    private final BeanContext ctx;
    private final Map<Class<?>, MicroHtml4Java> TYPES = new HashMap<>();

    RegisterObservableUIs(BeanContext beanContext) {
        this.ctx = beanContext;
    }

    private synchronized <T> MicroHtml4Java<T> register(BeanContext context, BeanDefinition<T> def) {
        Class<T> subClass = def.getBeanType();
        BeanIntrospection intro = BeanIntrospection.getIntrospection(subClass.getSuperclass());
        return new MicroHtml4Java(intro, subClass, def);
    }

    final <T> MicroHtml4Java<T> find(Class<T> type) {
        MicroHtml4Java micro = TYPES.get(type);
        assert micro != null;
        return micro;
    }

    @EventListener
    synchronized void init(StartupEvent event) {
        Collection<BeanDefinition<?>> beanDefinitions = ctx.getBeanDefinitions(Qualifiers.byStereotype(ObservableUI.class));
        for (BeanDefinition<?> def : beanDefinitions) {
            MicroHtml4Java<?> micro = register(ctx, def);
            TYPES.put(def.getBeanType(), micro);
        }
    }
}
