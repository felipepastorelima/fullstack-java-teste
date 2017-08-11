package br.com.contabilizei.server.core.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ServerErrorExceptionMapper implements ExceptionMapper<Exception> {
	 
	@Override
	public Response toResponse(final Exception exception) {
		ResponseBuilder builder = Response
			.status(Status.INTERNAL_SERVER_ERROR)
			.entity(jsonResponse())
			.type(MediaType.APPLICATION_JSON);
			
		return builder.build();
	}
	
	public String jsonResponse() {
		return "{\"message\":\"Ocorreu um erro interno\"}";
	}

}
