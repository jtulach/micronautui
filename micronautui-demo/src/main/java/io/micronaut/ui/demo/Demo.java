package io.micronaut.ui.demo;

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
import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Executable;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.ui.ObservableUI;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import java.util.List;
import javax.inject.Inject;
import net.java.html.json.Models;
import static net.java.html.json.Models.applyBindings;

@ObservableUI
@Introspected
public class Demo {
    @Inject
    private BeanContext ctx;
    @Inject
    private TodoClient client;

    private String desc;
    private List<Item> todos = Models.asList();

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isEmptyDesc() {
        return getDesc() == null || getDesc().length() == 0;
    }

    public List<Item> getTodos() {
        return todos;
    }

    public void setTodos(List<Item> todos) {
        this.todos = todos;
    }

    public int getNumTodos() {
        return getTodos().size();
    }

    @Override
    public boolean equals(Object obj) {
        // XXX: each @ObservableUI has to override equals right now
        // XXX: so it is possible to associate a proto field
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Executable
    void addTodo() {
        final Item item = ctx.createBean(Item.class);
        item.setTitle(desc);
        item.setDone(false);
        getTodos().add(item);
        // XXX: ugly refresh...
        setTodos(getTodos());
        setDesc("");
    }

    @Executable
    void readTodos() {
        Disposable[] subscribe = { null };
        subscribe[0] = client.items().subscribe((newItems) -> {
            getTodos().clear();
            getTodos().addAll(newItems);
            // XXX: ugly refresh...
            setTodos(getTodos());
            subscribe[0].dispose();
        });
    }

    @Client("http://localhost:8080/todos/")
    public interface TodoClient {
        @Get
        Flowable<List<Item>> items();
    }

    public static void onPageLoad() {
        ApplicationContext ac = ApplicationContext.run();
        Demo ui = ac.getBean(Demo.class);
         // XXX: Only calling a setter registers the class as a UI model
        ui.setDesc("Buy Milk");
        applyBindings(ui);
    }
}
