package a2geek.apple2.image.encoder.encode;

import junit.framework.TestCase;

/**
 * Contains helper methods that can be imported into any of the encoder tests.
 * 
 * @author a2geek@users.noreply.github.com
 */
public abstract class EncoderTestCase extends TestCase {
	/**
	 * Convert a hex string into an array of bytes.
	 * String is in the format "112233" which is decoded as the array
	 * of bytes 0x11, 0x22, 0x33.
	 */
	public byte[] toBytes(String hexString) {
		if (hexString == null || hexString.length() % 2 == 1) {
			throw new NumberFormatException("String must be an even length and not null");
		}
		byte[] data = new byte[hexString.length() / 2];
		for (int i=0; i<data.length; i++) {
			data[i] = (byte)(Integer.valueOf(hexString.substring(i*2, i*2+2), 16) & 0xff);
		}
		return data;
	}
	/**
	 * Test if the two byte arrays are identical.
	 * Note that the data length may be longer than the answer length -
	 * this is because the encoders generally construct a byte array of
	 * the maximum file size allowed.
	 */
	public void assertEquals(byte[] answer, byte[] data) {
		assertTrue(answer.length <= data.length);
		for (int i=0; i<answer.length; i++) {
			assertEquals(answer[i] & 0xff, data[i] & 0xff);
		}
	}
	/**
	 * Repeat a byte string the given number of times.
	 * Used to generate compressable data streams.
	 */
	public String repeat(String repeatedBytes, int numberOfRepeats) {
		StringBuffer data = new StringBuffer();
		while (numberOfRepeats-- > 0) data.append(repeatedBytes);
		return data.toString();
	}
}
