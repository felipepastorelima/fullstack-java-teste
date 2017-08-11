package br.com.contabilizei.server.company;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.contabilizei.server.core.BaseTest;
import br.com.contabilizei.server.tax.TaxAnnex;
import br.com.contabilizei.server.tax.TaxRegime;
import junit.framework.Assert;

public class CompanyListingTest extends BaseTest {
	
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
	public void testFindAll() {
		List<CompanyDTO> list = service.findAll();
		Assert.assertNotNull(list);
		Assert.assertEquals(1, list.size());
	}
	
	@Test
	public void testFindById() {
		company = service.find(company.getId());
		Assert.assertNotNull(company);
	}

}
