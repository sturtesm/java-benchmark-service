package com.benchmark.demo.service;

import java.util.concurrent.Callable;

public class ServiceExecutor implements Callable<Boolean> {

	private ExternalService service;

	ServiceExecutor(ExternalService service) {
		this.service = service;
	}
	
	@Override
	public Boolean call() throws Exception {
		return service.execService();
	}
}
