package io.test4rest.app.util;

import io.test4rest.app.model.KeyValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

import static io.test4rest.app.constants.CommonConstants.EMPTY_SPACE;

public class TableCopyKeyEventHandler implements EventHandler<KeyEvent> {
    private final KeyCodeCombination copyKeyCodeCombination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);

    @Override
    public void handle(KeyEvent keyEvent) {
        if (copyKeyCodeCombination.match(keyEvent)) {
            if (keyEvent.getSource() instanceof TableView) {
                // copy to clipboard
                copySelectionToClipboard((TableView<KeyValue>) keyEvent.getSource());
                // consuming handled event
                keyEvent.consume();
            }
        }
    }

    private void copySelectionToClipboard(TableView<KeyValue> tableView) {
        ObservableList<KeyValue> selectedTableData = tableView.getSelectionModel().getSelectedItems();
        if (!CollectionUtils.isEmpty(selectedTableData)) {
            StringBuilder stringBuilder = new StringBuilder();
            selectedTableData.forEach(keyValue -> stringBuilder
                    .append(keyValue.getKey())
                    .append(EMPTY_SPACE)
                    .append("->")
                    .append("\t")
                    .append(keyValue.getValue())
                    .append("\n")
            );
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(stringBuilder.toString());
            clipboard.setContent(clipboardContent);
        }
    }
}
