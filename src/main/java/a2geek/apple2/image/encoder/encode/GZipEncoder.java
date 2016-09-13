package a2geek.apple2.image.encoder.encode;

import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPOutputStream;

import a2geek.apple2.image.encoder.A2Image;
import a2geek.apple2.image.encoder.ui.ImageEncoderApp;

/**
 * Encode the image in GZip format.
 * 
 * @author a2geek@users.noreply.github.com
 */
public class GZipEncoder extends A2Encoder {
	public void encode(A2Image a2image, int maxSize) {
		try {
			reset(maxSize);
			ByteArrayOutputStream ba = new ByteArrayOutputStream();
			GZIPOutputStream out = new GZIPOutputStream(ba);
			out.write(a2image.getBytes());
			out.close();
			for (byte b : ba.toByteArray()) {
				addByte(b);
			}
		} catch (Throwable t) {
			ImageEncoderApp.showErrorDialog("GZip compress", t);
		}
	}
	public String getTitle() {
		return "GZip";
	}
}
