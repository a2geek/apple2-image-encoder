package a2geek.apple2.image.encoder.encode;

import a2geek.apple2.image.encoder.encode.BitPack3;

/**
 * Exercise BitPack3.
 * 
 * @author a2geek@users.noreply.github.com
 * @see a2geek.apple2.image.encoder.encode.RleEncoder;
 * @see a2geek.apple2.image.encoder.encode.EncoderTestCase;
 */
public class BitPack3EncoderTest extends EncoderTestCase {
	/**
	 * Test the RleEncoder with the given input and output.
	 */
	protected void test(String input, String output) {
		byte[] data = toBytes(input);
		byte[] answer = toBytes(output);
		BitPack3 e = new BitPack3();
		e.encode(data, 100);
		byte[] result = e.getData();
		assertEquals(answer.length, e.getSize());
		assertEquals(answer, result);
	}
	
	public void testNoRuns() {
		test("01020304", "0301020304");
	}
	public void testOneByteRun() {
		test(repeat("12",10), "8912");
	}
	public void testTwoByteRun() {
		test(repeat("5566",6), "955566");
	}
	public void testMixedData() {
		test("1122" + repeat("55",25) + "3344", "0111229b555502553344");
	}
}
