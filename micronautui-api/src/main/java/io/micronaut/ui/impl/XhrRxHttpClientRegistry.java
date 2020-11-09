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

import io.micronaut.context.annotation.Factory;
import io.micronaut.core.annotation.AnnotationMetadata;
import io.micronaut.http.HttpVersion;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.RxHttpClientRegistry;

@Factory
public class XhrRxHttpClientRegistry implements RxHttpClientRegistry {
    @Override
    public RxHttpClient getClient(HttpVersion httpVersion, String clientId, String path) {
        XhrConnection key = new XhrConnection(clientId, path, httpVersion);
        return new XhrRxHttpClient(key);
    }

    @Override
    public RxHttpClient getClient(AnnotationMetadata annotationMetadata) {
        final XhrConnection key = XhrConnection.findConnection(annotationMetadata);
        return new XhrRxHttpClient(key);
    }

    @Override
    public void disposeClient(AnnotationMetadata annotationMetadata) {
    }
}
