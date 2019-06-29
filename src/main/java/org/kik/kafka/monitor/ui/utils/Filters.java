package org.kik.kafka.monitor.ui.utils;

import spark.Filter;
import spark.Request;
import spark.Response;

public class Filters {

	private Filters() {
		// static class
	}

	// If a user manually manipulates paths and forgets to add
	// a trailing slash, redirect the user to the correct path
	public static final Filter ADD_TRAILING_SLASHES = (Request request, Response response) -> {
		if (!request.pathInfo().endsWith("/")) {
			response.redirect(request.pathInfo() + "/");
		}
	};

	// Enable GZIP for all responses
	public static final Filter ADD_GZIP_HEADER = (Request request, Response response) -> response.header("Content-Encoding", "gzip");

}