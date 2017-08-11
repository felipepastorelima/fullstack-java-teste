package br.com.contabilizei.server.company;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.contabilizei.server.core.BaseTest;
import br.com.contabilizei.server.invoice.InvoiceDTO;
import br.com.contabilizei.server.invoice.InvoiceService;
import br.com.contabilizei.server.tax.TaxAnnex;
import br.com.contabilizei.server.tax.TaxRegime;
import br.com.contabilizei.server.tax.TaxService;
import junit.framework.Assert;

public class CompanyDeletionTest extends BaseTest {
	
	CompanyService service;
	CompanyDTO company;
	
	@Before
	public void tearUp() {		
		service = new CompanyService();
				
		company = new CompanyDTO();
		company.setName("Company 1");
		company.setCnpj("67676776000105");
		company.setTaxRegime(TaxRegime.SIMPLES_NACIONAL);
		company.getTaxAnnexes().add(TaxAnnex.COMERCIO);
		company.getTaxAnnexes().add(TaxAnnex.INDUSTRIA);
		company.setEmail("felipepastorelima@gmailcom");
		company = service.create(company);
	}
	
	@Test
	public void testDelete() {
		service.delete(company.getId());
		List<CompanyDTO> list = service.findAll();		
		Assert.assertEquals(0, list.size());
	}
	
	@Test
	public void testDeleteWithInvoices() {
		InvoiceDTO invoice = new InvoiceDTO();
		invoice.setCode(1L);
		invoice.setCompany(company);
		invoice.setAmount(new BigDecimal(100.99));
		invoice.setTaxAnnex(TaxAnnex.COMERCIO);
		invoice.setDescription("Descrição da nota fiscal");
		invoice.setIssueDate(new Date());		
		new InvoiceService().create(invoice);
		
		service.delete(company.getId());
		List<CompanyDTO> list = service.findAll();		
		Assert.assertEquals(0, list.size());		
	}
	
	@Test
	public void testDeleteWithTaxes() {
		InvoiceDTO invoice = new InvoiceDTO();
		invoice.setCode(1L);
		invoice.setCompany(company);
		invoice.setAmount(new BigDecimal(100.99));
		invoice.setTaxAnnex(TaxAnnex.COMERCIO);
		invoice.setDescription("Descrição da nota fiscal");
		invoice.setIssueDate(new Date());		
		new InvoiceService().create(invoice);
		
		new TaxService().generateForCompanyAtMonth(company.getId(), new Date());
		
		service.delete(company.getId());
		List<CompanyDTO> list = service.findAll();		
		Assert.assertEquals(0, list.size());		
	}	

}
