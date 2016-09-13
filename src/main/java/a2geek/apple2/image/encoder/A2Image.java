package a2geek.apple2.image.encoder;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Represents an abstract Apple II image.
 * @author a2geek@users.noreply.github.com
 */
public abstract class A2Image {
	private BufferedImage image;
	private int height;
	private int width;
	
	public A2Image(BufferedImage originalImage, int physicalHeight, int physicalWidth, boolean keepAspectRatio) {
		this.height = physicalHeight;
		this.width = physicalWidth;

		this.image = new BufferedImage(physicalWidth, physicalHeight, BufferedImage.TYPE_INT_RGB); 
		int x = 0;
		int y = 0;
		if (keepAspectRatio) {
			double aspectRatioH = (double)physicalHeight / (double)originalImage.getHeight();
			double aspectRatioW = (double)physicalWidth / (double)originalImage.getWidth();
			double aspectRatio = (aspectRatioH < aspectRatioW) ?
					aspectRatioH : aspectRatioW;
			height = (int)(originalImage.getHeight() * aspectRatio);
			width =  (int)(originalImage.getWidth() * aspectRatio);
			x = (physicalWidth - width) / 2;
			y = (physicalHeight - height) / 2;
		}
		Graphics g = this.image.getGraphics();
		g.drawImage(originalImage, x, y, width, height, null);
		g.dispose();

		this.height = physicalHeight;
		this.width = physicalWidth;
	}
	
	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}
	public int getTotalBytes() {
		return getTotalPixels() / 2;
	}
	public int getTotalPixels() {
		return height * width;
	}
	protected int getRGB(int x, int y) {
		return image.getRGB(x,y);
	}
	protected void setRGB(int x, int y, int color) {
		image.setRGB(x, y, color);
	}
	public BufferedImage getImage() {
		return image;
	}
	/**
	 * Get Apple II color at X, Y.
	 */
	public abstract int getColor(int x, int y);
	/**
	 * Get Apple II color treating the screen as an array of pixels.
	 */
	public int getColor(int pos) {
		if (pos >= getTotalPixels()) return -1;
    	int y = (pos / getWidth());
    	int x = pos % getWidth();
    	return getColor(x,y);
	}
	/**
	 * Get a byte of memory as stored by the hardware.
	 */
	public abstract int getMemoryByte(int x, int y);
	/**
	 * Treat the screen as an array of bytes.
	 */
	public abstract int getMemoryByte(int pos);
	/**
	 * Test if, from a given coordinate, the rest of the row is a given color.
	 */
	public boolean checkRowColor(int y, int xStart, int color) {
		for (int x=xStart; x<getWidth(); x++) {
			if (getColor(x,y) != color) return false;
		}
		return true;
	}
	/**
	 * Create a byte array of a physical memory dump for the image.
	 */
	public abstract byte[] getBytes();

	/**
	 * Setup the default Apple II 16 colors (lores, double lores, double hires colors).
	 */
	public static PaletteEntry[] getStandard12BitPalette() {
		PaletteEntry[] standardPalette = new PaletteEntry[16];
		standardPalette[0x0] = new PaletteEntry(0x000);	// black
		standardPalette[0x1] = new PaletteEntry(0xf00);	// magenta
		standardPalette[0x2] = new PaletteEntry(0x800);	// brown
		standardPalette[0x3] = new PaletteEntry(0xf80);	// orange
		standardPalette[0x4] = new PaletteEntry(0x080);	// dark green
		standardPalette[0x5] = new PaletteEntry(0x888);	// grey1
		standardPalette[0x6] = new PaletteEntry(0x0f0);	// green
		standardPalette[0x7] = new PaletteEntry(0xff0);	// yellow
		standardPalette[0x8] = new PaletteEntry(0x008);	// dark blue
		standardPalette[0x9] = new PaletteEntry(0xf0f);	// violet
		standardPalette[0xa] = new PaletteEntry(0xccc);	// grey2
		standardPalette[0xb] = new PaletteEntry(0xf8c);	// pink
		standardPalette[0xc] = new PaletteEntry(0x00c);	// medium blue
		standardPalette[0xd] = new PaletteEntry(0x00f);	// light blue
		standardPalette[0xe] = new PaletteEntry(0x0c8);	// aqua
		standardPalette[0xf] = new PaletteEntry(0xfff);	// white
		// Force each entry to be active
		for (PaletteEntry paletteEntry : standardPalette) paletteEntry.setActive();
		return standardPalette;
	}
}
