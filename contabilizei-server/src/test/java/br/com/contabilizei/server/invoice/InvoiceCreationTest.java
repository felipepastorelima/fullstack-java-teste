package br.com.contabilizei.server.invoice;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Strings;

import br.com.contabilizei.server.company.CompanyDTO;
import br.com.contabilizei.server.company.CompanyService;
import br.com.contabilizei.server.core.BaseTest;
import br.com.contabilizei.server.core.exception.ValidationException;
import br.com.contabilizei.server.tax.TaxAnnex;
import br.com.contabilizei.server.tax.TaxRegime;
import junit.framework.Assert;

public class InvoiceCreationTest extends BaseTest {
		
		CompanyDTO companySimplesNacional;
		CompanyDTO companyLucroPresumido;
		InvoiceService service;
		InvoiceDTO invoice;
		
		@Before
		public void tearUp() {
			companySimplesNacional = new CompanyDTO();
			companySimplesNacional.setName("Company 1");
			companySimplesNacional.setCnpj("67676776000105");
			companySimplesNacional.setTaxRegime(TaxRegime.SIMPLES_NACIONAL);
			companySimplesNacional.getTaxAnnexes().add(TaxAnnex.COMERCIO);
			companySimplesNacional.getTaxAnnexes().add(TaxAnnex.INDUSTRIA);
			companySimplesNacional.setEmail("felipepastorelima@gmailcom");
			companySimplesNacional = new CompanyService().create(companySimplesNacional);
			
			companyLucroPresumido = new CompanyDTO();
			companyLucroPresumido.setName("Company 2");
			companyLucroPresumido.setCnpj("32413343000174");
			companyLucroPresumido.setTaxRegime(TaxRegime.LUCRO_PRESUMIDO);
			companyLucroPresumido.setEmail("felipepastorelima@gmail.com");
			companyLucroPresumido = new CompanyService().create(companyLucroPresumido);
			
			invoice = new InvoiceDTO();
			invoice.setCode(1L);
			invoice.setCompany(companySimplesNacional);
			invoice.setAmount(new BigDecimal(100.99));
			invoice.setTaxAnnex(TaxAnnex.COMERCIO);
			invoice.setDescription("Descrição da nota fiscal");
			invoice.setIssueDate(new Date());		
			
			service = new InvoiceService();
		}
		
		@Test
		public void testCreate() {		
			invoice = service.create(invoice);
			Assert.assertNotNull(invoice.getId());
		}
		
		@Test
		public void accept99biAmount() {		
			invoice.setAmount(new BigDecimal("99999999999"));
			invoice = service.create(invoice);
			Assert.assertNotNull(invoice.getId());
		}		
		
		@Test
		public void clearTaxAnnexIfLucroPresumido() {
			invoice.setCompany(companyLucroPresumido);
			invoice = service.create(invoice);
			invoice = service.find(invoice.getId());
			Assert.assertNull(invoice.getTaxAnnex());
		}
	
		@Test(expected=ValidationException.class)
		public void requiresTaxAnnexIfSimplesNacional() {
			invoice.setTaxAnnex(null);
			invoice = service.create(invoice);
		}		
		
		@Test(expected=ValidationException.class)
		public void testAcceptJustCompanyRelatedTaxAnnexes() {
			invoice.setTaxAnnex(TaxAnnex.PRESTACAO_DE_SERVICOS);
			service.create(invoice);
		}
		
		@Test(expected=ValidationException.class)
		public void acceptsJustPositiveAmount() {
			invoice.setAmount(new BigDecimal("-1"));
			service.create(invoice);
		}
		
		@Test(expected=ValidationException.class)
		public void doesntAcceptZeroAmount() {
			invoice.setAmount(new BigDecimal("0"));
			service.create(invoice);
		}		
		
		@Test(expected=ValidationException.class)
		public void doesntAcceptInvalidCompany() {
			invoice.setCompany(new CompanyDTO(999L));
			service.create(invoice);
		}	
		
		@Test(expected=ValidationException.class)
		public void doesntAcceptInvalidCode() {
			invoice.setCode(0L);
			service.create(invoice);
		}
		
		@Test(expected=ValidationException.class)
		public void doesntAcceptNullCode() {
			invoice.setCode(0L);
			service.create(invoice);
		}
		
		@Test(expected=ValidationException.class)
		public void doesntAcceptRepeatedCode() {			
			service.create(invoice);
			
			// Apaga o Id para reaproveitar os dados
			invoice.setId(null);
			service.create(invoice);			
		}
		
		@Test(expected=ValidationException.class)
		public void doesntAcceptNullIssueDate() {
			invoice.setIssueDate(null);
			service.create(invoice);		
		}
		
		@Test(expected=ValidationException.class)
		public void doesntAcceptDescriptionNull() {
			invoice.setDescription(null);
			service.create(invoice);		
		}
		
		@Test(expected=ValidationException.class)
		public void doesntAcceptDescriptionLong() {
			invoice.setDescription(
				Strings.repeat("i", 256)
			);
			
			service.create(invoice);		
		}
}

