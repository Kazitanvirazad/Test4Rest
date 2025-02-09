package io.test4rest.app.util;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

/**
 * @Todo Test this class
 */
public class TableUtils {
    /**
     * Install the keyboard handler:
     * + CTRL + C = copy to clipboard
     *
     * @param table
     */
    public static void installCopyPasteHandler(TableView<?> table) {
        // install copy/paste keyboard handler
        table.setOnKeyPressed(new TableKeyEventHandler());
    }

    /**
     * Copy/Paste keyboard event handler.
     * The handler uses the keyEvent's source for the clipboard data. The source must be of type TableView.
     */
    private static class TableKeyEventHandler implements EventHandler<KeyEvent> {
        private final KeyCodeCombination copyKeyCodeCombination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);

        public void handle(final KeyEvent keyEvent) {
            if (copyKeyCodeCombination.match(keyEvent)) {
                if (keyEvent.getSource() instanceof TableView) {
                    // copy to clipboard
                    copySelectionToClipboard((TableView<?>) keyEvent.getSource());
                    // event is handled, consume it
                    keyEvent.consume();
                }
            }
        }

        /**
         * Get table selection and copy it to the clipboard.
         *
         * @param table
         */
        private static void copySelectionToClipboard(TableView<?> table) {
            StringBuilder clipboardString = new StringBuilder();
            ObservableList<TablePosition> positionList = table.getSelectionModel().getSelectedCells();
            int prevRow = -1;
            for (TablePosition position : positionList) {
                int row = position.getRow();
                int column = position.getColumn();
                Object cell = (Object) table.getColumns().get(column).getCellData(row);
                // null-check: provide empty string for nulls
                if (cell == null) {
                    cell = "";
                }
                // determine whether we advance in a row (tab) or a column
                // (newline).
                if (prevRow == row) {
                    clipboardString.append('\t');
                } else if (prevRow != -1) {
                    clipboardString.append('\n');
                }
                // create string from cell
                String text = cell.toString();
                // add new item to clipboard
                clipboardString.append(text);
                // remember previous
                prevRow = row;
            }

            // create clipboard content
            final ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(clipboardString.toString());

            // set clipboard content
            Clipboard.getSystemClipboard().setContent(clipboardContent);
        }
    }
}
