package a2geek.apple2.image.encoder.encode;

import a2geek.apple2.image.encoder.A2Image;

/**
 * Bit pack #3.
 * <p>
 * Pack image with the following format:<br/>
 *   %1nnn rrrr   Repeat the next n+1 bytes r+1 times.<br/>
 *   %0nnn nnnn   Copy the next n+1 bytes.
 * <p>
 * @author a2geek@users.noreply.github.com (sort of)
 */
public class BitPack3 extends A2Encoder {
	public String getTitle() {
		return "Bit Pack #3";
	}
	public void encode(A2Image image, int maxSize) {
		byte[] data = image.getBytes();
		encode(data, maxSize);
	}
	public void encode(byte[] data, int maxSize) {
		reset(maxSize);
		int unencodeStart = -1;
		for (int i=0; i<data.length; i++) {
	    	int[] run = findBestRun(data, 8, i, 16, 1);
	    	if (run != null) {
				// If we have an unencoded stretch, handle it:
	    		addUnencodedBytes(data, unencodeStart, i);
	    		unencodeStart = -1;
				// Dump out the encoded piece:
				int runSize = getRunSize(data, i, run.length, 16);
				addByte(0x80 | (run.length-1) << 4 | runSize-1);
				addBytes(run);
				i+= (runSize * run.length) - 1;
			} else {
				if (unencodeStart == -1) unencodeStart = i;
			}
		}
		// Handle any remaining unencoded bytes:
		addUnencodedBytes(data, unencodeStart, data.length);
	}
	/**
	 * Add a segment of uncoded bytes.
	 */
	private void addUnencodedBytes(byte[] data, int start, int end) {
		int current = start;
		while (current != -1 && current < data.length) {
			int count = end - current;
			if (count > 128) count = 128;
			addByte(0x00 + count - 1);
			for (int j=0; j<count; j++) {
				addByte(data[current+j]);
			}
			current+= count;
			if (current == end) current = -1;
		}
	}
}
