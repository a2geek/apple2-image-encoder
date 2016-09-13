package a2geek.apple2.image.encoder.util;

/**
 * This interface is to allow other classes to receive updates to long running tasks.
 * 
 * @author a2geek@users.noreply.github.com
 */
public interface ProgressListener {
	public void update(int currentPosition, int endPosition, String message);
	public boolean isCancelled();
}
