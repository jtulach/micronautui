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
import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.java.html.junit.BrowserRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(BrowserRunner.class)
public class XhrRxHttpClientTest {
    @Singleton
    public static class Data {
        @Inject
        GitClient client;
    }

    @Test
    public Runnable[] basicClientOp() {
        try (ApplicationContext ctx = ApplicationContext.build().start()) {
            Data data = ctx.getBean(Data.class);
            assertNotNull(data);
            assertNotNull("Client found", data.client);

            String[] arr = { null };
            int[] cnt = { 1 };
            Flowable<String> msg = data.client.zen();
            Disposable dispose = msg.subscribe((t) -> {
                arr[0] = t;
            });
            assertNotNull(dispose);

            Runnable[] checks = new Runnable[50];
            Runnable check = () -> {
                if (arr[0] == null && ++cnt[0] < checks.length) {
                    return;
                }
                System.err.println("zen: " + arr[0]);
                assertNotNull("zen provided", arr[0]);
                assertNotEquals("non-empty zen provided", 0, arr[0].length());
            };
            for (int i = 0; i < checks.length; i++) {
                checks[i] = check;
            }
            return checks;
        }
    }

    @Test
    public Runnable[] obtainUserObject() {
        try (ApplicationContext ctx = ApplicationContext.build().start()) {
            Data data = ctx.getBean(Data.class);
            assertNotNull(data);
            assertNotNull("Client found", data.client);

            User[] arr = { null };
            int[] cnt = { 1 };
            Flowable<User> msg = data.client.user("defunct");
            Disposable dispose = msg.subscribe((t) -> {
                arr[0] = t;
            });
            assertNotNull(dispose);

            Runnable[] checks = new Runnable[50];
            Runnable check = () -> {
                if (arr[0] == null && ++cnt[0] < checks.length) {
                    return;
                }
                System.err.println("user: " + arr[0]);
                assertNotNull("user provided", arr[0]);
                assertEquals("login provided", "defunct", arr[0].getLogin());
            };
            for (int i = 0; i < checks.length; i++) {
                checks[i] = check;
            }
            return checks;
        }
    }

    @Client("https://api.github.com/")
    public interface GitClient {
        @Get("/zen")
        public Flowable<String> zen();

        @Get("/users/{name}")
        public Flowable<User> user(String name);
    }

    @Introspected
    public static class User {
        private String login;
        private int id;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "User{" + "login=" + login + ", id=" + id + '}';
        }
    }
}
