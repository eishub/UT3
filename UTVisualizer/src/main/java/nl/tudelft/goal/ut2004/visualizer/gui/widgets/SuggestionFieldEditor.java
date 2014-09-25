package nl.tudelft.goal.ut2004.visualizer.gui.widgets;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.ComboBoxEditor;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

class SuggestionFieldEditor implements ComboBoxEditor {

    private final JTextField textField;
    private final SuggestionModel model;
    private int nextSuggestionIndex = 0;
    private final JComboBox combobox;

    public SuggestionFieldEditor(JComboBox combobox, SuggestionModel model) {
        this.textField = new JTextField();
        this.model = model;
        this.combobox = combobox;
        this.textField.addKeyListener(new Listener());
    }

    private class Listener extends KeyAdapter {

        @Override
        public void keyPressed(final KeyEvent e) {
            // Invoke later to update after the key event has actually been done.
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    updateSuggestion(isAutoCompletionRequest(e));
                }
            });
        }
    }

    private boolean isAutoCompletionRequest(KeyEvent e) {
        return e.isControlDown() && e.getKeyChar() == ' ';
    }

    private void updateSuggestion(boolean complete) {

        // Hide popup, it will change.
        combobox.hidePopup();

        String query = getUnselectedText();

        // Get suggestion from provider, should take care to
        // update model to only show new suggestions.
        List<String> suggestions = model.sugestFor(query);

        // Only complete on request if we have results
        if (suggestions.size() > 0
                // And only if the user isn't deleting text
                // Or wants a completion.
                && (isQueryEdited(query) || complete)) {
            nextSuggestionIndex %= suggestions.size();
            String suggestion = suggestions.get(nextSuggestionIndex++);

            updateCompletion(complete, query, suggestion);

        } else {
            // Reset counter if we did not provide a suggestion.
            nextSuggestionIndex = 0;
        }

        // Show popup box with trimmed list of suggestions
        // as promised by the provider.
        combobox.showPopup();
    }
    private String previousQuery = null;

    /**
     * Checks if the user tries to change the suggestion.
     *
     * A suggestion is changed iff query.startsWith(previousQuery) &&
     * query.lenght() > previousQuery.lenght().
     */
    private boolean isQueryEdited(String query) {
        boolean edited = previousQuery != null
                && query.length() > previousQuery.length()
                && query.startsWith(previousQuery);

        previousQuery = query;
        return edited;
    }

    /**
     * Updates the text field with the suggestion. Ensures that if the
     * suggestion is a completion it gets appended and highlighted. Unless the
     * user requested the suggestion to be shown.
     *
     * @param showSuggestion iff the user requested to see the selection.
     * @param query the query made by the user.
     * @param suggestion the suggestion that matches the query.
     */
    private void updateCompletion(boolean showSuggestion, String query,
            String suggestion) {

        // We complete either because we were asked to
        if (showSuggestion
                // Or because we have a sensible completion
                || (suggestion.startsWith(query) && query.length() > 0)) {

            textField.setText(suggestion);

            // If we completed because we have a completion mark it
            // as such.
            if (suggestion.startsWith(query)) {
                textField.select(query.length(), suggestion.length());
            } // Otherwise make it a suggestion.
            else {
                textField.select(suggestion.indexOf(query) + query.length(),
                        suggestion.length());
            }
        }
    }

    /**
     * Returns the section of the text that the user provided as input.
     *
     * This method assumes the user has accepted the completion when it is no
     * longer highlighted until the last character.
     *
     * @return the original text the user put in.
     */
    private String getUnselectedText() {

        String selected = textField.getSelectedText();
        String fullText = textField.getText();

        // Does the selection match that of a completion?
        if (selected != null && fullText.endsWith(selected)) {
            return fullText.substring(0, fullText.indexOf(selected));
        }

        // User accepted suggestion.
        return fullText;
    }

    @Override
    public Component getEditorComponent() {
        return textField;
    }

    @Override
    public Object getItem() {
        return model.getItem(textField.getText());
    }

    @Override
    public void removeActionListener(ActionListener l) {
        textField.removeActionListener(l);
    }

    @Override
    public void addActionListener(ActionListener l) {
        textField.addActionListener(l);

    }

    @Override
    public void selectAll() {
        textField.selectAll();
    }

    @Override
    public void setItem(Object item) {
        if (item != null) {
            textField.setText(item.toString());
        } else {
            textField.setText("");
        }
    }
}