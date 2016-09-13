package a2geek.apple2.image.encoder;

/**
 * Represents an entry in the palette.
 * 
 * @author a2geek@users.noreply.github.com
 */
public class PaletteEntry {
	private boolean active;
	private int originalCount;
	private int count;
	private int color;		// assumed to be 0xRGB (instead of 0xRRGGBB)
	private int mergedColor;
	
	public PaletteEntry(int color) {
		this.color = color;
		this.mergedColor = color;
	}
	public int getColor() { return color; }
	public int getOriginalCount() { return originalCount; }
	public void incrementOriginalCount() { originalCount++; active= true; count= originalCount; }
	public boolean isActive() { return active; }
	public void setInactive() { active = false; }
	public void setActive() { active = true; }
	public int getMergedColor() { return mergedColor; }
	public int getMergedColor16() {
		return (mergedColor & 0xf00) << 12
			 | (mergedColor & 0x0f0) << 8
			 | (mergedColor & 0x00f) << 4;
	}
	public void setMergedColor(int color) { this.mergedColor = color; }
	public void addToCount(int number) { count+= number; }
	public int getCount() { return count; }
	public int getRed() { return (color & 0xf00) >> 8; }
	public int getGreen() { return (color & 0x0f0) >> 4; }
	public int getBlue() { return color & 0x00f; }
}