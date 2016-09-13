/**
 * 
 */
package a2geek.apple2.image.encoder.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import a2geek.apple2.image.encoder.encode.A2Encoder;

@SuppressWarnings("serial")
public class EncoderTableCellRenderer extends DefaultTableCellRenderer {
	private int maxSize = 0;
	private Color okColor = new Color(128, 255, 128);
	private Color notOkColor = new Color(255, 128, 128);
	private EncoderTableModel model = null;
	
	public EncoderTableCellRenderer(EncoderTableModel model, int maxSize) {
		this.model = model;
		this.maxSize = maxSize;
	}
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		A2Encoder encoder = model.getSelectedEncoder(row);
		setBackground(encoder.getSize() <= maxSize ? okColor : notOkColor);
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
	}
}