package com.dukescript.demo.javafx.webui;

/*-
 * #%L
 * DukeScript JavaFX Extensions - a library from the "DukeScript" project.
 * %%
 * Copyright (C) 2018 Dukehoff GmbH
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

import com.dukescript.api.javafx.beans.ObservableUI;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Executable;
import io.micronaut.core.annotation.Introspected;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.java.html.json.Models;

@ObservableUI
@Introspected
public class DataModel {
    private String message;
    private boolean rotating;
    private List<HistoryElement> history = new ArrayList<>();
    
    
    public DataModel() {
//        message.addListener((observable, oldValue, newValue) -> {
//            history.add(new HistoryElement(newValue));
//        });
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRotating() {
        return rotating;
    }

    public void setRotating(boolean rotating) {
        this.rotating = rotating;
    }

    public List<HistoryElement> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryElement> history) {
        this.history = history;
    }

    public List<String> getWords() {
        return words(getMessage());
    }
    
    @Executable
    public void turnAnimationOn() {
        setRotating(true);
    }

    @Executable
    public void turnAnimationOff() {
        setRotating(false);
    }

    @Executable
    public void rotate5s() {
        setRotating(true);
        java.util.Timer timer = new java.util.Timer("Rotates a while");
        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                setRotating(false);
            }
        }, 5000);
    }

    @Executable
    public void showScreenSize() {
        setMessage("Screen size is unknown");
    }

    final void removeFromHistory(Object event) {
        HistoryElement h = null;//event.getSource(HistoryElement.class);
        history.remove(h);
        if (Objects.equals(h.message, getMessage())) {
            setMessage("Message has been removed from history");
        }
    }

    static List<String> words(String message) {
        String[] arr = new String[6];
        String[] words = message == null ? new String[0] : message.split(" ", 6);
        for (int i = 0; i < 6; i++) {
            arr[i] = words.length > i ? words[i] : "!";
        }
        return Arrays.asList(arr);
    }

    /**
     * Called when the page is ready.
     */
    static void onPageLoad() {
        ApplicationContext ac = ApplicationContext.run();
        DataModel ui = ac.getBean(DataModel.class);
        ui.setMessage("Hello World from HTML and Java!");
        Models.applyBindings(ui);
    }

    @Introspected
    @ObservableUI
    static class HistoryElement {
        final String message;

        HistoryElement(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }
}
