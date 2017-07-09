package a2geek.apple2.image.encoder.encode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.CompressorStreamProvider;

import a2geek.apple2.image.encoder.A2Image;
import a2geek.apple2.image.encoder.ui.ImageEncoderApp;

public class CommonsCodecEncoder extends A2Encoder {
	private CompressorStreamProvider provider = new CompressorStreamFactory();
	private String name;
	private String title;

	public CommonsCodecEncoder(String name) {
		this(name, name.toUpperCase());
	}
	public CommonsCodecEncoder(String name, String title) {
		this.name = name;
		this.title = title;
	}

	@Override
	public void encode(A2Image a2image, int maxSize) {
		try {
			reset(maxSize);
			ByteArrayOutputStream ba = new ByteArrayOutputStream();
			CompressorOutputStream out = provider.createCompressorOutputStream(name, ba);
			out.write(a2image.getBytes());
			out.close();
			for (byte b : ba.toByteArray()) {
				addByte(b);
			}
		} catch (CompressorException | IOException ex) {
			ImageEncoderApp.showErrorDialog("Apache Commons '" + name + "' compress", ex);
		}
	}

	@Override
	public String getTitle() {
		return title;
	}

}
