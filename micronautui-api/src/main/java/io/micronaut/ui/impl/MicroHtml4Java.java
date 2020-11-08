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
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.BeanContext;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.core.beans.BeanProperty;
import io.micronaut.inject.BeanDefinition;
import io.micronaut.inject.ExecutableMethod;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.java.html.BrwsrCtx;
import org.netbeans.html.json.spi.Proto;

final class MicroHtml4Java<T> extends Proto.Type<T> {
    private final BeanIntrospection<T> intro;
    private final List<ExecutableMethod<T, ?>> methods;
    private static final Map<Class<?>,MicroHtml4Java> TYPES = new HashMap<>();

    synchronized static <T> MicroHtml4Java<T> find(BeanContext context, T bean, Class<T> clazz, BeanIntrospection<T> intro) {
        MicroHtml4Java<T> data = TYPES.get(clazz);
        if (data == null) {
            final Collection<BeanProperty<T, Object>> props = intro.getBeanProperties();
            final BeanDefinition<T> def = context.getBeanDefinition(intro.getBeanType());
            final Collection<ExecutableMethod<T, ?>> methods = def.getExecutableMethods();
            data = new MicroHtml4Java(intro, bean.getClass(), clazz, props.size(), methods);
            int i = 0;
            for (BeanProperty<T, Object> p : props) {
                data.registerProperty(p.getName(), i++, p.isReadOnly());
            }
            i = 0;
            for (ExecutableMethod<T, ?> m : methods) {
                data.registerFunction(m.getMethodName(), i++);
            }
        }
        return data;
    }

    MicroHtml4Java(BeanIntrospection<T> intro, Class<? extends T> subclazz, Class<T> clazz, int props, Collection<ExecutableMethod<T, ?>> methods) {
        super(subclazz, clazz, props, methods.size());
        this.intro = intro;
        this.methods = new ArrayList<>(methods);
    }

    @Override
    protected void setValue(T model, int i, Object o) {
        String name = intro.getPropertyNames()[i];
        BeanProperty<T, Object> prop = intro.getProperty(name).get();
        prop.set(model, o);
    }

    @Override
    protected Object getValue(T model, int i) {
        String name = intro.getPropertyNames()[i];
        BeanProperty<T, Object> prop = intro.getProperty(name).get();
        return prop.get(model);
    }

    @Override
    protected void call(T model, int i, Object o, Object o1) throws Exception {
        ExecutableMethod<T, ?> m = methods.get(i);
        switch (m.getArguments().length) {
            case 0:
                m.invoke(model);
                break;
            case 1:
                m.invoke(model, o);
                break;
            default:
                m.invoke(model, o, o1);
                break;
        }
    }

    @Override
    protected T cloneTo(T model, BrwsrCtx bc) {
        T copy = intro.instantiate();
        for (BeanProperty<T, Object> p : intro.getBeanProperties()) {
            Object value = p.get(model);
            p.set(copy, value);
        }
        return copy;
    }

    @Override
    protected T read(BrwsrCtx bc, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void onChange(T model, int i) {
    }

    @Override
    protected Proto protoFor(Object o) {
        return QueryProto.findFor(o);
    }

    final BeanProperty<T, Object> findProperty(MethodInvocationContext<T, Object> context, boolean[] setterGetter) {
        final String methodName = context.getMethodName();
        String expProp;
        if (methodName.startsWith("set")) {
            expProp = methodName.substring(3);
            setterGetter[0] = true;
        } else if (methodName.startsWith("get")) {
            expProp = methodName.substring(3);
            setterGetter[1] = true;
        } else if (methodName.startsWith("is")) {
            expProp = methodName.substring(2);
            setterGetter[1] = true;
        } else {
            return null;
        }
        if (expProp.length() > 0 && Character.isUpperCase(expProp.charAt(0))) {
            expProp = Character.toLowerCase(expProp.charAt(0)) + expProp.substring(1);
        }
        for (BeanProperty<T, Object> p : intro.getBeanProperties()) {
            if (p.getName().equals(expProp)) {
                return p;
            }
        }
        return null;
    }
}
