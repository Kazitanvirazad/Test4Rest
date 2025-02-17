package io.test4rest.app.model;

import javafx.beans.property.SimpleStringProperty;

public class HeaderKeyValue extends KeyValue {
    private boolean isAddedByRequestBodyTextTypeSelector;

    public HeaderKeyValue() {
        super();
    }

    public HeaderKeyValue(String description, SimpleStringProperty key, SimpleStringProperty value) {
        super(description, key, value);
    }

    public HeaderKeyValue(String description, SimpleStringProperty key, SimpleStringProperty value, boolean isAddedByRequestBodyTextTypeSelector) {
        this(description, key, value);
        this.isAddedByRequestBodyTextTypeSelector = isAddedByRequestBodyTextTypeSelector;
    }

    public boolean isAddedByRequestBodyTextTypeSelector() {
        return isAddedByRequestBodyTextTypeSelector;
    }

    public void setAddedByRequestBodyTextTypeSelector(boolean addedByRequestBodyTextTypeSelector) {
        isAddedByRequestBodyTextTypeSelector = addedByRequestBodyTextTypeSelector;
    }
}
