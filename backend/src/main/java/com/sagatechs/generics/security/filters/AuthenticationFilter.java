package com.sagatechs.generics.security.filters;

import com.sagatechs.generics.security.CustomPrincipal;
import com.sagatechs.generics.security.UserSecurityContext;
import com.sagatechs.generics.security.annotations.Secured;
import com.sagatechs.generics.security.credentials.UsernameJwtCredential;
import com.sagatechs.generics.security.servicio.UserService;
import com.sagatechs.generics.webservice.webModel.RoleWeb;
import com.sagatechs.generics.webservice.webModel.UserWeb;
import org.jboss.logging.Logger;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String REALM = "example";
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    private SecurityContext securityContext;


    @Inject
    UserService userService;

    @Context
    private HttpServletRequest httpRequest;

    @Context
    private HttpServletResponse httpResponse;

    private final static Logger LOGGER = Logger.getLogger(AuthenticationFilter.class);


    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Get the Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        // LOGGER.debug(requestContext.getUriInfo().getRequestUri().toURL());
        // Validate the Authorization header
        if (!isTokenBasedAuthentication(authorizationHeader)) {
            abortWithUnauthorized(requestContext);
            return;
        }

        // Extract the token from the Authorization header
        String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();

        try {

            // Validate the token
            UserWeb user = userService.validateTokenGetUserWeb(token);

            Set<String> roles = new HashSet<>();
            for (RoleWeb role : user.getRoles()) {
                roles.add(role.getName());
            }
            UsernameJwtCredential credential = new UsernameJwtCredential(user, token, roles);
            credential.setToken(token);
            AuthenticationParameters authenticationParameters = AuthenticationParameters.withParams()
                    .credential(credential);
            AuthenticationStatus authenticationStatus = securityContext.authenticate(httpRequest, httpResponse, authenticationParameters);

            if (authenticationStatus != AuthenticationStatus.SUCCESS) {
                abortWithUnauthorized(requestContext);
            }

            UserSecurityContext.setCurrentUser(user);



        } catch (Exception e) {
            abortWithUnauthorized(requestContext);
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
