package com.dukescript.impl.javafx.beans;

import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.core.beans.BeanProperty;
import javax.inject.Inject;
import net.java.html.BrwsrCtx;
import org.netbeans.html.json.spi.Proto;

@Prototype
public final class ObservableInterceptor<T> implements MethodInterceptor<T, Object> {
    Proto proto;
    MicroHtml4Java<T> micro;
    @Inject
    private BeanContext context;
    
    public ObservableInterceptor() {
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
        return res;
    }
}
