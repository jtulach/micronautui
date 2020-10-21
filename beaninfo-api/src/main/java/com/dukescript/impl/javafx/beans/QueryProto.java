package com.dukescript.impl.javafx.beans;

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
