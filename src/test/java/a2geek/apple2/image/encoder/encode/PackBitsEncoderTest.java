package a2geek.apple2.image.encoder.encode;

import a2geek.apple2.image.encoder.encode.PackBitsEncoder;

/**
 * Exercise the PackBitsEncoder
 * 
 * @author a2geek@users.noreply.github.com
 * @see a2geek.apple2.image.encoder.encode.PackBitsEncoder;
 * @see a2geek.apple2.image.encoder.encode.EncoderTestCase;
 */
public class PackBitsEncoderTest extends EncoderTestCase {
	/**
	 * Test the PackBitsEncoder with the given input and output.
	 */
	protected void test(String input, String output) {
		byte[] data = toBytes(input);
		byte[] answer = toBytes(output);
		PackBitsEncoder e = new PackBitsEncoder();
		e.encode(data, 100);
		byte[] result = e.getData();
		assertEquals(answer.length, e.getSize());
		assertEquals(answer, result);
	}
	
	public void testNoRuns() {
		test("01020304", "0301020304");
	}
	public void testOneByteRun() {
		test(repeat("12",10), "F712");
	}
	public void testTwoByteRun() {
		test(repeat("5566",6), "0b" + repeat("5566",6));
	}
	public void testMixedData() {
		test("1122" + repeat("55",25) + "3344", "011122e855013344");
	}
	public void testFromAppleSite() {
		test("AAAAAA80002AAAAAAAAA80002A22AAAAAAAAAAAAAAAAAAAA", 
				"FEAA0280002AFDAA0380002A22F7AA");
	
	}
}
