package a2geek.apple2.image.encoder.encode;

import a2geek.apple2.image.encoder.A2Image;

/**
 * Perform bit pack #1.
 * <p> 
 * Image encoding:<br/>
 * 0000 0000  $00   End of picture<br/>
 * 0000 0001  $01   Color the line current color<br/>
 * 0000 0010  $02   Next line (duplicate of $4n?)<br/>
 * 0001 nnnn  $1n   Set color to n<br/>
 * 01nn nnnn  >$40  Goto line n<br/>
 * 10nn nnnn  >$80  Plot x1 coordinate<br/>
 * 11nn nnnn  >$C0  HLIN from x1 coordinate to x2 coordinate<br/>
 * 
 * @author a2geek@users.noreply.github.com (sort of)
 */
public class BitPack1 extends A2Encoder {
	public String getTitle() {
		return "Bit Pack #1";
	}
	public void encode(A2Image a2image, int maxSize) {
		if (a2image.getWidth() > 64 || a2image.getHeight() > 64) {
			throw new NumberFormatException("Maximum X or Y coordinate supported is 64!");
		}
		int currentColor = 0;
		int rowMoves = 0;
		reset(maxSize);
		for (int y=0; y<a2image.getHeight(); y++) {
			if (a2image.checkRowColor(y, 0, 0)) {
				rowMoves++;
			} else {
				if (rowMoves == 1) {
					addByte(0x02);
				} else if (rowMoves > 1) {
					addByte(0x40 + y);
				}
				rowMoves = 0;
				for (int x=0; x<a2image.getWidth(); x++) {
					int color = a2image.getColor(x,y);
					if (color != 0) {
						int i = 0;
						for (i=x; i<a2image.getWidth() && a2image.getColor(i,y) == color; i++) ;
						if (color != currentColor) {
							addByte(0x10 + color);
							currentColor = color;
						}
						addByte(0x80 + x);
						if (i > x) {
							addByte(0xc0 + i - 1);		// i is always 1 past end
						}
						x = i;
					}
				}
				rowMoves++;
			}
		}
		if (rowMoves == 1) {
			addByte(0x02);
			rowMoves = 0;
		}
		addByte(0x00);
	}
}
