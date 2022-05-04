package com.sagatechs.generics.exceptions.mappers;


import com.sagatechs.generics.exceptions.AuthorizationException;
import com.sagatechs.generics.model.ExceptionModelWs;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthorizationExceptionMapper implements ExceptionMapper<AuthorizationException> {

    @Override
    public Response toResponse(AuthorizationException exception) {

        Status statusCode = Status.UNAUTHORIZED;

        ExceptionModelWs errorResponse = new ExceptionModelWs(statusCode.getStatusCode(),
                exception.getMessage());

        return Response.status(statusCode).entity(errorResponse).
                type(MediaType.APPLICATION_JSON).build();

    }




}