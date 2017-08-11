package br.com.contabilizei.server.tax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.contabilizei.server.company.CompanyDTO;
import br.com.contabilizei.server.company.CompanyService;
import br.com.contabilizei.server.core.BaseTest;
import br.com.contabilizei.server.core.helpers.DateHelper;
import br.com.contabilizei.server.invoice.InvoiceDTO;
import br.com.contabilizei.server.invoice.InvoiceService;
import junit.framework.Assert;

public class TaxSimplesNacionalTest extends BaseTest{
	
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
		company.setName("Company 1");
		company.setCnpj("67676776000105");
		company.setTaxRegime(TaxRegime.SIMPLES_NACIONAL);
		company.getTaxAnnexes().add(TaxAnnex.COMERCIO);
		company.getTaxAnnexes().add(TaxAnnex.INDUSTRIA);
		company.getTaxAnnexes().add(TaxAnnex.PRESTACAO_DE_SERVICOS);
		company.setEmail("felipepastorelima@gmailcom");
		company = companyService.create(company);
		
		InvoiceDTO invoice = new InvoiceDTO();
		invoice.setCode(1L);
		invoice.setCompany(company);
		invoice.setAmount(new BigDecimal(1000));
		invoice.setTaxAnnex(TaxAnnex.COMERCIO);
		invoice.setDescription("Descrição da nota fiscal");
		invoice.setIssueDate(new Date());
		invoiceService.create(invoice);
		
		invoice = new InvoiceDTO();
		invoice.setCode(2L);
		invoice.setCompany(company);
		invoice.setAmount(new BigDecimal(5000));
		invoice.setTaxAnnex(TaxAnnex.PRESTACAO_DE_SERVICOS);
		invoice.setDescription("Descrição da nota fiscal");
		invoice.setIssueDate(new Date());
		invoiceService.create(invoice);
	}
	
	@Test
	public void correctName() {
		taxService.generateForCompanyAtMonth(
			company.getId(),
			new Date()
		);
		
		List<TaxDTO> taxes = taxService.findAllByCompanyAndMonth(company.getId(), new Date());
		Assert.assertNotNull(taxes);
		Assert.assertEquals(1, taxes.size());
		Assert.assertEquals("Simples Nacional", taxes.get(0).getName());
	}
	
	@Test
	public void correctAmount() {
		taxService.generateForCompanyAtMonth(
			company.getId(),
			new Date()
		);
		
		List<TaxDTO> taxes = taxService.findAllByCompanyAndMonth(company.getId(), new Date());

		Assert.assertEquals(0, 
			BigDecimal.valueOf(610)
				.compareTo(taxes.get(0).getAmount())
		);
	}
	
	@Test
	public void regenerates() {
		taxService.generateForCompanyAtMonth(
			company.getId(),
			new Date()
		);
		
		InvoiceDTO invoice = new InvoiceDTO();
		invoice.setCode(3L);
		invoice.setCompany(company);
		invoice.setAmount(new BigDecimal(1000));
		invoice.setTaxAnnex(TaxAnnex.INDUSTRIA);
		invoice.setDescription("Descrição da nota fiscal");
		invoice.setIssueDate(new Date());
		invoiceService.create(invoice);
		
		taxService.generateForCompanyAtMonth(
			company.getId(),
			new Date()
		);
		
		List<TaxDTO> taxes = taxService.findAllByCompanyAndMonth(company.getId(), new Date());

		Assert.assertNotNull(taxes);
		Assert.assertEquals(1, taxes.size());
		Assert.assertEquals(0, 
			BigDecimal.valueOf(695)
				.compareTo(taxes.get(0).getAmount())
		);
	}
	
	@Test
	public void considersReferenceDate() {
		taxService.generateForCompanyAtMonth(
			company.getId(),
			new Date()
		);
		
		Date oneMonthAgo = DateHelper.addTo(new Date(), -1, Calendar.MONTH);
			
		List<TaxDTO> taxes = taxService.findAllByCompanyAndMonth(
			company.getId(), 
			oneMonthAgo
		);
		
		Assert.assertNotNull(taxes);
		Assert.assertEquals(0, taxes.size());
	}

	@Test
	public void correctDueDate() {
		taxService.generateForCompanyAtMonth(
			company.getId(),
			new Date()
		);
		
		List<TaxDTO> taxes = taxService.findAllByCompanyAndMonth(
			company.getId(), 
			new Date()
		);
		
		Date expectedDueDate = Date.from(
			LocalDate
				.now()
				.plusMonths(1)
				.withDayOfMonth(20)
			.atStartOfDay(ZoneId.systemDefault()).toInstant()
		); 
		
		Assert.assertNotNull(taxes);
		Assert.assertEquals(1, taxes.size());
		Assert.assertEquals(
			expectedDueDate, 
			taxes.get(0).getDueDate()
		);
	}
	
	
}
