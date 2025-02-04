package io.test4rest.app.model;

import javafx.beans.property.SimpleStringProperty;

public class KeyValue {
    private SimpleStringProperty key;
    private SimpleStringProperty value;
    private String description;

    public KeyValue() {
    }

    public KeyValue(String description, SimpleStringProperty key, SimpleStringProperty value) {
        this.description = description;
        this.key = key;
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey() {
        return key.get();
    }

    public SimpleStringProperty keyProperty() {
        return key;
    }

    public void setKey(String key) {
        this.key.set(key);
    }

    public String getValue() {
        return value.get();
    }

    public SimpleStringProperty valueProperty() {
        return value;
    }

    public void setValue(String value) {
        this.value.set(value);
    }
}
