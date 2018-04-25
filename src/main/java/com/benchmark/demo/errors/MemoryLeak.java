package com.benchmark.demo.errors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

public class MemoryLeak {
	
	Logger logger = Logger.getLogger(MemoryLeak.class);

	private static List<String> heapLeak = new ArrayList<String> ();

	private int totalLeakSizeBytes = 0;
	
	/** should we leak mem ongoing? */
	private boolean ongoingMemLeak = false;
	
	private char randomChar() {
	    return (char) (48 + new Random().nextInt(47));
	}
	
	public void clearMemLeak() {
		heapLeak.clear();
		setOngoingMemLeak(false);
		totalLeakSizeBytes = 0;
	}
	
	public List<String> getHeapLeak() {
		return heapLeak;
	}
	
	public int getLeakSizeBytes() {
		return totalLeakSizeBytes;
	}
	
	public float getLeakSizeMB() {
		return getLeakSizeBytes() / 1000000;
	}
	
	public void leakMemory(Integer leakRequestBytes, boolean ongoingLeak) {
		
		setOngoingMemLeak(ongoingLeak);
		
		if (leakRequestBytes == null) {
			logger.debug("leak size is null, creating random leak between 1-MB to 10-MB");
			
			int minLeak = 1000 * 1000; // 1-MB
			int maxLeak = minLeak * 10;
		
			leakRequestBytes = new Random().nextInt(maxLeak) + minLeak;
		}
		else {
			logger.info("Received request to leak " + leakRequestBytes + " (Bytes)");
		}
		/**
		StringBuffer buf = new StringBuffer(leakRequestBytes);
		
		for (int i =0; i < leakRequestBytes; i++) {
			buf.append(randomChar());
		}
		**/
		
		char[] buf = new char[leakRequestBytes];
		
		/**
		for (int i=0; i < leakRequestBytes; i++) {
			buf[i] = (char) i;
		}
		*/
		
		heapLeak.add(new String(buf));
		
		/** track the leak */
		totalLeakSizeBytes += leakRequestBytes;
		
		logger.debug("Adding " + leakRequestBytes + 
				" Bytes to heap leak, leak is now + " + getLeakSizeMB() + " (MB).");
	}

	public boolean isOngoingMemLeak() {
		return ongoingMemLeak;
	}

	public void setOngoingMemLeak(boolean ongoingMemLeak) {
		this.ongoingMemLeak = ongoingMemLeak;
	}

}
