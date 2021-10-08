package com.sagatechs.generics.exceptions.mappers;


import com.sagatechs.generics.exceptions.AccessDeniedException;
import com.sagatechs.generics.model.ExceptionModelWs;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AccessDeniedExceptionMapper implements ExceptionMapper<AccessDeniedException> {

	@Override
	public Response toResponse(AccessDeniedException exception) {
		ExceptionModelWs errorResponse = new ExceptionModelWs(Status.FORBIDDEN.getStatusCode(),
				exception.getMessage());
		return Response.status(Status.FORBIDDEN).entity(errorResponse).type(MediaType.APPLICATION_JSON).build();

	}

}