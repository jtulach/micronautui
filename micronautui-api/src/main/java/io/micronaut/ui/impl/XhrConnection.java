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

import io.micronaut.core.annotation.AnnotationMetadata;
import io.micronaut.http.HttpVersion;
import io.micronaut.http.client.annotation.Client;

final class XhrConnection {
    static final XhrConnection DEFAULT_CLIENT_KEY = new XhrConnection(null, null, HttpVersion.HTTP_1_1);
    final String id;
    final String path;
    final HttpVersion httpVersion;

    XhrConnection(String id, String path, HttpVersion httpVersion) {
        this.id = id;
        this.path = path;
        this.httpVersion = httpVersion;
    }

    static XhrConnection findConnection(AnnotationMetadata annotationMetadata) {
        String id = annotationMetadata.stringValue(Client.class).orElse(null);
        String path = annotationMetadata.stringValue(Client.class, "path").orElse(null);
        HttpVersion httpVersion = annotationMetadata.enumValue(Client.class, "httpVersion", HttpVersion.class).orElse(HttpVersion.HTTP_1_1);
        return new XhrConnection(id, path, httpVersion);
    }

}
