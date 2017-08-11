package br.com.contabilizei.server.company;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/company")
public class CompanyResource {
	
	/**
	 * Busca todas as empresas do sistema.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAll() {
		CompanyService service = new CompanyService();
		
		return Response.ok(
			service.findAll()
		).build();
	}
	
	/**
	 * Busca empresa por Id
	 *
	 * @param id - Id da empresa
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response find(@PathParam("id") Long id) {
		CompanyService service = new CompanyService();
		
		return Response.ok(
			service.find(id)
		).build();
	}	
	
	/**
	 * Cria uma nova empresa
	 * 
	 * @param company - Dados da empresa
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(CompanyDTO company) {
		CompanyService service = new CompanyService();
		
		return Response.ok(
			service.create(company)
		).build();
	}
	
	/**
	 * Delete uma empresa
	 * 
	 * @param id - Id da empresa
	 */
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") Long id) {
		CompanyService service = new CompanyService();
		
		service.delete(id);
		return Response.ok().build();
	}	

}
