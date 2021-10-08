package com.sagatechs.generics.security.filters;

import javax.ws.rs.container.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

	/**
	 * Method for ContainerRequestFilter.
	 */
	@Override
	public void filter(ContainerRequestContext request) {

		// If it's a preflight request, we abort the request with
		// a 200 status, and the CORS headers are added in the
		// response filter method below.
		if (isPreflightRequest(request)) {
			request.abortWith(Response.ok().build());
		}
	}

	/**
	 * A preflight request is an OPTIONS request with an Origin header.
	 */
	private static boolean isPreflightRequest(ContainerRequestContext request) {
		return request.getHeaderString("Origin") != null && request.getMethod().equalsIgnoreCase("OPTIONS");
	}

	/**
	 * Method for ContainerResponseFilter.
	 */
	@Override
	public void filter(ContainerRequestContext request, ContainerResponseContext response) {

		// if there is no Origin header, then it is not a
		// cross origin request. We don't do anything.
		/*if (request.getHeaderString("Origin") == null) {
			return;
		}*/

		// If it is a preflight request, then we add all
		// the CORS headers here.
		if (isPreflightRequest(request)) {
			response.getHeaders().add("Access-Control-Allow-Credentials", "true");
			response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
			response.getHeaders().add("Access-Control-Expose-Headers", "Accept, refresh-token,content-disposition");
			response.getHeaders().add("Access-Control-Allow-Headers",
					// Whatever other non-standard/safe headers (see list above)
					// you want the client to be able to send to the server,
					// put it in this list. And remove the ones you don't want.
					"X-Requested-With, Authorization, appCode,content-disposition, " +
							"Accept-Version, Content-MD5, CSRF-Token, origin, content-type, accept, authorization, refresh-token");

		}
		response.getHeaders().add("Access-Control-Expose-Headers", "Accept, refresh-token,Content-Disposition");
		response.getHeaders().add("Access-Control-Allow-Headers",
				// Whatever other non-standard/safe headers (see list above)
				// you want the client to be able to send to the server,
				// put it in this list. And remove the ones you don't want.
				"X-Requested-With, Authorization, appCode,Content-Disposition, " +
						"Accept-Version, Content-MD5, CSRF-Token, origin, content-type, accept, authorization, refresh-token");

		// Cross origin requests can be either simple requests
		// or preflight request. We need to add this header
		// to both type of requests. Only preflight requests
		// need the previously added headers.
		response.getHeaders().add("Access-Control-Allow-Origin", "*");
	}
}
