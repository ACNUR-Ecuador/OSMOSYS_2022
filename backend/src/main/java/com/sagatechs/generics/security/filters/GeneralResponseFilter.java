package com.sagatechs.generics.security.filters;

import com.sagatechs.generics.security.servicio.UserService;
import com.sagatechs.generics.webservice.webModel.UserWeb;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@PreMatching
public class GeneralResponseFilter implements ContainerResponseFilter {

    private static final Logger LOGGER = Logger.getLogger(GeneralResponseFilter.class);
    @Inject
    UserService userService;

    private static final String REALM = "example";
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {


        // Get the Authorization header from the request
        String authorizationHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        LOGGER.debug(containerRequestContext.getHeaders());
        // Validate the Authorization header
        if (isTokenBasedAuthentication(authorizationHeader)) {
            int status = containerResponseContext.getStatus();

            if (status != 401 && status != 403) {
                // Extract the token from the Authorization header
                String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
                //String newToken = this.userService.refreshTokenFromToken(token);
                containerResponseContext.getHeaders().add("refresh-token", "newToken");
                return;
            }
        } else {
            int status = containerResponseContext.getStatus();
            if (status >= 200 && status < 300) {

                if (containerRequestContext.getMethod().equals("POST") && containerRequestContext.getUriInfo().getPath().equals("/authentication/login")) {
               /* GeneralResponseJWT responseBodyJWT = new GeneralResponseJWT();
                responseBodyJWT.setState("ok");*/
                    UserWeb user = (UserWeb) containerResponseContext.getEntity();
                /*responseBodyJWT.setData(user);
                containerResponseContext.setEntity(responseBodyJWT);*/
                    //String token = this.userService.issueTokenForLogin(user);
                    //responseBodyJWT.setToken(token);
                    containerResponseContext.getHeaders().add("refresh-token", "token");
                }
            }
        }


    }


    private boolean isTokenBasedAuthentication(String authorizationHeader) {

        // Check if the Authorization header is valid
        // It must not be null and must be prefixed with "Bearer" plus a whitespace
        // The authentication scheme comparison must be case-insensitive
        return authorizationHeader != null
                && authorizationHeader.toLowerCase().startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {

        // Abort the filter chain with a 401 status code response
        // The WWW-Authenticate header is sent along with the response
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"").build());
    }
}
