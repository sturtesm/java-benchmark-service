package com.benchmark.demo.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.benchmark.demo.errors.ExceptionError;
import com.benchmark.demo.errors.ResponseTimeMeter;
import com.benchmark.demo.servlet.DemoConstants;
import com.benchmark.demo.servlet.DemoConstants.SCENARIOS;


public abstract class AbstractBenchmark extends HttpServlet {

	static public String PAGE_FOOTER = "</body></html>";

	static public String PAGE_HEADER = "<html><head><title>Welcome to Our Gartner MQ Demo App</title></head><body>";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** what is the call stack complexity for this benchmark */
	private Integer benchmarkComplexity = DemoConstants.DEFAULT_COMPLEXITY;

	/** how many times will we iterate on this benchmark */
	private Integer benchmarkIterations = DemoConstants.DEFAULT_ITERATIONS;

	private Logger logger = Logger.getLogger(AbstractBenchmark.class);

	private SCENARIOS requestedScenario;

	private ResponseTimeMeter rtMeter = null;


	public AbstractBenchmark() {
		super();

		rtMeter = new ResponseTimeMeter();
	}


	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		logger.debug("Processing request " + req.getRequestURL().toString() + 
				" for user session: " + req.getSession(true).getId());

		processDemoScenario(req);

		if (benchmarkIterations < 0 || benchmarkIterations == null) {
			benchmarkIterations = DemoConstants.DEFAULT_ITERATIONS;
		}
		if (benchmarkComplexity < 0 || benchmarkComplexity == null) {
			benchmarkComplexity = DemoConstants.DEFAULT_COMPLEXITY;
		}

		long startTime = new Date().getTime();
		
		for (int i = 0; i < benchmarkIterations; i++) {
			logger.info("----------------------------------------------------------------");
			logger.info("Processing benchmark iteration: " + (i+1) + " of " + benchmarkIterations);
			logger.info("----------------------------------------------------------------");

			doSomething(benchmarkComplexity, benchmarkComplexity);
		}
		long endTime = new Date().getTime();

		String metering = String.format("%.02f %%", rtMeter.getMeterRate() * 100);
		
		String title = String.format("Performance Benchmark: Iterations: %d, Complexity: %d, Metering: %s",
				benchmarkIterations, benchmarkComplexity, metering);

		String message = String.format("Processing Time: %d (ms)", (endTime-startTime));
		
		this.printResponse(req, resp, title, message);
	}

	private void doSomething(Integer complexity, Integer iteration) {
		int minTime = getMinResponseTimeMillis();
		int maxVariance = getVarianceResponseTimeMillis();


		if (iteration > 0) {
			logger.info("[ " + iteration + " / " + complexity + "] "
					+ "Processing benchmark call stack: " + iteration + " of " + complexity);

			doSomething(complexity, iteration-1);
		}


		int sleepTime = new Random().nextInt(maxVariance) + minTime;

		logger.debug("Calculated response time: " + sleepTime);

		/** get the sleep time now influenced by the metering */
		sleepTime = (int) rtMeter.getMeteredResponseTime(sleepTime);

		logger.debug("Calculated response-time after metering is: " + sleepTime); 

		try {
			Thread.sleep(sleepTime);
		}
		catch (Exception e) {

		}	
	}

	/** gets the min response-time this service should take */
	public abstract int getMinResponseTimeMillis();

	public SCENARIOS getRequestedScenario() {
		return requestedScenario;
	}

	protected int getSleepTime(int seedTime) {
		return (int) rtMeter.getMeteredResponseTime(seedTime);
	}

	/** gets the variance in response-time this service should take */
	public abstract int getVarianceResponseTimeMillis();


	protected void printResponse(HttpServletRequest req, HttpServletResponse resp, 
			String title, String message) throws ServletException, IOException {

		ResultPrinter.addResult(req, resp, title, title, message, null);

		req.getRequestDispatcher("/jsp/response.jsp").forward(req, resp);
	}


	/**
	 * Looks at the parameters in the incoming request to see if a specific scenario
	 * should be applied.
	 * 
	 * @param req
	 */
	private void processDemoScenario(HttpServletRequest req) {
		Map<String, String[]> map = req.getParameterMap();

		Map<String, String> treeMap = 
				new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);

		logger.debug("Processing Parameters from Web Request");

		if (map == null || map.isEmpty()) {
			logger.debug("No parameters found on incoming request.");
		}

		Iterator<String> i = map.keySet().iterator();

		while (i.hasNext()) {
			String key = i.next();
			String[] values = map.get(key);

			String value = values[0];

			treeMap.put(key, value);
		}
		
		/** we'll always set the response time metering rate, regardless of the scenario */
		setResponseTimeMeterRate(treeMap); 

		if (treeMap.containsKey(DemoConstants.SCENARIO_KEY_PARM)) {
			String v = treeMap.get(DemoConstants.SCENARIO_KEY_PARM);

			SCENARIOS s = SCENARIOS.getScenario(v);

			switch (s) {
			case NULL_POINTER: logger.info("Scenario is Null Pointer Exception"); ExceptionError.throwNullPointer(); break;
			case UNKNOWN: logger.info("Scenario " + v + " is Unknown, nothing to do."); break;
			default: logger.info("Error " + v + " is Unknown, nothing to do.");
			}

			/** store the current requested scenario */
			this.requestedScenario = s;
		}

		if (treeMap.containsKey(DemoConstants.ITERATIONS_KEY_PARM)) {
			try {
				String iterations = treeMap.get(DemoConstants.ITERATIONS_KEY_PARM);

				this.benchmarkIterations = new Integer(iterations);
			}
			catch (Exception e) {
				e.printStackTrace();

				this.benchmarkIterations = DemoConstants.DEFAULT_ITERATIONS;
			}
		}
		else {
			this.benchmarkIterations = DemoConstants.DEFAULT_ITERATIONS;
		}

		if (treeMap.containsKey(DemoConstants.COMPLEXITY_KEY_PARM)) {
			try {
				String complexity = treeMap.get(DemoConstants.COMPLEXITY_KEY_PARM);

				this.benchmarkComplexity = new Integer(complexity);
			}
			catch (Exception e) {
				e.printStackTrace();

				this.benchmarkComplexity = DemoConstants.DEFAULT_COMPLEXITY;
			}
		}
		else {
			logger.info("Scenario not defined in request, will process request normally");

			this.benchmarkComplexity = DemoConstants.DEFAULT_COMPLEXITY;
		}
	}


	public void setRequestedScenario(SCENARIOS requestedScenario) {
		this.requestedScenario = requestedScenario;
	}

	protected void setResponseTimeMeterRate(Map<String, String> treeMap) {

		try {
			/** by how much should we degrade the response-times */
			String rate = treeMap.get(DemoConstants.RATE_KEY_PARM);

			if (rate == null) {
				logger.error("Can't adjust response-time metering, expecting HTTP Parameter (rate) to set meter rate.");
				return;
			}

			logger.info("Degrading response-times across the board for ALL BTs");

			logger.debug("Converting response-time meter string = " + rate + " - to float");

			float meter = new Float(rate);

			rtMeter.setMeterRate(meter);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}


	protected void updateResponseTimeMetering(HttpServletRequest req) {
		try {
			logger.debug("Processing respons-time meter rate");

			/** by how much should we degrade the response-times */
			String rate = req.getParameter("rate");

			/** should we degrade the response-times globally, or just for this BT */
			String global = req.getParameter("global");

			Class<?> degradedBTClass = null;

			if (global == null || !global.equalsIgnoreCase("true")) {
				degradedBTClass = getClass();

				logger.info("Degrading response-time ONLY for " + degradedBTClass.getName());
			}
			else {
				logger.info("Degrading response-times across the board for ALL BTs");
			}

			if (rate == null) {
				logger.error("Can't adjust response-time metering, expecting HTTP Parameter (rate) to set meter rate.");
				return;
			}

			logger.debug("Converting response-time meter string = " + rate + " - to float");
			float meter = new Float(rate);


			rtMeter.setMeterRate(meter);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
