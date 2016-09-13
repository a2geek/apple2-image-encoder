package a2geek.apple2.image.encoder;
import java.awt.image.BufferedImage;

import a2geek.apple2.image.encoder.util.ProgressListener;

/**
 * Represents an Apple II double lores image.
 * @author a2geek@users.noreply.github.com
 */
public class A2DoubleLoresImage extends A2LoresImage {
	public A2DoubleLoresImage(BufferedImage image, boolean keepAspectRatio) {
		this(image, keepAspectRatio, null);
	}
	public A2DoubleLoresImage(BufferedImage image, boolean keepAspectRatio, ProgressListener listener) {
		super(image, 80, keepAspectRatio, listener);
	}
}
