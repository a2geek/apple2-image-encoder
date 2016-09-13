package a2geek.apple2.image.encoder.encode;

import a2geek.apple2.image.encoder.A2Image;

/**
 * PackBits.  The following text is from Wikipedia:
 * <p>
 * PackBits is a fast, simple compression scheme for run-length encoding of data.
 * <p>
 * Apple introduced the PackBits format with the release of MacPaint on the Macintosh computer. 
 * This compression scheme is one of the types of compression that can be used in TIFF-files.
 * <p>
 * A PackBits data stream consists of packets of one byte of header followed by data. The header 
 * is a signed byte; the data can be signed, unsigned, or packed (such as MacPaint pixels).
 * <p>
 * <table>
 * <tr><th>Header byte</th><th>Data</th></tr>
 * <tr><td>0 to 127</td><td>1 + n literal bytes of data</td></tr>
 * <tr><td>0 to -127</td><td>One byte of data, repeated 1 - n times in the decompressed output</td></tr>
 * <tr><td>-128</td><td>No operation</td></tr>
 * </table>
 * <p>
 * Note that interpreting 0 as positive or negative makes no difference in the output. Runs of 
 * two bytes adjacent to non-runs are typically written as literal data.
 * <p>
 * Looking at the bits, setup is:<br/>
 * <table>
 * <tr><td>%0nnn nnnn</td><td>1 + n literal bytes of data</td></tr>
 * <tr><td>%1nnn nnnn</td><td>One byte of data repeated 1 - n times.</td></tr>
 * <tr><td>%1000 0000</td><td>No operation (used to flag END OF FILE; this is a variance)</td></tr>
 * </table>
 *
 * @author a2geek@users.noreply.github.com (sort of)
 * @see http://en.wikipedia.org/wiki/PackBits
 * @see http://developer.apple.com/technotes/tn/tn1023.html
 * @see http://web.archive.org/web/20080705155158/http://developer.apple.com/technotes/tn/tn1023.html
 */
public class PackBitsEncoder extends A2Encoder {
	public String getTitle() {
		return "PackBits (Apple)";
	}
	public void encode(A2Image image, int maxSize) {
		byte[] data = image.getBytes();
		encode(data, maxSize);
	}
	public void encode(byte[] data, int maxSize) {
		reset(maxSize);
		for (int i=0; i<data.length; i++) {
			int b = data[i] & 0xff;
			int runSize = getRunSize(data, i, 1, 128);
			if (runSize > 2) {
				// Compress run of repeated bytes:
				addByte(- runSize + 1);
				addByte(b);
				i+= runSize - 1;
			} else {
				// Gather up nonrepeating bytes:
				int count=0;
				for (count=1; i+count<data.length && count<127; count++) {
					b = data[i+count] & 0xff;
					runSize = getRunSize(data, i+count, 1, 128);
					if (runSize > 2) break;
				}
				addByte(count - 1);
				for (int k=0; k<count; k++) {
					addByte(data[i+k] & 0xff);
				}
				i+= count - 1;
			}
		}
	}
}
