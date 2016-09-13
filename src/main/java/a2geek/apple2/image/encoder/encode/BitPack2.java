package a2geek.apple2.image.encoder.encode;

import a2geek.apple2.image.encoder.A2Image;

/**
 * Perform bit pack #2.
 * <p>
 * Image is encoded as such:<br/>
 * 1nnn nnnn = plot current color n pixels (1..128)<br/> 
 * 01nn cccc = set color, plot n pixels (n = 0 to 3; 0 = setcolor)<br/> 
 * 0000 0000 = end<br/>
 * 001n nnnn = move down n (1..32) lines (reset X to 0, line CR)<br/> 
 * 0001 ???? = undef<br/>
 * 
 * @author a2geek@users.noreply.github.com (sort of)
 */
public class BitPack2 extends A2Encoder {
	public String getTitle() {
		return "Bit Pack #2";
	}
	public void encode(A2Image a2image, int maxSize) {
		int currentColor = 0;
		// FIXME: Should implement this as endOfLine...
		int solidBlackLines = 0;
		reset(maxSize);
		for (int i=0; i<a2image.getTotalPixels(); i++) {
			int color = a2image.getColor(i);
			boolean solidBlackLine = false;
			if (color == 0 && i % a2image.getWidth() == 0) {
				int blackPixels = 0;
				for (int j=0; j<a2image.getWidth(); j++) {
					if (a2image.getColor(i+j) == 0) blackPixels++;
				}
				solidBlackLine = (blackPixels == a2image.getWidth());
			}
			if (solidBlackLine) {
				solidBlackLines++;
				i+= a2image.getWidth() - 1;	// for loop increments by 1 too
			} else {
				if (solidBlackLines > 0) {
					if (solidBlackLines > 32) {
						addByte(0x20 + 31);
						solidBlackLines-= 32;
					} else {
						addByte(0x20 + solidBlackLines-1);
						solidBlackLines = 0;
					}
					i--;	// we need to remain at the current position
				} else if (color == currentColor) {
					int number = 1;
					while (number <= 128 && a2image.getColor(i+1) == currentColor) {
						i++;
						number++;
					}
					addByte(0x80 + number-1);
				} else {		// color != currentColor
					int number = 1;
					while (number < 3 && a2image.getColor(i+1) == color) {
						i++;
						number++;
					}
					addByte(0x40 + (number << 4) + color);
					currentColor = color;
				}
			}
		}
		if (solidBlackLines > 0) {
			addByte(0x20 + solidBlackLines);
			solidBlackLines = 0;
		}
		addByte(0x00);
	}

}
