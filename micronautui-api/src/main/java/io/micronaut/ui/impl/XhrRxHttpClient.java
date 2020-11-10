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

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.RxHttpClient;
import io.reactivex.Flowable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.java.html.js.JavaScriptBody;
import org.reactivestreams.Subscriber;

public final class XhrRxHttpClient implements RxHttpClient {
    private final XhrConnection conn;

    XhrRxHttpClient(XhrConnection connection) {
        this.conn = connection;
    }

    private final class Response<I, O, E> extends Flowable<HttpResponse<O>> {
        private final List<Subscriber<? super HttpResponse<O>>> all = new CopyOnWriteArrayList<>();
        private boolean initialized;
        private final HttpRequest<I> request;
        private final Argument<O> bodyType;
        private final Argument<E> errorType;

        Response(HttpRequest<I> request, Argument<O> bodyType, Argument<E> errorType) {
            this.request = request;
            this.bodyType = bodyType;
            this.errorType = errorType;
        }

        @Override
        protected void subscribeActual(Subscriber<? super HttpResponse<O>> s) {
            all.add(s);
            if (!initialized) {
                initialized = true;
                try {
                    URI uri = new URI(conn.id).resolve(request.getPath());
                    loadJSON(uri.toString(), this, request.getMethodName(), null, new String[0]);
                } catch (URISyntaxException ex) {
                    onError(ex);
                }
            }
        }

        void onError(Throwable t) {
            for (Subscriber<? super HttpResponse<O>> s : all) {
                s.onError(t);
            }
        }

        void onNext(MutableHttpResponse<O> response) {
            for (Subscriber<? super HttpResponse<O>> s : all) {
                s.onNext(response);
            }
        }
    }

    @Override
    public <I, O, E> Flowable<HttpResponse<O>> exchange(HttpRequest<I> request, Argument<O> bodyType, Argument<E> errorType) {
        return new Response<>(request, bodyType, errorType);
    }

    @JavaScriptBody(args = {"url", "done", "method", "data", "hp"}, javacall = true, body = ""
        + "var request = new XMLHttpRequest();\n"
        + "if (!method) method = 'GET';\n"
        + "request.open(method, url, true);\n"
        + "request.setRequestHeader('Content-Type', 'application/json; charset=utf-8');\n"
        + "for (var i = 0; i < hp.length; i += 2) {\n"
        + "  var h = hp[i];\n"
        + "  var v = hp[i + 1];\n"
        + "  request.setRequestHeader(h, v);\n"
        + "}\n"
        + "request.onreadystatechange = function() {\n"
        + "  if (request.readyState !== 4) return;\n"
        + "  var r = request.response || request.responseText;\n"
        + "  try {\n"
        + "    var str = r;\n"
        + "    if (request.status !== 0)\n"
        + "      if (request.status < 100 || request.status >= 400) throw request.status + ': ' + request.statusText;"
        + "    try { r = eval('(' + r + ')'); } catch (ignore) { }"
        + "    @io.micronaut.ui.impl.XhrRxHttpClient::notifySuccess(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)(done, str, r);\n"
        + "  } catch (error) {;\n"
        + "    @io.micronaut.ui.impl.XhrRxHttpClient::notifyError(Ljava/lang/Object;Ljava/lang/Object;)(done, error);\n"
        + "  }\n"
        + "};\n"
        + "request.onerror = function (e) {\n"
        + "  @io.micronaut.ui.impl.XhrRxHttpClient::notifyError(Ljava/lang/Object;Ljava/lang/Object;)(done, e.type + ' status ' + request.status);\n"
        + "};\n"
        + "if (data) request.send(data);\n"
        + "else request.send();\n"
    )
    native static <O> void loadJSON(
        String url, Response done, String method, String data, Object[] hp
    );

    static void notifySuccess(Object done, Object a2, Object a3) {
        Response r = (Response) done;
        r.onNext(HttpResponse.created(a2.toString()));
    }
    static void notifyError(Object done, Object a2) {
        Response r = (Response) done;
        r.onError(new IOException(a2.toString()));
    }

    @Override
    public BlockingHttpClient toBlocking() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRunning() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
