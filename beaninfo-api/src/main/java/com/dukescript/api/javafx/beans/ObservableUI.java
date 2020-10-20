package com.dukescript.api.javafx.beans;

import com.dukescript.impl.javafx.beans.ObservableInterceptor;
import io.micronaut.aop.Around;
import io.micronaut.context.annotation.Type;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Around
@Type(ObservableInterceptor.class)
public @interface ObservableUI {
}
