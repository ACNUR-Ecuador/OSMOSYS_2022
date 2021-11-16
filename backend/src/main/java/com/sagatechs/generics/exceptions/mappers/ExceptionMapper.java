package com.sagatechs.generics.exceptions.mappers;


import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.model.ExceptionModelWs;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        int code = 500;
        Status statusCode = Status.fromStatusCode(code);
        ExceptionModelWs errorResponse = new ExceptionModelWs(code,
                "Se produjo un error desconocido, por favor comunícate con el administrador del sistema. "+exception.getMessage());

        return Response.status(statusCode).entity(errorResponse).
                type(MediaType.APPLICATION_JSON).build();

    }

}