package a2geek.apple2.image.encoder.encode;

import a2geek.apple2.image.encoder.encode.VariableRleEncoder;

/**
 * Exercise the VariableRleEncoder.
 * <p>
 * Note that the RleEncoder and VariableRleEncoder actually encode the repeated byte
 * and the number of repeats in a different order.
 * 
 * @author a2geek@users.noreply.github.com
 * @see a2geek.apple2.image.encoder.encode.VariableRleEncoder;
 * @see a2geek.apple2.image.encoder.encode.EncoderTestCase;
 */
public class VariableRleEncoderTest extends EncoderTestCase {
	/**
	 * Test the VariableRleEncoder with the given input and output.
	 */
	protected void test(String input, String output) {
		byte[] data = toBytes(input);
		byte[] answer = toBytes(output);
		VariableRleEncoder e = new VariableRleEncoder();
		e.encode(data, 100);
		byte[] result = e.getData();
		assertEquals(answer.length, e.getSize());
		assertEquals(answer, result);
	}
	
	public void testNoRuns() {
		test("01020304", "080005060708090a0b01020304");
	}
	public void testOneByteRun() {
		test(repeat("12",10), "080001020304050607000a12");
	}
	public void testTwoByteRun() {
		test(repeat("5566",6), "08000102030405060701065566");
	}
	public void testThreeByteRun() {
		test(repeat("112233",32), "0800010203040506070220112233");
	}
}
