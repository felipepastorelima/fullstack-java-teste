package br.com.contabilizei.server.core.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

	@Override
	public Response toResponse(BadRequestException exception) {
		ResponseBuilder builder = Response
			.status(Status.BAD_REQUEST)
			.entity(jsonResponse(exception.getMessage()))
			.type(MediaType.APPLICATION_JSON);
				
		return builder.build();
	}
	
	public String jsonResponse(String message) {
		return "{\"message\":\""+message+"\"}";
	}

}
