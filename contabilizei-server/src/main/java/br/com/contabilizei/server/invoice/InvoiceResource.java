package br.com.contabilizei.server.invoice;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.contabilizei.server.company.CompanyDTO;
import br.com.contabilizei.server.core.api.adapter.DateParam;

@Path("/company/{companyId}/invoice")
public class InvoiceResource {

	/**
	 * Busca todas as notas fiscais da empresa no mês informado.
	 * 
	 * @param companyId - Id da empresa
	 * @param referenceDate - Data de referência. Será considerado apenas o mês e o ano.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllByCompanyAndMonth(
		@PathParam("companyId") Long companyId,
		@QueryParam("referenceDate") DateParam referenceDate
	) {
		InvoiceService service = new InvoiceService();
		
		return Response.ok(
			service.findAllByCompanyAndMonth(companyId, referenceDate)
		).build();
	}
	
	/**
	 * Busca uma nota fiscal por Id.
	 * 
	 * @param id - Id da nota fiscal
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response find(@PathParam("id") Long id) {
		InvoiceService service = new InvoiceService();
		
		return Response.ok(
			service.find(id)
		).build();
	}	
	
	/**
	 * Cria uma nova nota fiscal.
	 *
	 * @param companyId - Id da empresa referente a nota fiscal
	 * @param invoice - Dados da nota fiscal 
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(
		@PathParam("companyId") Long companyId,
		InvoiceDTO invoice
	) {
		InvoiceService service = new InvoiceService();
		
		invoice.setCompany(new CompanyDTO(companyId));
		
		return Response.ok(
			service.create(invoice)
		).build();
	}
	
	/**
	 * Deleta uma nota fiscal.
	 * 
	 * @param id - Id da nota fiscal 
	 */
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") Long id) {
		InvoiceService service = new InvoiceService();
		
		service.delete(id);
		return Response.ok().build();
	}
	
}
