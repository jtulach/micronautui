package com.dukescript.api.javafx.beans;

import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;

public final class OnChangeInterceptor implements MethodInterceptor<Object, Object> {
    static {
        System.err.println("loaded");
    }
    public OnChangeInterceptor() {
        System.err.println("created");
    }

    @Override
    public Object intercept(MethodInvocationContext<Object, Object> context) {
        System.err.println("called On change: " + this + " ctx: " + context);
        return context.proceed();
    }
}
