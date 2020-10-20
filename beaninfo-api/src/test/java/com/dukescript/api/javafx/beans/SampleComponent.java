package com.dukescript.api.javafx.beans;

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

    void noArgCallback() {
        counter++;
        this.ev = null;
    }

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
    int notAnAction() {
        return 0;
    }

    static void ignore() {
    }
    
}
