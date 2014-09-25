package nl.tudelft.goal.ut2004.visualizer.gui.widgets;

import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 * Smart text field that displays a list of suggestions based on what the user
 * types.
 * 
 * @author M.P. Korstanje
 * 
 */
public class SuggestionField extends JPanel {

	private final JComboBox combobox;
	private final SuggestionModel model;
	private final SuggestionFieldEditor editor;

	public SuggestionField(SuggestionModel model) {
		this.model = model;
		this.combobox = new JComboBox(model);
		this.editor = new SuggestionFieldEditor(combobox, model);

		this.combobox.setEditor(editor);
		this.combobox.setEditable(true);
		this.combobox.setSelectedIndex(-1);

		add(combobox);
	}

	public void setSelectedItem(Object item) {
		combobox.setSelectedItem(item);
		editor.setItem(item);
		model.setSelectedItem(item);
	}

	public Object getSelecteditem() {
		return combobox.getSelectedItem();
	}

}