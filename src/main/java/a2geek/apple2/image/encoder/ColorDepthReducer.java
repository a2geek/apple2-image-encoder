package a2geek.apple2.image.encoder;
import java.awt.image.BufferedImage;

import a2geek.apple2.image.encoder.util.ProgressListener;

/**
 * Reduces the color depth of an image to one that a IIgs can handle.
 * It allows up to 16 pallets of 16 colors.  Each color has 4 bits.
 */
public class ColorDepthReducer {
	private BufferedImage image;
	private PaletteEntry[][] paletteColors;
	private int[] paletteNumbers;
	private ProgressListener progressListener;
	/**
	 * Prepare to reduce colors for a super hires palette.
	 */
	public ColorDepthReducer(BufferedImage image) {
		this.image = image;
		paletteColors = new PaletteEntry[16][16];
		paletteNumbers = new int[200];
	}
	/**
	 * Prepare to reduce colors for an Apple II standard color palette (typically 16 lores colors).
	 */
	public ColorDepthReducer(BufferedImage image, PaletteEntry[] palette) {
		this.image = image;
		paletteNumbers = null;
		paletteColors = new PaletteEntry[1][];
		paletteColors[0] = palette;
	}
	/**
	 * Begin the color reduction process.
	 */
	public void process() {
		if (paletteNumbers != null) {
			processSuperHires();
		} else {
			processToPalette();
		}
	}
	/**
	 * Begin the color reduction process for super hires.
	 * We group the image into 16 bands of color.
	 */
	private void processSuperHires() {
		int bandHeight = (image.getHeight() / paletteColors.length) + 1;
		int paletteNumber = 0;
		for (int y=0; y<image.getHeight(); y+=bandHeight) {
			if (progressListener != null) {
				if (progressListener.isCancelled()) return;
				progressListener.update(y, image.getHeight(), "Palette #" + (paletteNumber+1));
			}
			int ymin = y;
			int ymax = (y + bandHeight < image.getHeight()) ? y+bandHeight : image.getHeight();
			paletteColors[paletteNumber]= processBand(paletteNumber,ymin,ymax);
			paletteNumber++;
		}
	}
	/**
	 * Merge all colors to our given palette.
	 */
	private void processToPalette() {
		for (int y=0; y<image.getHeight(); y++) {
			for (int x=0; x<image.getWidth(); x++) {
				int color = getColor(x,y);
				int entry = getClosestColor(paletteColors[0], 
						(color & 0xf00) >> 8,		// red
						(color & 0x0f0) >> 4,		// green
						color & 0x00f				// blue
						);
				image.setRGB(x, y, paletteColors[0][entry].getMergedColor16());
			}
		}
	}
	/**
	 * Process a band of the image.  This is where the bulk of the work is done.
	 */
	private PaletteEntry[] processBand(int paletteNumber, int yStart, int yEnd) {
		PaletteEntry[] palette = new PaletteEntry[0x1000];
		// Setup all starting PaletteEntries
		for (int c=0; c<palette.length; c++) palette[c] = new PaletteEntry(c);
		// Count colors
		for (int y=yStart; y<yEnd; y++) {
			paletteNumbers[y] = paletteNumber;
			for (int x=0; x<image.getWidth(); x++) {
				palette[getColor(x,y)].incrementOriginalCount();
			}
		}
		int colors = 0;
		for (int i=0; i<palette.length; i++) colors+= (palette[i].isActive()) ? 1 : 0;
		// Begin merging colors
		while (colors > 16) {
			if (progressListener != null) {
				if (progressListener.isCancelled()) return null;
				progressListener.update(yStart, image.getHeight(), "Palette #" + (paletteNumber+1) + " (" + colors + " colors)");
			}
			// Locate least popular color
			int leastPopular = -1;
			double leastPopularFactor = Double.MAX_VALUE;
			for (int c=0; c<palette.length; c++) {
				if (palette[c].isActive()) {
					int r0 = palette[c].getRed(); 
					int g0 = palette[c].getGreen(); 
					int b0 = palette[c].getBlue();
					for (int activeColor=0; activeColor<palette.length; activeColor++) {
						if (!palette[activeColor].isActive() || activeColor == c) continue;
						int r1 = palette[activeColor].getRed() - r0; 
						int g1 = palette[activeColor].getGreen() - g0; 
						int b1 = palette[activeColor].getBlue() - b0;
						double distance = Math.sqrt(r1*r1 + g1*g1 + b1*b1);
						double factor = distance * palette[c].getCount();
						if (factor < leastPopularFactor) {
							leastPopular = c;
							leastPopularFactor = factor;
						}
					}
				}
			}
			palette[leastPopular].setInactive();
			// Re-merge affected inactive colors
			for (int inactiveColor=0; inactiveColor<palette.length; inactiveColor++) {
				if (palette[inactiveColor].isActive()) continue;
				if (inactiveColor != leastPopular && palette[inactiveColor].getMergedColor() != leastPopular) continue;
				int closestColor = getClosestColor(palette, palette[inactiveColor]);
				palette[inactiveColor].setMergedColor(closestColor);
				palette[closestColor].addToCount(palette[inactiveColor].getOriginalCount());
			}
			// Done with this color
			colors--;
		}
		// Map to new colors
		for (int y=yStart; y<yEnd; y++) {
			for (int x=0; x<image.getWidth(); x++) {
				image.setRGB(x, y, palette[getColor(x,y)].getMergedColor16());
			}
		}
		// Build color palette
		PaletteEntry[] colorPalette = new PaletteEntry[16];
		int currentEntry = 0;
		for (int c=0; c<0x1000; c++) {
			if (palette[c].isActive()) colorPalette[currentEntry++] = palette[c];
		}
		return colorPalette;
	}
	
	private int getColor(int x, int y) {
		int color = image.getRGB(x,y);
		return (color & 0xf00000) >> 12
			 | (color & 0x00f000) >> 8
			 | (color & 0x0000f0) >> 4;
	}
	
	/**
	 * Locate the closest color to the given PaletteEntry.
	 * Returns with the palette index entry.
	 */
	private int getClosestColor(PaletteEntry[] paletteEntries, PaletteEntry paletteEntry) {
		int r0 = paletteEntry.getRed(); 
		int g0 = paletteEntry.getGreen(); 
		int b0 = paletteEntry.getBlue();
		return getClosestColor(paletteEntries, r0, g0, b0);
	}
	/**
	 * Locate the closest color to the given R,G,B coordinates in the given palette.
	 * Returns with the palette index entry.
	 */
	private int getClosestColor(PaletteEntry[] paletteEntries, int r0, int g0, int b0) {
		double targetDistance = Double.MAX_VALUE;
		int closestColor = -1;
		for (int activeColor=0; activeColor<paletteEntries.length; activeColor++) {
			if (!paletteEntries[activeColor].isActive()) continue;
			int r1 = paletteEntries[activeColor].getRed() - r0; 
			int g1 = paletteEntries[activeColor].getGreen() - g0; 
			int b1 = paletteEntries[activeColor].getBlue() - b0;
			double distance = Math.sqrt(r1*r1 + g1*g1 + b1*b1);
			if (distance < targetDistance) {
				targetDistance = distance;
				closestColor = activeColor;
			}
		}
		return closestColor;
	}
	
	public PaletteEntry[][] getPaletteColors() {
		return paletteColors;
	}
	public int[] getPaletteNumbers() {
		return paletteNumbers;
	}
	public ProgressListener getProgressListener() {
		return this.progressListener;
	}
	public void setProgressListener(ProgressListener progressListener) {
		this.progressListener = progressListener;
	}
}
