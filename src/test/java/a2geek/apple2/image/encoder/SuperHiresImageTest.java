package a2geek.apple2.image.encoder;
import a2geek.apple2.image.encoder.SuperHiresImage;
import junit.framework.TestCase;

public class SuperHiresImageTest extends TestCase {
//	public void testRgb4to8() {
//		SuperHiresImage image = new SuperHiresImage(null);
//		assertEquals(0xf0f0f0, image.rgb4to8(0xfff));
//		assertEquals(0xf0f000, image.rgb4to8(0xff0));
//	}
	
	public void testRgb8to4() {
		SuperHiresImage image = new SuperHiresImage(null, false, null);
		assertEquals(0xfff, image.rgb8to4(0x00f0f0f0));
		assertEquals(0xff0, image.rgb8to4(0x00ffff00));
	}
}
