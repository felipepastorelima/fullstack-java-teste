package br.com.contabilizei.server.tax;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.contabilizei.server.company.CompanyDTO;
import br.com.contabilizei.server.company.CompanyService;
import br.com.contabilizei.server.core.exception.ValidationException;
import br.com.contabilizei.server.invoice.InvoiceDTO;
import br.com.contabilizei.server.invoice.InvoiceService;
import br.com.contabilizei.server.tax.generators.TaxesGenerator;
import br.com.contabilizei.server.tax.generators.TaxesGeneratorByTaxRegime;

public class TaxService {
	
	TaxDAO dao;
	CompanyService companyService;
	InvoiceService invoiceService;
	
	public TaxService() {		
		this.dao = new TaxDAO();
		this.companyService = new CompanyService();
		this.invoiceService = new InvoiceService();
	}
	
	public TaxDTO markAsPayed(
		Long id
	) {
		return TaxDTO.fromEntity(
			dao.markAsPayed(id)
		);
	}		

	public List<TaxDTO> findAllByCompanyAndMonth(
		Long companyId,
		Date referenceDate
	) {
		return TaxDTO.fromEntityList(
			dao.findAllByCompanyAndMonth(companyId, referenceDate)
		);
	}	
	
	public List<TaxDTO> generateForCompanyAtMonth(
		Long companyId,
		Date referenceDate
	) {
		CompanyDTO company = findAndValidateCompany(companyId);
		TaxesGenerator generator = TaxesGeneratorByTaxRegime.get(company.getTaxRegime());
		List<InvoiceDTO> invoices = invoiceService.findAllByCompanyAndMonth(companyId, referenceDate);
		
		List<TaxDTO> taxes = new ArrayList<>();
		if (invoices != null && !invoices.isEmpty()) {
			taxes = generator.generateFor(invoices);			
		}
		
		return TaxDTO.fromEntityList(
			dao.deleteAndCreateForCompanyAtMonth(
				companyId,
				referenceDate, 
				Tax.fromDTOList(taxes)
			)
		);
	}
		
	private CompanyDTO findAndValidateCompany(Long companyId) {
		try{
			checkNotNull(companyId, "O id da empresa está vazio!");
			checkArgument(companyId > 0, "O id da empresa está vazio!");
			
			try{
				return companyService.find(companyId);
			}catch(Exception e) {
				throw new ValidationException(
					String.format("Não foi encontrada empresa de id '%s'!", companyId)
				);
			}
		}catch(Exception e) {
			throw new ValidationException(e.getMessage());
		}
	}
	
}