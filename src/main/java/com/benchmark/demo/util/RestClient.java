package com.benchmark.demo.util;


import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.log4j.Logger;


public class RestClient {

	private Logger logger = Logger.getLogger(RestClient.class);

	public RestClient() {
		
	}
	
	
	public WebClient getWebClient(String protocol, String hostname, Integer port, String uriBasePath) {
		StringBuffer target = new StringBuffer("");
		
		if (protocol == null) {
			throw new IllegalArgumentException("Port cannot be null");
		}
		else if (hostname == null) {
			throw new IllegalArgumentException("Hostname cannot be null");
		}
		
		target.append(protocol + "://");
		target.append(hostname);
		
		if (port != null) {
			target.append(":" + port.toString() );
		}
		if (uriBasePath != null) {
			target.append("/" + uriBasePath);
		}

		logger.debug("Returning WebClient for path " + target);
		
		return WebClient.create(target.toString());
	}
	
	public WebClient setPath(WebClient client, String path) {
		return client.path(path);
	}
}
