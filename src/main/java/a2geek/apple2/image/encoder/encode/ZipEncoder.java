package a2geek.apple2.image.encoder.encode;

import java.io.ByteArrayOutputStream;
import java.util.zip.DeflaterOutputStream;

import a2geek.apple2.image.encoder.A2Image;
import a2geek.apple2.image.encoder.ui.ImageEncoderApp;

/**
 * Encode the image in Zip format.
 * 
 * @author a2geek@users.noreply.github.com
 */
public class ZipEncoder extends A2Encoder {
	public void encode(A2Image a2image, int maxSize) {
		try {
			reset(maxSize);
			ByteArrayOutputStream ba = new ByteArrayOutputStream();
			DeflaterOutputStream out = new DeflaterOutputStream(ba);
			out.write(a2image.getBytes());
			out.close();
			for (byte b : ba.toByteArray()) {
				addByte(b);
			}
		} catch (Throwable t) {
			ImageEncoderApp.showErrorDialog("Zip compress", t);
		}
	}
	public String getTitle() {
		return "Zip";
	}
}
