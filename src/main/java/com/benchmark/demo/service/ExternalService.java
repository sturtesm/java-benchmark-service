package com.benchmark.demo.service;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

public abstract class ExternalService {

	private static ExecutorService pool = Executors.newCachedThreadPool();

	
	private static final long MAX_WAIT_TIME_MS = 15000;

	/** the min response-time the credit check should take */
	private int minRT = 0;

	/** the response-time variance */
	private int rtVariance = 0;
	
	Logger logger = Logger.getLogger(ExternalService.class);
	
	public ExternalService(int minResponseTime, int responseTimeVariance) {
		this.minRT = minResponseTime;
		this.rtVariance = responseTimeVariance;
	}

	protected abstract Boolean execService() throws Exception;

	public void execService(boolean async) throws Exception {
		if (async) {
			
			ServiceExecutor executor = new ServiceExecutor(this);
			
			Future<Boolean> future = pool.submit(executor);
			
			Boolean success = future.get();
			
			if (success) {
				logger.info("Call to external service from in executor "
						+ " thread pool was successful");
			}
			else {
				logger.fatal("Call to external service from in executor "
						+ "thread pool failed");
			}
		}
		else {
			execService();
		}
	}
	
	protected void simulateLatency() {
		
		int sleepTime = new Random().nextInt(rtVariance) + minRT;
		
		logger.debug("Calculated response time: " + sleepTime);
		
		try {
			Thread.sleep(sleepTime);
		}
		catch (Exception e) {
			
		}		
	}

	private void joinOnAsync(Thread t) throws InterruptedException {
		t.join(MAX_WAIT_TIME_MS);
	}
}
