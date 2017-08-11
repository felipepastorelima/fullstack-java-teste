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

public class TaxLucroPresumidoTest extends BaseTest{
	
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
	public void generates() {
		taxService.generateForCompanyAtMonth(
			company.getId(),
			new Date()
		);
		
		List<TaxDTO> taxes = taxService.findAllByCompanyAndMonth(company.getId(), new Date());
		Assert.assertNotNull(taxes);
		Assert.assertEquals(3, taxes.size());
		Assert.assertEquals("IRPJ", taxes.get(0).getName());
		Assert.assertEquals("ISS", taxes.get(1).getName());
		Assert.assertEquals("Cofins", taxes.get(2).getName());
	}
	
	@Test
	public void correctAmount() {
		taxService.generateForCompanyAtMonth(
			company.getId(),
			new Date()
		);
		
		List<TaxDTO> taxes = taxService.findAllByCompanyAndMonth(company.getId(), new Date());

		Double irpjAmount = 72.0;
		Double issAmount = 30.0;
		Double cofinsAmount = 45.0;
		
		Assert.assertEquals(0, 
			BigDecimal.valueOf(irpjAmount)
				.compareTo(taxes.get(0).getAmount())
		);
		
		Assert.assertEquals(0, 
			BigDecimal.valueOf(issAmount)
				.compareTo(taxes.get(1).getAmount())
		);
		
		Assert.assertEquals(0, 
			BigDecimal.valueOf(cofinsAmount)
				.compareTo(taxes.get(2).getAmount())
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
		invoice.setAmount(new BigDecimal(1500));
		invoice.setDescription("Descrição da nota fiscal");
		invoice.setIssueDate(new Date());
		invoiceService.create(invoice);
		
		taxService.generateForCompanyAtMonth(
			company.getId(),
			new Date()
		);
		
		List<TaxDTO> taxes = taxService.findAllByCompanyAndMonth(company.getId(), new Date());

		Double irpjAmount = 144.0;
		Double issAmount = 60.0;
		Double cofinsAmount = 90.0;
		
		Assert.assertEquals(0, 
			BigDecimal.valueOf(irpjAmount)
				.compareTo(taxes.get(0).getAmount())
		);
		
		Assert.assertEquals(0, 
			BigDecimal.valueOf(issAmount)
				.compareTo(taxes.get(1).getAmount())
		);
		
		Assert.assertEquals(0, 
			BigDecimal.valueOf(cofinsAmount)
				.compareTo(taxes.get(2).getAmount())
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
		Assert.assertEquals(3, taxes.size());
		Assert.assertEquals(
			expectedDueDate, 
			taxes.get(0).getDueDate()
		);
		Assert.assertEquals(
			expectedDueDate, 
			taxes.get(1).getDueDate()
		);
		Assert.assertEquals(
			expectedDueDate, 
			taxes.get(2).getDueDate()
		);		
	}

}
