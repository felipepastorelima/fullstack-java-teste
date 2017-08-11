package br.com.contabilizei.server.core.api;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import br.com.contabilizei.server.core.exception.BadRequestException;
import br.com.contabilizei.server.core.exception.ServerErrorExceptionMapper;

@ApplicationPath("/api")
public class ApiConfiguration extends ResourceConfig
{
    public ApiConfiguration() {
    	packages("br.com.contabilizei");
    	register(JacksonFeature.class);
    	register(CorsResponseFilter.class);
    	register(ServerErrorExceptionMapper.class);
    	register(BadRequestException.class);
    }
}
