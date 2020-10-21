package com.dukescript.impl.javafx.beans;

import com.dukescript.api.javafx.beans.SampleComponent;
import io.micronaut.context.ApplicationContext;
import org.junit.Test;
import static org.junit.Assert.*;
import org.netbeans.html.json.spi.Proto;

public class QueryProtoTest {
    @Test
    public void findProtoForABean() {
        try (ApplicationContext ctx = ApplicationContext.run()) {
            SampleComponent c = ctx.getBean(SampleComponent.class);
            c.setFine("Great!");
            
            Proto p = QueryProto.findFor(c);
            assertNotNull("Proto for c found", p);
        }
    }
    
}
