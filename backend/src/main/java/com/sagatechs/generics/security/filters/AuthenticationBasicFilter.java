package com.sagatechs.generics.security.filters;

import com.sagatechs.generics.exceptions.AccessDeniedException;
import com.sagatechs.generics.security.annotations.BasicSecured;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@BasicSecured
//@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationBasicFilter implements ContainerRequestFilter {

	private static final String REALM = "example";
	private static final String AUTHENTICATION_SCHEME = "Bearer";

	private static final String USERNAME_KEY = "username";
	private static final String PASSWORD_KEY = "password";
	private static final String USERNAME = "osmosys_im";
	private static final String PASSWORD = "A1a2a3a4$";

	@SuppressWarnings({"unused", "CdiInjectionPointsInspection"})
	@Inject
	private SecurityContext securityContext;


	@Context
	private HttpServletRequest httpRequest;

	@Context
	private HttpServletResponse httpResponse;

	private static final Logger LOGGER = Logger.getLogger(AuthenticationBasicFilter.class);

	@Context
	private ResourceInfo resourceInfo;


	@SuppressWarnings("unused")
	@Override
	public void filter(ContainerRequestContext requestContext) {

		Method method = resourceInfo.getResourceMethod();

		if (!method.isAnnotationPresent(BasicSecured.class)) {
			return;
		}
		// Get the Authorization header from the request
		try {
			String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

			Map<String, String> mapa = decodeAuthorizacionHeader(authorizationHeader);
			if (USERNAME.equals(mapa.get(USERNAME_KEY))
					&& PASSWORD.equals(mapa.get(PASSWORD_KEY))) {
				LOGGER.info("Autenticación Básica autorizado: " +USERNAME);
			} else {
				abortWithUnauthorized(requestContext);
			}

		} catch (Exception e) {
			abortWithUnauthorized(requestContext);
		}
	}

	private void abortWithUnauthorized(ContainerRequestContext requestContext) {

		// Abort the filter chain with a 401 status code response
		// The WWW-Authenticate header is sent along with the response
		requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
				.header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"").build());
	}

	/**
	 * Decodifica header de autorizacion básica,
	 * 
	 * @param authorizationHeader
	 * @return mapa con usuario y contrasena con keys USERNAME_KEY,PASSWORD_KEY
	 * @throws AccessDeniedException
	 * @throws Exception
	 */
	@SuppressWarnings("WeakerAccess")
	public Map<String, String> decodeAuthorizacionHeader(String authorizationHeader) throws AccessDeniedException {

		String codedString = StringUtils.removeStart(authorizationHeader, "Basic ");

		LOGGER.debug(codedString);

		if (codedString == null) {
			//noinspection ConstantConditions
			LOGGER.error("cabecera de autenticación mal formada: " + authorizationHeader);
			throw new AccessDeniedException("Nombre de usuario o contraseña incorrectos");
		}

		// *****Decode the authorisation String*****
		byte[] e = Base64.getDecoder().decode(codedString);
		String usernpass = new String(e);
		// *****Split the username from the password*****
		String user = usernpass.substring(0, usernpass.indexOf(":"));
		String password = usernpass.substring(usernpass.indexOf(":") + 1);

		Map<String, String> result = new HashMap<>();

		result.put(USERNAME_KEY, user);
		result.put(PASSWORD_KEY, password);

		return result;
	}

}