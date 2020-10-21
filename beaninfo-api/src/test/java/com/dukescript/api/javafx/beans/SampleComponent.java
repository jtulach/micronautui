package com.dukescript.api.javafx.beans;

import io.micronaut.context.annotation.Executable;
import io.micronaut.core.annotation.Introspected;
import javafx.event.ActionEvent;

@Introspected
@ObservableUI
public class SampleComponent {
    public SampleComponent() {
    }

    boolean ok = true;
    private String fine = "ok";
    private final String immutable = "Hi";

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getFine() {
        return fine;
    }

    public void setFine(String fine) {
        this.fine = fine;
    }

    public String getImmutable() {
        return immutable;
    }
    int counter;
    ActionEvent ev;

    private void callback(ActionEvent ev) {
        counter++;
        this.ev = ev;
    }

    @Executable
    void noArgCallback() {
        counter++;
        this.ev = null;
    }

    @Executable
    void actionCallback(ActionEvent ev) {
        counter++;
        this.ev = ev;
    }

    /*
    void actionDataCallback(ActionDataEvent ev) {
    counter += ev.getProperty(Number.class, null).intValue();
    this.ev = ev;
    }
     */
    @Executable
    int notAnAction() {
        return 0;
    }

    static void ignore() {
    }

    @Override
    public boolean equals(Object obj) {
        // MicroHack: Need a way for a bean to find out one of its interceptors
        // MicroHack: Using equals(QueryProto) trick
        return super.equals(obj);
    }
}
