package a2geek.apple2.image.encoder.ui;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

import a2geek.apple2.image.encoder.A2DoubleLoresImage;
import a2geek.apple2.image.encoder.A2LoresImage;
import a2geek.apple2.image.encoder.SuperHiresImage;

public class GraphicsModeComboBoxModel implements ComboBoxModel {
	private int selectedItem = 0;
	private String[] itemNames = {
			"Super Hi-Res (320x200)",
			"Lo-Res (40x40)",
			"Double Lo-Res (80x40)"
		};
	private Class[] itemClasses = {
			SuperHiresImage.class,
			A2LoresImage.class,
			A2DoubleLoresImage.class
		};

	public void setSelectedItem(Object anItem) {
		for (int i=0; i<itemNames.length; i++) {
			if (itemNames[i].equals(anItem)) selectedItem = i;
		}
	}

	public Object getSelectedItem() {
		return itemNames[selectedItem];
	}
	public Class getSelectedClass() {
		return itemClasses[selectedItem];
	}

	public int getSize() {
		return itemNames.length;
	}

	public Object getElementAt(int index) {
		return itemNames[index];
	}

	public void addListDataListener(ListDataListener l) {
	}

	public void removeListDataListener(ListDataListener l) {
	}
}
