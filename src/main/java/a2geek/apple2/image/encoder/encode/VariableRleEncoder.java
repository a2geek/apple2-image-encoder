package a2geek.apple2.image.encoder.encode;

import a2geek.apple2.image.encoder.A2Image;

/**
 * Perform variable length repeated byte packing (really is a type of RLE).
 * <p> 
 * Encoding becomes: <code>## R1 R2 ... [ XX | Rn XX NN ]</code>
 * where:<ul>
 * <li>## is the number of repeat toggle bytes.</li>
 * <li>Rn is the repeat toggle byte.</li>
 * <li>XX is the color byte to place in Apple II memory.</li>
 * <li>NN is the number of bytes repeated.</li>
 * </ul>
 * Each succeeding repeat toggle byte is for a run of N bytes.
 * For example, "02 55 66" identifes two repeat toggle bytes with "55"
 * indicating a run of 1 byte and "66" indicating a run of 2 bytes.
 * 
 * @author a2geek@users.noreply.github.com
 */
public class VariableRleEncoder extends A2Encoder {
	public String getTitle() {
		return "RLE (Variable)";
	}
	public void encode(A2Image a2, int maxSize) {
		byte[] data = a2.getBytes();
		encode(data, maxSize);
	}
	protected void encode(byte[] data, int maxSize) {
		int[] repeatBytes = new int[8];
		reset(maxSize);
		// Identify an unused value:
	    int count[] = new int[256];
	    for (int i=0; i<data.length; i++) {
    		count[data[i] & 0xff]++;
	    }
	    int numRepeatBytes = 0;
	    for (int i=0; i<count.length; i++) {
	    	if (count[i] == 0) {
	    		repeatBytes[numRepeatBytes] = i & 0xff;
	    		numRepeatBytes++;
	    		if (numRepeatBytes == repeatBytes.length) break;
	    	}
	    }
	    // Encode data!
	    addByte(numRepeatBytes);
	    addBytes(repeatBytes);
	    for (int i=0; i<data.length; i++) {
	    	int[] run = findBestRun(data, numRepeatBytes, i, 0xff, 2);
	    	if (run != null) {
	    		// We had a run, record it
	    		int runLength = getRunSize(data, i, run.length, 0xff);
	    		addByte(repeatBytes[run.length-1]);		// appropriate flag byte (ie, # of bytes in pattern)
	    		addByte(runLength);						// number of iterations
	    		addBytes(run);							// bytes of the repeated pattern
	    		i+= (runLength * run.length) - 1;
	    	} else {
	    		// If this is a repeat flag, handle is as if it a run of 1.
		    	int value = data[i] & 0xff;
	    		for (int s=0; s<numRepeatBytes; s++) {
	    			if (value == repeatBytes[s]) {
	    				addByte(repeatBytes[0]);
	    				addByte(1);
	    				addByte(value);
	    				continue;
	    			}
	    		}
	    		// We have a single value - record it
	    		addByte(value);
	    	}
	    }
	}
}
