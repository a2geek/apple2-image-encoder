package a2geek.apple2.image.encoder.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import a2geek.apple2.image.encoder.A2Image;
import a2geek.apple2.image.encoder.encode.A2Encoder;
import a2geek.apple2.image.encoder.encode.BitPack1;
import a2geek.apple2.image.encoder.encode.BitPack2;
import a2geek.apple2.image.encoder.encode.BitPack3;
import a2geek.apple2.image.encoder.encode.GZipEncoder;
import a2geek.apple2.image.encoder.encode.PackBitsEncoder;
import a2geek.apple2.image.encoder.encode.RleEncoder;
import a2geek.apple2.image.encoder.encode.VariableRleEncoder;
import a2geek.apple2.image.encoder.encode.ZipEncoder;
import a2geek.apple2.image.encoder.util.ProgressListener;

/**
 * Provide a TableModel for the encoding table.  This TableModel also
 * handles actually performing the compression.
 * 
 * @author a2geek@users.noreply.github.com
 */
@SuppressWarnings("serial")
public class EncoderTableModel extends AbstractTableModel {
	private List<A2Encoder> encoders = new ArrayList<A2Encoder>();
	private byte[] data = null;
	private String[] headers = new String[] { "Type", "Original", "Compressed", "%" };
	/**
	 * Construct the TableModel and setup all the A2Encoders.
	 */
	public EncoderTableModel(A2Image image, int maxSize, ProgressListener listener) {
		this.data = image.getBytes();
		
		Class[] encoderClasses = new Class[] {
			RleEncoder.class,
			VariableRleEncoder.class,
			PackBitsEncoder.class,
			BitPack1.class,
			BitPack2.class,
			BitPack3.class,
			GZipEncoder.class,
			ZipEncoder.class
		};
		int count = 0;
		for (Class encoderClass : encoderClasses) {
			if (listener != null && listener.isCancelled()) return;
			A2Encoder a2encoder = null;
			try {
				a2encoder = (A2Encoder)encoderClass.newInstance();
				count++;
				if (listener != null) listener.update(count, encoderClasses.length, a2encoder.getTitle());
				a2encoder.encode(image, maxSize);
				encoders.add(a2encoder);
			} catch (Throwable t) {
				// FIXME ignore errors...
				//ImageEncoderApp.showErrorDialog(a2encoder.getTitle(), t);
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
		return encoders.size();
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
			return encoders.get(rowIndex).getTitle();
		case 1:
			return data.length;
		case 2:
			return encoders.get(rowIndex).getSize();
		case 3:
			return (encoders.get(rowIndex).getSize() * 100) / data.length;
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
		return encoders.get(rowIndex);
	}
}
