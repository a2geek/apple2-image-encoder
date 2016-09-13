package a2geek.apple2.image.encoder.util;

import java.awt.Component;

import javax.swing.ProgressMonitor;


/**
 * A simple implementation of the ProgressListener for Swing applications.
 * This should be generic enough to support all Swing usage.
 * 
 * @author a2geek@users.noreply.github.com
 */
public class DefaultSwingProgressListener implements ProgressListener {
	private ProgressMonitor progressMonitor;
	/**
	 * Constructor with customized message.
	 */
	public DefaultSwingProgressListener(Component parentComponent, String message, String initialNote) {
		progressMonitor = new ProgressMonitor(parentComponent, message, initialNote, 0, 1);
		progressMonitor.setMillisToDecideToPopup(0);
		progressMonitor.setMillisToPopup(0);
	}
	/**
	 * Update the current position.
	 */
	public void update(int currentPosition, int endPosition, String note) {
		progressMonitor.setNote(note);
		progressMonitor.setMaximum(endPosition);
		progressMonitor.setProgress(currentPosition);
	}
	/**
	 * Indicates if this operation has been cancelled.
	 */
	public boolean isCancelled() {
		return progressMonitor.isCanceled();
	}
}
