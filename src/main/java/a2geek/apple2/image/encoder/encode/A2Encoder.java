package a2geek.apple2.image.encoder.encode;

import java.io.ByteArrayOutputStream;

import a2geek.apple2.image.encoder.A2Image;

public abstract class A2Encoder {
	private ByteArrayOutputStream encodedData = new ByteArrayOutputStream();
	
	public abstract void encode(A2Image a2image, int maxSize);
	public abstract String getTitle();
	public void reset(int maxSize) {
		encodedData.reset();
	}
	public void addByte(int value) {
		encodedData.write(value & 0xff);
	}
	public void addBytes(int[] bytes) {
		for (int b : bytes) {
			addByte(b);
		}
	}
	public int getSize() {
		return encodedData.size();
	}
	public byte[] getData() {
		return encodedData.toByteArray();
	}
	/**
	 * Compute the run size of the given byte pattern.
	 * Specifically, the number of iterations of the given byte pattern.
	 */
	protected int getRunSize(byte[] data, int start, int runLengthSize, int maxIterations) {
		if (start + runLengthSize >= data.length) return 0;
		byte[] run = new byte[runLengthSize];
		System.arraycopy(data, start, run, 0, runLengthSize);
		int runLength = 0;
		for (int i=0; start+i<data.length && runLength<maxIterations; i+= runLengthSize) {
			for (int j=0; j<runLengthSize; j++) {
				int pos = start+i+j;
				if (pos >= data.length) return runLength;
				int byt = data[pos];
				if (run[j] != byt) return runLength;
			}
			runLength++;
		}
		return runLength;
	}
	/**
	 * Find the best run of bytes with varying lengths. 
	 */
	protected int[] findBestRun(byte[] data, int maxRunSize, int position, int maxRunLength, int bytesUsedFactor) {
    	int maxBytesSaved = 0;	// We don't want to encode unless we save some space!!
    	int bestRunLength = Integer.MIN_VALUE;
    	int[] run = null;
    	for (int s=0; s<maxRunSize; s++) {
    		int runSize = s+1;
    		int runLength = getRunSize(data, position, runSize, maxRunLength);
    		int bytesUsed = bytesUsedFactor + runSize;
    		int bytesSaved = (runLength*runSize) - bytesUsed;
    		if (bytesSaved > maxBytesSaved) {
    			maxBytesSaved = bytesSaved;
    			bestRunLength = runLength;
    			run = new int[runSize];
    			for (int j=0; j<run.length; j++) run[j]= data[position+j];
    		}
    	}
    	return bestRunLength > 1 ? run : null;
	}
}
