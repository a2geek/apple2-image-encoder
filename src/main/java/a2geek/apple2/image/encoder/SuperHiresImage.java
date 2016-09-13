package a2geek.apple2.image.encoder;
import java.awt.image.BufferedImage;
import java.security.InvalidParameterException;

import a2geek.apple2.image.encoder.util.ProgressListener;

/**
 * Represents an Apple II super hires image.  Images are 320x200 and can
 * have upto 16 palettes of 16 4-bit colors each.  That is, 4096 possible
 * colors in 16 entries in the 16 palettes.  Max number of colors available
 * in one image is 256.
 * 
 * @author a2geek@users.noreply.github.com
 */
public class SuperHiresImage extends A2Image {
	private int[][] paletteColors;
	private int[] paletteNumbers;
	/**
	 * Construct a Super Hires image.
	 */
	public SuperHiresImage(BufferedImage image, boolean keepAspectRatio) {
		this(image, keepAspectRatio, null);
	}
	/**
	 * Construct a Super Hires image.
	 * The image must be 320x200.
	 * If the image is not limited to 16 colors per palette, it will be reduced
	 * by a hand-crafted algorithm.
	 */
	public SuperHiresImage(BufferedImage image, boolean keepAspectRatio, ProgressListener listener) {
		super(image, 200, 320, keepAspectRatio);
		// Need to be careful and use OUR image instead of the original...
		ColorDepthReducer cdr = new ColorDepthReducer(getImage());
		cdr.setProgressListener(listener);
		cdr.process();
		// Copy results locally
		// (The structure is assumed to be 16x16)
		if (listener != null) {
			if (listener.isCancelled()) return;
			listener.update(image.getHeight(), image.getHeight(), "Remembering palettes...");
		}
		PaletteEntry[][] paletteEntries = cdr.getPaletteColors();
		paletteColors = new int[16][16];
		for (int paletteNumber=0; paletteNumber<16; paletteNumber++) {
			for (int entryNumber=0; entryNumber<16; entryNumber++) {
				PaletteEntry paletteEntry = paletteEntries[paletteNumber][entryNumber];
				if (paletteEntry != null) {
					paletteColors[paletteNumber][entryNumber] = paletteEntry.getMergedColor();
				}
			}
		}
		paletteNumbers = cdr.getPaletteNumbers();
	}
	/**
	 * Convert 8-bit color to 4-bit IIgs color.
	 * (That's from 16 million colors to 4096 colors.)
	 */
	protected int rgb8to4(int rgb8) {
		return (rgb8 & 0x00f00000) >> 12
			 | (rgb8 & 0x0000f000) >> 8
			 | (rgb8 & 0x000000f0) >> 4;
	}
	/**
	 * Get the palette entry at X,Y.
	 */
	public int getColor(int x, int y) {
	    int color = rgb8to4(getRGB(x,y));
	    int paletteNumber = paletteNumbers[y];
	    for (int c=0; c<paletteColors[paletteNumber].length; c++) {
	    	if (paletteColors[paletteNumber][c] == color) return c;
	    }
		System.out.println("Unable to locate palette entry for (0x" 
				+ Integer.toHexString(color) + ") at " + x + "," + y + "!");
		return 0;
	}
	/**
	 * Get a byte of memory.  That is, the X,Y and X,Y+1 byte.
	 */
	public int getMemoryByte(int x, int y) {
		if (x < 0 || x >= getWidth() || x%2 == 1) { 
			throw new InvalidParameterException("X(=" + x + ") must be between 0 and " 
					+ getWidth() + " and even.");
		}
		if (y < 0 || y >= getHeight()) {
			throw new InvalidParameterException("Y(=" + y + ") must be between 0 and " 
					+ getHeight() + ".");
		}
		return (getColor(x,y) << 4) | getColor(x+1,y);
	}
	/**
	 * Treat the screen as an array of bytes.
	 */
	public int getMemoryByte(int pos) {
		if (pos >= getTotalBytes()) return -1;
    	int y = pos / (getWidth() / 2);
    	int x = (pos % (getWidth() / 2)) * 2;
    	return getMemoryByte(x,y);
	}
	/**
	 * Create a byte array of a physical memory dump for the image.
	 */
	public byte[] getBytes() {
		byte[] data = new byte[0x8000];
		int offset = 0;
		// Pixel data (physical memory location $E1:2000 to $E1:9CFF
		for (int i=0; i<getTotalBytes(); i++) data[offset++] = (byte)(getMemoryByte(i) & 0xff);
		// Scan line control bytes from $E1:9D00 to $E1:9DC7
		for (int i=0; i<200; i++) data[offset++]= (byte)(paletteNumbers[i] & 0xff);
		// Memory hole from $E1:9DC8 to $E1:9DFF
		offset= 0x7e00;
		// 16 palettes from $E1:9E00 to $E1:9FFF (colors are GB 0R)
		for (int p=0; p<16; p++) {
			for (int c=0; c<16; c++) {
				int color = paletteColors[p][c];
				int red =   (color & 0xf00) >> 8;
				int green = (color & 0x0f0) >> 4;
				int blue =  (color & 0x00f);
				data[offset++]= (byte)( (green << 4) | blue );
				data[offset++]= (byte)( red );
			}
		}
		return data;
	}
}
