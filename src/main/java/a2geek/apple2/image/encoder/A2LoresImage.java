package a2geek.apple2.image.encoder;
import java.awt.image.BufferedImage;
import java.security.InvalidParameterException;

import a2geek.apple2.image.encoder.util.ProgressListener;

/**
 * Represents an Apple II lores image.
 * @author a2geek@users.noreply.github.com
 */
public class A2LoresImage extends A2Image {
	private PaletteEntry[] palette = null;
	
	public A2LoresImage(BufferedImage image, boolean keepAspectRatio, ProgressListener listener) {
		this(image, 40, keepAspectRatio, null);
	}
	/**
	 * Used by the double lores class.
	 * We somewhat fake progress - the ProgressListener has states of 0 (starting),
	 * 1 (ready to fix colors), and 2 (done fixing colors).
	 */
	protected A2LoresImage(BufferedImage image, int width, boolean keepAspectRatio, ProgressListener listener) {
		super(image, 40, width, keepAspectRatio);
		palette = getStandard12BitPalette();
		if (listener != null) listener.update(1, 2, "Stage 1");
		ColorDepthReducer cdr = new ColorDepthReducer(getImage(), palette);
		cdr.setProgressListener(listener);
		cdr.process();
		if (listener != null) listener.update(2, 2, "Stage 2");
	}
	/**
	 * Get Apple II color at X, Y.
	 */
	public int getColor(int x, int y) {
	    int color = (getRGB(x,y) & 0xffffff);
	    if (palette != null) {
	    	int color12 = (color & 0xf00000) >> 12 | (color & 0x00f000) >> 8 | (color & 0x0000f0) >> 4;
	    	for (int i=0; i<palette.length; i++) {
	    		if (palette[i].getColor() == color12) return i;
	    	}
	    }
		System.out.println("Unknown color (0x" + Integer.toHexString(color) 
				+ ") at " + x + "," + y + "!");
		return 0;
	}
	/**
	 * Get a byte of memory.  That is, the X,Y and X,Y+1 byte.
	 */
	public int getMemoryByte(int x, int y) {
		if (x < 0 || x >= getWidth()) { 
			throw new InvalidParameterException("X(=" + x + ") must be between 0 and " 
					+ getWidth() + ".");
		}
		if (y < 0 || y >= getHeight() || y%2 == 1) {
			throw new InvalidParameterException("Y(=" + y + ") must be between 0 and " 
					+ getHeight() + " and even.");
		}
		return getColor(x,y) | (getColor(x,y+1) << 4);
	}
	/**
	 * Treat the screen as an array of bytes.
	 */
	public int getMemoryByte(int pos) {
		if (pos >= getTotalBytes()) return -1;
    	int y = (pos / getWidth()) * 2;
    	int x = pos % getWidth();
    	return getMemoryByte(x,y);
	}
	/**
	 * Create a byte array of a physical memory dump for the image.
	 * Note that this is organized vertically - line 0/1 to line 38/39
	 * instead of the actual physical memory map.
	 */
	public byte[] getBytes() {
		byte[] data = new byte[getTotalBytes()];
		for (int i=0; i<getTotalBytes(); i++) {
			data[i] = (byte)(getMemoryByte(i) & 0xff);
		}
		return data;
	}
}
