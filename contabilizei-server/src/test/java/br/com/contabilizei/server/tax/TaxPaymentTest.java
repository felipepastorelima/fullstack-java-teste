package br.com.contabilizei.server.tax;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.contabilizei.server.company.CompanyDTO;
import br.com.contabilizei.server.company.CompanyService;
import br.com.contabilizei.server.core.BaseTest;
import br.com.contabilizei.server.invoice.InvoiceDTO;
import br.com.contabilizei.server.invoice.InvoiceService;
import junit.framework.Assert;

public class TaxPaymentTest  extends BaseTest{
	
	CompanyDTO company;
	InvoiceService invoiceService;
	CompanyService companyService;
	TaxService taxService;
	
	@Before
	public void tearUp() {
		companyService = new CompanyService();
		invoiceService = new InvoiceService();
		taxService = new TaxService();
		
		company = new CompanyDTO();
		company.setName("Company 2");
		company.setCnpj("32413343000174");
		company.setTaxRegime(TaxRegime.LUCRO_PRESUMIDO);
		company.setEmail("felipepastorelima@gmail.com");
		company = new CompanyService().create(company);
		
		InvoiceDTO invoice = new InvoiceDTO();
		invoice.setCode(1L);
		invoice.setCompany(company);
		invoice.setAmount(new BigDecimal(1000));
		invoice.setDescription("Descrição da nota fiscal");
		invoice.setIssueDate(new Date());
		invoiceService.create(invoice);
		
		invoice = new InvoiceDTO();
		invoice.setCode(2L);
		invoice.setCompany(company);
		invoice.setAmount(new BigDecimal(500));
		invoice.setDescription("Descrição da nota fiscal");
		invoice.setIssueDate(new Date());
		invoiceService.create(invoice);
	}
	
	@Test
	public void marksAsPayed() {
		taxService.generateForCompanyAtMonth(
			company.getId(),
			new Date()
		);
		
		List<TaxDTO> taxes = taxService.findAllByCompanyAndMonth(company.getId(), new Date());
		
		taxService.markAsPayed(taxes.get(0).getId());
		
		taxes = taxService.findAllByCompanyAndMonth(company.getId(), new Date());
		Assert.assertTrue(taxes.get(0).getPayed());
		Assert.assertFalse(taxes.get(1).getPayed());
		Assert.assertFalse(taxes.get(2).getPayed());
	}
	

}