package nl.tudelft.goal.ut2004.visualizer.gui.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public final class SuggestionModel implements ComboBoxModel {

	private final TreeMap<String, Object> map;
	private final List<String> sortedKeys;

	public SuggestionModel() {
		this.map = new TreeMap<String, Object>();
		this.sortedKeys = new ArrayList<String>();
		this.listeners = new LinkedList<ListDataListener>();
	}

	public void addElement(Object element) {
		map.put(element.toString(), element);
		sortedKeys.add(element.toString());
		// TODO: Optimize.
		Collections.sort(sortedKeys);
		modelChanged();
	}

	private void modelChanged() {
		setSelectedItem(selected);
		notifyListeners();
	}

	public void removeElement(Object element) {
		map.remove(element.toString());
		sortedKeys.remove(element.toString());
		Collections.sort(sortedKeys);
		modelChanged();
	}

	private void notifyListeners() {
		for (ListDataListener l : listeners) {
			l.contentsChanged(new ListDataEvent(this,
					ListDataEvent.CONTENTS_CHANGED, 0, getSize()));
		}
	}

	public List<String> sugestFor(String query) {

		String lowerCaseQuery = query.toLowerCase();

		// Find new suggestions
		List<String> suggestion = new LinkedList<String>();
		for (String key : map.keySet()) {
			if (key.toLowerCase().contains(lowerCaseQuery)) {
				System.out.println("Found " + key);
				suggestion.add(key);
			}
		}

		sortedKeys.clear();
		sortedKeys.addAll(suggestion);

		// Don't notify listeners.
		// This will unset the selected text.
		// notifyListeners();

		return suggestion;
	}

	@Override
	public Object getElementAt(int index) {
		return map.get(sortedKeys.get(index));
	}

	@Override
	public int getSize() {
		return sortedKeys.size();
	}

	private final List<ListDataListener> listeners;

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	private Object selected;

	@Override
	public Object getSelectedItem() {
		return selected;
	}

	@Override
	public void setSelectedItem(Object anItem) {
		if (anItem != null) {
			selected = map.get(anItem.toString());
		} else {
			selected = anItem;
		}
	}

	public Object getItem(String key) {
		return map.get(key);
	}

}