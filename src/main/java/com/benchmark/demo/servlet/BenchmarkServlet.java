/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.benchmark.demo.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.benchmark.demo.servlet.DemoConstants.SCENARIOS;

/**
 * <p>
 * A simple servlet taking advantage of features added in 3.0.
 * </p>
 * 
 * <p>
 * The servlet is registered and mapped to /accountHistory
 * </p>
 * 
 */
@SuppressWarnings("serial")
@WebServlet("/api/v1/web-service/benchmark")
public class BenchmarkServlet extends AbstractBenchmark {

	private Logger logger = Logger.getLogger(AbstractBenchmark.class);

	
	public BenchmarkServlet() {
		super();
	}


	/** gets the min response-time this service should take */
	public int getMinResponseTimeMillis() {
		return 500;
	}

	/** gets the variance in response-time this service should take */
	public int getVarianceResponseTimeMillis() {
		return 1000;
	}

	@Override
	protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		logger.debug("Processing Parameters from Web Request");

		if (getRequestedScenario() == SCENARIOS.RESPONSE_TIME_METER) {
			updateResponseTimeMetering(req);
		}
	}		
}
