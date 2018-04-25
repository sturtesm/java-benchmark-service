package com.benchmark.demo.errors;

import org.apache.log4j.Logger;

/** 
 * Used to meter the response-times or error rates for all transactions.
 * 
 * @author steve.sturtevant
 *
 */
public class ResponseTimeMeter {

	public static final float MAX_METER_FACTOR=5.0f;
	public static final float MIN_METER_FACTOR=0.1f;

	Logger logger = Logger.getLogger(ResponseTimeMeter.class);

	/** we'll meter all transactions by this rate, either to improve or degrade them */
	private float meterRate = 1.0f;

	
	public ResponseTimeMeter() {

	}

	public float getMeterRate() {
		return meterRate;
	}

	public float getMeteredResponseTime(float input) {

		logger.debug("Setting globally degraded response time rate of: " + meterRate);

		return input * meterRate;
	}


	public void setMeterRate(float rate) {
		if (rate > MAX_METER_FACTOR) {
			throw new IllegalArgumentException("Can't increase the response-time meter > " + 
					MAX_METER_FACTOR);
		}
		else if (rate < MIN_METER_FACTOR) {
			throw new IllegalArgumentException("Can't decrease the response-time meter < " + 
					MIN_METER_FACTOR);
		}

		logger.debug("Updating response-time metering globally for all BTs to: " + rate + "%");

		this.meterRate = rate;		
	}

}
