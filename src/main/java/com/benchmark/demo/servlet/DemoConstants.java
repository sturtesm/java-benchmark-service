package com.benchmark.demo.servlet;

import org.apache.log4j.Logger;

public class DemoConstants {
	Logger logger = Logger.getLogger(DemoConstants.class);

	public static final String SCENARIO_KEY_PARM = "SCENARIO";
	public static final String ITERATIONS_KEY_PARM = "ITERATIONS";
	public static final String COMPLEXITY_KEY_PARM = "COMPLEXITY";
	public static final String RATE_KEY_PARM = "rate";
	
	public static final Integer DEFAULT_ITERATIONS = 10;
	public static final Integer DEFAULT_COMPLEXITY = 10;

	public enum SCENARIOS {
		NULL_POINTER("null-pointer"),
		UNKNOWN("unknown");

		private String type = "unknown";

		SCENARIOS(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}

		public static SCENARIOS getScenario(String s) {

			for (SCENARIOS scenario : SCENARIOS.values()) {
				if (s.compareTo(scenario.getType()) == 0) {
					return scenario;
				}
			}

			return UNKNOWN;
		}
	}

}
