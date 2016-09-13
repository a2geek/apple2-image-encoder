package a2geek.apple2.image.encoder.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 * Provide our own TransferHandler to accept only image files.
 * This is not sophisticated enough to identify type of file, however.
 * 
 * @author a2geek@users.noreply.github.com
 */
@SuppressWarnings("serial")
public class DropHandler extends TransferHandler {
	/**
	 * Custom importData method to pull in a single image file.
	 */
	public boolean importData(JComponent component, Transferable transferable) {
		DataFlavor[] flavors = transferable.getTransferDataFlavors();
		try {
			if (canImportFiles(flavors)) {
				List files = (List)transferable.getTransferData(DataFlavor.javaFileListFlavor);
				File file = (File) files.get(0);
				ImageEncoderApp.loadImage(file);
				return true;
			}
			return false;
		} catch (Exception e) {
			ImageEncoderApp.showErrorDialog("import", e);
		}
		return false;
	}
	/**
	 * Indicates if we can import these types of data.
	 */
	public boolean canImport(JComponent c, DataFlavor[] flavors) {
		return canImportFiles(flavors);
	}
	/**
	 * Determines if we can import this data.
	 */
	private boolean canImportFiles(DataFlavor[] flavors) {
		return Arrays.asList(flavors).contains(DataFlavor.javaFileListFlavor);
	}
}