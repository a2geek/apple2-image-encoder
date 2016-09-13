package a2geek.apple2.image.encoder.encode;

import a2geek.apple2.image.encoder.A2Image;

/**
 * Perform repeated byte packing (really is a type of RLE).
 * <p> 
 * Encoding becomes: <code>RR [ XX | RR XX NN ]</code>
 * where:<ul>
 * <li>RR is the repeat toggle byte (1st entry is to identify it).</li>
 * <li>XX is the color byte to place in Apple II memory.</li>
 * <li>NN is the number of bytes repeated.</li>
 * </ul>
 * 
 * @author a2geek@users.noreply.github.com
 */
public class RleEncoder extends A2Encoder {
	public String getTitle() {
		return "RLE (Standard)";
	}
	public void encode(A2Image a2, int maxSize) {
		byte[] data = a2.getBytes();
		encode(data, maxSize);
	}
	public void encode(byte[] data, int maxSize) {
		reset(maxSize);
		// Identify an unused value:
	    int count[] = new int[256];
	    for (int i=0; i<data.length; i++) {
    		count[data[i] & 0xff]++;
	    }
	    int repeatByte = 0;
	    for (int i=0; i<count.length; i++) {
	    	if (count[i] == 0) {
	    		repeatByte = i;
	    		break;
	    	}
	    }
	    // Encode data!
	    addByte(repeatByte);
	    for (int i=0; i<data.length; i++) {
	    	int color = data[i] & 0xff;
	    	if (i < data.length-2 && (color == repeatByte || (color == (data[i+1] & 0xff) && color == (data[i+2] & 0xff)))) {
	    		int number = 1;
	    		while (i < data.length-1 && number < 0xff && (data[i+1] & 0xff) == color) {
	    			number++;
	    			i++;
	    		}
	    		addByte(repeatByte);
	    		addByte(color);
	    		addByte(number);
	    	} else {
	    		addByte(color);
	    	}
	    }
	}
}
