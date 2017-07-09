package a2geek.apple2.image.encoder.encode;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.compressors.CompressorStreamFactory;

public abstract class A2EncoderFactory {
	public static List<A2Encoder> getEncoders() {
		List<A2Encoder> list = new ArrayList<>();
		list.add(new RleEncoder());
		list.add(new VariableRleEncoder());
		list.add(new PackBitsEncoder());
		list.add(new BitPack1());
		list.add(new BitPack2());
		list.add(new BitPack3());
		list.add(new GZipEncoder());
		list.add(new ZipEncoder());
		// From Apache Commons
		for (String outputProvider : CompressorStreamFactory.findAvailableCompressorOutputStreamProviders().keySet()) {
			// PACK200 does nothing for some reason, so just ignoring it
			if (CompressorStreamFactory.PACK200.equals(outputProvider)) continue;
			list.add(new CommonsCodecEncoder(outputProvider));
		}
		return list;
	}
}
