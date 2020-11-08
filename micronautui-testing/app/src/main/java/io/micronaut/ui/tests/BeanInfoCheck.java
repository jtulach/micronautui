package io.micronaut.ui.tests;

/*-
 * #%L
 * MicronautUI - a library from the Micronaut project
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

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Executable;
import io.micronaut.inject.BeanDefinition;
import io.micronaut.inject.ExecutableMethod;
import io.micronaut.ui.ObservableUI;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Test;

public class BeanInfoCheck {
    static
    // BEGIN: io.micronaut.ui.demo.BeanInfoCheck#CountingBean
    @ObservableUI
    public class CountingBean {
        private int count;

        @Executable
        public void actionWithEvent(int ev) {
            count += ev;
        }
        
        @Executable
        public void actionWithoutParameters() {
            count++;
        }
        
        @Executable()
        public void actionWithData(int ev1, int ev2) {
            count += ev1;
            count += ev2;
        }
    }
    // END: io.micronaut.ui.demo.BeanInfoCheck#CountingBean

    @Test
    public void beanInfoCanHaveActions() {
        try (ApplicationContext ctx = ApplicationContext.run()) {
            CountingBean bean = new CountingBean();

            BeanDefinition<? extends CountingBean> info = ctx.getBeanDefinition(bean.getClass());

            assertNotNull("Bean info created", info);
            assertNotNull("It has actions", info.getExecutableMethods());
            assertEquals("Three actions found", 3, info.getExecutableMethods().size());

            ExecutableMethod incrementByOne = find(info, "actionWithoutParameters");
            incrementByOne.invoke(bean);
            assertEquals("Action was called", 1, bean.count);

            ExecutableMethod addSource = find(info, "actionWithEvent");
            addSource.invoke(bean, 4);
            assertEquals("Action was called", 5, bean.count);
            ExecutableMethod actionWithData = find(info, "actionWithData");
            actionWithData.invoke(bean, 15, 5);
            assertEquals("Action was called", 25, bean.count);
        }
    }
    
    private static <D> ExecutableMethod<? extends D, ?> find(BeanDefinition<D> def, String n) {
        for (var i : def.getExecutableMethods()) {
            if (i.getName().equals(n)) {
                return i;
            }
        }
        fail("Method " + n + " not found in " + def);
        return null;
    }
}
