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
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.ui.ObservableUI;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;
import net.java.html.json.Models;
import static net.java.html.json.Models.applyBindings;

@ObservableUI
public class Demo {
    private final BeanContext ctx;
    private final TodoClient client;

    Demo(BeanContext ctx, TodoClient client) {
        this.ctx = ctx;
        this.client = client;
    }

    private String desc = "Buy Milk";
    private List<Item> todos = Models.asList();
    private Show show = Show.ALL;

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

    public List<Item> getVisibleTodos() {
        // XXX: has to call getTodos() and not use todos directly
        List<Item> arr = new ArrayList<>(getTodos());
        switch (getShow()) {
            case DONE:
                arr.removeIf((item) -> !item.isDone());
                break;
            case PENDING:
                arr.removeIf((item) -> item.isDone());
                break;
        }
        return arr;
    }

    @Executable
    public void cleanUp() {
        getTodos().removeIf((item) -> item.isDone());
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
        setDesc("");
    }

    @Executable
    void readTodos() {
        Disposable[] subscribe = { null };
        subscribe[0] = client.items().subscribe((newItems) -> {
            getTodos().clear();
            getTodos().addAll(newItems);
            subscribe[0].dispose();
        });
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    @Client("http://localhost:8080/todos/")
    public interface TodoClient {
        @Get
        Flowable<List<Item>> items();
    }

    @Executable
    void showAll() {
        setShow(Show.ALL);
    }

    @Executable
    void showDone() {
        setShow(Show.DONE);
    }

    @Executable
    void showPending() {
        setShow(Show.PENDING);
    }

    public boolean isActiveAll() {
        // XXX: Have to call getShow() and not use show directly
        return getShow() == Show.ALL;
    }

    public boolean isActivePending() {
        return getShow() == Show.PENDING;
    }

    public boolean isActiveDone() {
        return getShow() == Show.DONE;
    }

    public static void onPageLoad() {
        ApplicationContext ac = ApplicationContext.run();
        Demo ui = ac.getBean(Demo.class);
        applyBindings(ui);
    }

    public enum Show {
        ALL, PENDING, DONE
    }
}
