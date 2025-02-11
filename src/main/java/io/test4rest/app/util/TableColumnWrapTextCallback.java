package io.test4rest.app.util;

import io.test4rest.app.model.KeyValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class TableColumnWrapTextCallback implements Callback<TableColumn<KeyValue, String>, TableCell<KeyValue, String>> {
    private final TableColumn<KeyValue, String> tableColumn;

    public TableColumnWrapTextCallback(TableColumn<KeyValue, String> tableColumn) {
        this.tableColumn = tableColumn;
    }

    @Override
    public TableCell<KeyValue, String> call(TableColumn<KeyValue, String> param) {
        return new TableCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!isEmpty()) {
                    Text text = new Text(item);
                    text.wrappingWidthProperty().bind(tableColumn.widthProperty().subtract(8.0));
                    setGraphic(text);
                }
            }
        };
    }
}
