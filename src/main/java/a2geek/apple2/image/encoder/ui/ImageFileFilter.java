package a2geek.apple2.image.encoder.ui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Provides selection of known image filetypes.
 * 
 * @author a2geek@users.noreply.github.com
 */
class ImageFileFilter extends FileFilter {
	private String[] formatNames;
	public ImageFileFilter(String[] formatNames) {
		this.formatNames = formatNames;
	}
	public boolean accept(File file) {
		if (file.isDirectory()) return true;
		for (String formatName : formatNames) if (file.getName().endsWith("." + formatName)) return true;
		return false;
	}
	public String getDescription() {
		return "All Image Types";
	}
}