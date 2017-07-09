package a2geek.apple2.image.encoder.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import a2geek.apple2.image.encoder.A2Image;
import a2geek.apple2.image.encoder.encode.A2Encoder;
import a2geek.apple2.image.encoder.encode.A2EncoderFactory;
import a2geek.apple2.image.encoder.util.ProgressListener;

/**
 * Provide a TableModel for the encoding table.  This TableModel also
 * handles actually performing the compression.
 * 
 * @author a2geek@users.noreply.github.com
 */
@SuppressWarnings("serial")
public class EncoderTableModel extends AbstractTableModel {
	private final List<A2Encoder> results = new ArrayList<>();
	private byte[] data = null;
	private String[] headers = new String[] { "Type", "Original", "Compressed", "%" };
	/**
	 * Construct the TableModel and setup all the A2Encoders.
	 */
	public EncoderTableModel(A2Image image, int maxSize, ProgressListener listener) {
		this.data = image.getBytes();

		int count = 0;
		int size = A2EncoderFactory.getEncoders().size();
		for (A2Encoder encoder : A2EncoderFactory.getEncoders()) {
			try {
				if (listener != null && listener.isCancelled()) return;
				count++;
				if (listener != null) listener.update(count, size, encoder.getTitle());
				encoder.encode(image, maxSize);
				results.add(encoder);
			} catch (Throwable t) {
				// Ignoring these as some are expected to throw errors if boundaries are missed
			}
		}
	}
	/**
	 * Answer with the name of the column.
	 */
	public String getColumnName(int column) {
		return headers[column];
	}
	/**
	 * Answer with the number of rows.
	 */
	public int getRowCount() {
		return results.size();
	}
	/**
	 * Answer with the number of columns.
	 */
	public int getColumnCount() {
		return headers.length;
	}
	/**
	 * Answer with a specific cell value in the table.
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:	
			return results.get(rowIndex).getTitle();
		case 1:
			return data.length;
		case 2:
			return results.get(rowIndex).getSize();
		case 3:
			return (results.get(rowIndex).getSize() * 100) / data.length;
		default:
			return "Unknown";
		}
	}
	/**
	 * Anwwer with the specific Class to indicate formatting in the cell.
	 */
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:	
			return String.class;
		case 1: 
			return Integer.class;
		case 2: 
			return Integer.class;
		case 3: 
			return Integer.class;
		default: 
			return Object.class;
		}
	}
	/**
	 * Answer with the specific A2Encoder.
	 */
	public A2Encoder getSelectedEncoder(int rowIndex) {
		return results.get(rowIndex);
	}
}
