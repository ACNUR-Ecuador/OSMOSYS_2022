package com.sagatechs.generics.exceptions.mappers;


import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.model.ExceptionModelWs;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GeneralAppExceptionMapper implements ExceptionMapper<GeneralAppException> {

    @Override
    public Response toResponse(GeneralAppException exception) {
        Integer code = exception.getHttpcode();
        if (code == null) {
            code = 500;
        }
        Status statusCode = Status.fromStatusCode(code);
        ExceptionModelWs errorResponse = new ExceptionModelWs(code,
                exception.getMessage());

        return Response.status(statusCode).entity(errorResponse).
                type(MediaType.APPLICATION_JSON).build();

    }

}