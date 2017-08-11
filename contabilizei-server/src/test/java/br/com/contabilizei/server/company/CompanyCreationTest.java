package br.com.contabilizei.server.company;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Strings;

import br.com.contabilizei.server.core.BaseTest;
import br.com.contabilizei.server.core.exception.ValidationException;
import br.com.contabilizei.server.tax.TaxAnnex;
import br.com.contabilizei.server.tax.TaxRegime;
import junit.framework.Assert;

public class CompanyCreationTest extends BaseTest {
	
	CompanyDTO company;
	CompanyService service;
	
	@Before
	public void tearUp() {
		company = new CompanyDTO();
		company.setName("Company 1");
		company.setCnpj("67676776000105");
		company.setTaxRegime(TaxRegime.SIMPLES_NACIONAL);
		company.getTaxAnnexes().add(TaxAnnex.COMERCIO);
		company.getTaxAnnexes().add(TaxAnnex.INDUSTRIA);
		company.setEmail("felipepastorelima@gmailcom");
		
		service = new CompanyService();
	}
	
	@Test
	public void testCreate() {		
		company = service.create(company);
		Assert.assertNotNull(company.getId());
	}
	
	@Test
	public void testCreateWithTaxAnnexes() {		
		company = service.create(company);
		company = service.findAll().get(0);
		
		Assert.assertNotNull(company.getId());
		Assert.assertNotNull(company.getTaxAnnexes());
		Assert.assertEquals(TaxAnnex.INDUSTRIA, company.getTaxAnnexes().get(1));
	}
	
	@Test
	public void testClearTaxAnnexesIfLucroPresumido() {		
		company.setTaxRegime(TaxRegime.LUCRO_PRESUMIDO);
		company = service.create(company);
		company = service.findAll().get(0);
		
		Assert.assertNotNull(company.getId());		
		Assert.assertTrue("Anexos são salvos mesmo se for lucro presumido!", company.getTaxAnnexes().isEmpty());
	}	
	
	@Test(expected=ValidationException.class)
	public void testAnnexNotNullIfSimplesNacional() {	
		company.setTaxAnnexes(null);
		company = service.create(company);		
	}	
	
	@Test
	public void testSanitizeCnpj() {
		company.setCnpj("67.676.776/0001-05");
		company = new CompanyService().create(company);
		Assert.assertEquals("67676776000105", company.getCnpj());
	}
	
	@Test(expected=ValidationException.class)
	public void testUniqueCNPJ() {
		new CompanyService().create(company);
		
		// Limpa o ID para aproveitar os dados
		company.setId(null);
		
		company.setCnpj("67.676.776/0001-05");
		company = new CompanyService().create(company);		
	}
	
	@Test(expected=ValidationException.class)
	public void testValidateNameNull() {
		company.setName(null);
		company = new CompanyService().create(company);		
	}
	
	@Test(expected=ValidationException.class)
	public void testValidateNameLarge() {
		company.setName(
			Strings.repeat("i", 256)
		);
		company = new CompanyService().create(company);		
	}

	@Test(expected=ValidationException.class)
	public void testValidateCnpjNull() {
		company.setCnpj(null);
		company = new CompanyService().create(company);		
	}
	
	@Test(expected=ValidationException.class)
	public void testValidateCnpjInvalid() {
		company.setCnpj("invalid");
		company = new CompanyService().create(company);		
	}
	
	@Test(expected=ValidationException.class)
	public void testValidateEmailNull() {
		company.setEmail(null);
		company = new CompanyService().create(company);		
	}
	
	@Test(expected=ValidationException.class)
	public void testValidateEmailLarge() {
		company.setEmail(
			Strings.repeat("i", 256)
		);
		company = new CompanyService().create(company);		
	}
	
}
