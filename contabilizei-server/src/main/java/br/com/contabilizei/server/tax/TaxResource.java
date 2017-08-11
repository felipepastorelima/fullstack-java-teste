package br.com.contabilizei.server.tax;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.contabilizei.server.core.api.adapter.DateParam;

@Path("/company/{companyId}/tax")
public class TaxResource {

	/**
	 * Busca todos os impostos da empresa no mês informado.
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
		TaxService service = new TaxService();
		
		return Response.ok(
			service.findAllByCompanyAndMonth(companyId, referenceDate)
		).build();
	}
	
	/**
	 * Gera os impostos para a empresa naquele mês e ano, calculando o valor sobre suas notas fiscais.
	 * Caso a empresa já possuia impostos naquele mês e ano, estes serão descartados e criados novos.
	 * 
	 * @param companyId - Id da empresa
	 * @param referenceDate - Data de referência. Será considerado apenas o mês e o ano.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response generateForCompanyAtMonth(
		@PathParam("companyId") Long companyId,
		TaxReferenceDateDTO referenceDateDTO
	) {
		TaxService service = new TaxService();
				
		return Response.ok(
			service.generateForCompanyAtMonth(
				companyId, 
				referenceDateDTO.getReferenceDate()
			)
		).build();
	}
	
	/**
	 * Marca um imposto como pago
	 * 
	 * @param id - Id do imposto
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}/payed")
	public Response markAsPayed(
		@PathParam("id") Long id		
	) {
		TaxService service = new TaxService();
				
		return Response.ok(
			service.markAsPayed(
				id
			)
		).build();
	}
	
}
