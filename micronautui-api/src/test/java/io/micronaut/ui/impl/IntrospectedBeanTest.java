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

import io.micronaut.context.ApplicationContext;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.core.beans.BeanProperty;
import net.java.html.json.Models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class IntrospectedBeanTest {
    @Test
    public void introspectionAccess() throws Exception {
        try (ApplicationContext ac = ApplicationContext.run()) {
            BeanIntrospection<SampleComponent> intro = BeanIntrospection.getIntrospection(SampleComponent.class);

            SampleComponent bean = ac.getBean(SampleComponent.class);
            SampleComponent bean2 = ac.getBean(SampleComponent.class);

            assertTrue("it's ok", bean.ok);
            BeanProperty<SampleComponent, Boolean> okProperty = intro.getProperty("ok", boolean.class).get();
            assertTrue("Read write", okProperty.isReadWrite());

            okProperty.set(bean, false);
            assertFalse("no longer ok", bean.ok);
            
            okProperty.set(bean2, true);
            assertTrue("still ok", bean2.ok);

            final BeanProperty<SampleComponent, Object> immutableProperty = intro.getProperty("immutable").get();
            assertFalse("Read only, not write", immutableProperty.isReadWrite());
            assertTrue("Read only", immutableProperty.isReadOnly());
            assertEquals("Hi", immutableProperty.get(bean));

            bean.noArgCallback();
            
            assertTrue(Models.isModel(bean.getClass()));
        }
    }
}
