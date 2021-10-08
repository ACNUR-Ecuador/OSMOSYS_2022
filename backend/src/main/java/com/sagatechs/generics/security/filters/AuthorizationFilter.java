package com.sagatechs.generics.security.filters;

import com.sagatechs.generics.exceptions.AuthorizationException;
import com.sagatechs.generics.security.annotations.Secured;
import org.jboss.logging.Logger;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.Dependent;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Role authorization filter.
 *
 * @author sebas
 */
@Provider
@Dependent
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;
    @Context
    private ContainerRequestContext requestContext;

    private final static Logger LOGGER = Logger.getLogger(AuthorizationFilter.class);

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {

        Method method = resourceInfo.getResourceMethod();
        this.requestContext=requestContext;

        // LOGGER.info(requestContext.getUriInfo().getRequestUri().toURL());

        if (!method.isAnnotationPresent(Secured.class)) {
            return;
        }
        
        // @DenyAll on the method takes precedence over @RolesAllowed and @PermitAll
        if (method.isAnnotationPresent(DenyAll.class)) {
            throw new AuthorizationException("No tienes permiso para usar este recurso.");
        }

        // @RolesAllowed on the method takes precedence over @PermitAll
        RolesAllowed rolesAllowed = method.getAnnotation(RolesAllowed.class);
        if (rolesAllowed != null) {
            performAuthorization(rolesAllowed.value(), requestContext);
            return;
        }

        // @PermitAll on the method takes precedence over @RolesAllowed on the class
        if (method.isAnnotationPresent(PermitAll.class)) {
            // Do nothing
            return;
        }

        // @DenyAll can't be attached to classes

        // @RolesAllowed on the class takes precedence over @PermitAll on the class
        rolesAllowed = resourceInfo.getResourceClass().getAnnotation(RolesAllowed.class);
        if (rolesAllowed != null) {
            performAuthorization(rolesAllowed.value(), requestContext);
        }

        // @PermitAll on the class
        if (resourceInfo.getResourceClass().isAnnotationPresent(PermitAll.class)) {
            // Do nothing
            return;
        }

        // Authentication is required for non-annotated methods
        if (!isAuthenticated()) {
            throw new AuthorizationException("No tienes permiso para usar este recurso.");
        }
    }

    /**
     * Perform authorization based on roles.
     *
     * @param rolesAllowed
     * @param requestContext
     */
    private void performAuthorization(String[] rolesAllowed, ContainerRequestContext requestContext) {

        if (rolesAllowed.length > 0 && isAuthenticated()) {
            throw new AuthorizationException("No tienes permiso para usar este recurso.");
        }

        for (final String role : rolesAllowed) {
            if (requestContext.getSecurityContext().isUserInRole(role)) {
                return;
            }
        }

        throw new AuthorizationException("No tienes permiso para usar este recurso.");
    }

    /**
     * Check if the user is authenticated.

     * @return
     */
    private boolean isAuthenticated() {
        return this.requestContext.getSecurityContext().getUserPrincipal() != null;
    }
}