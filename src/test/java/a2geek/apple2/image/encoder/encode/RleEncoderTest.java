package a2geek.apple2.image.encoder.encode;

import a2geek.apple2.image.encoder.encode.RleEncoder;

/**
 * Exercise the RleEncoder.
 * <p>
 * Note that the RleEncoder and VariableRleEncoder actually encode the repeated byte
 * and the number of repeats in a different order.
 * 
 * @author a2geek@users.noreply.github.com
 * @see a2geek.apple2.image.encoder.encode.RleEncoder;
 * @see a2geek.apple2.image.encoder.encode.EncoderTestCase;
 */
public class RleEncoderTest extends EncoderTestCase {
	/**
	 * Test the RleEncoder with the given input and output.
	 */
	protected void test(String input, String output) {
		byte[] data = toBytes(input);
		byte[] answer = toBytes(output);
		RleEncoder e = new RleEncoder();
		e.encode(data, 100);
		byte[] result = e.getData();
		assertEquals(answer.length, e.getSize());
		assertEquals(answer, result);
	}
	
	public void testNoRuns() {
		test("01020304", "0001020304");
	}
	public void testOneByteRun() {
		test(repeat("12",10), "0000120a");
	}
	public void testTwoByteRun() {
		test(repeat("5566",6), "00" + repeat("5566",6));
	}
	public void testMixedData() {
		test("1122" + repeat("55",25) + "3344", "0011220055193344");
	}
}
