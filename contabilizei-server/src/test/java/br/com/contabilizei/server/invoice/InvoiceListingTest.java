package br.com.contabilizei.server.invoice;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.contabilizei.server.company.CompanyDTO;
import br.com.contabilizei.server.company.CompanyService;
import br.com.contabilizei.server.core.BaseTest;
import br.com.contabilizei.server.tax.TaxAnnex;
import br.com.contabilizei.server.tax.TaxRegime;
import junit.framework.Assert;

public class InvoiceListingTest extends BaseTest {
	
	InvoiceService service;
	InvoiceDTO invoice;
	CompanyDTO company;
	
	@Before
	public void tearUp() {
		company = new CompanyDTO();
		company.setName("Company 1");
		company.setCnpj("67676776000105");
		company.setTaxRegime(TaxRegime.SIMPLES_NACIONAL);
		company.getTaxAnnexes().add(TaxAnnex.COMERCIO);
		company.getTaxAnnexes().add(TaxAnnex.INDUSTRIA);
		company.setEmail("felipepastorelima@gmailcom");
		company = new CompanyService().create(company);
		
		invoice = new InvoiceDTO();
		invoice.setCode(1L);
		invoice.setCompany(company);
		invoice.setAmount(new BigDecimal(100.99));
		invoice.setTaxAnnex(TaxAnnex.COMERCIO);
		invoice.setDescription("Descrição da nota fiscal");
		invoice.setIssueDate(new Date());		
		
		service = new InvoiceService();
		service.create(invoice);
	}
	
	@Test
	public void testList() {		
		List<InvoiceDTO> invoices = service.findAllByCompanyAndMonth(company.getId(), new Date());
		Assert.assertNotNull(invoices);
		Assert.assertEquals(1, invoices.size());
	}

}
