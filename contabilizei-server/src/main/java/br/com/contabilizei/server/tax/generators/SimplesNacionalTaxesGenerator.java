package br.com.contabilizei.server.tax.generators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.contabilizei.server.invoice.InvoiceDTO;
import br.com.contabilizei.server.tax.TaxDTO;
import br.com.contabilizei.server.tax.generators.single.SimplesNacionalTaxGenerator;
import br.com.contabilizei.server.tax.generators.single.TaxGenerator;

public class SimplesNacionalTaxesGenerator implements TaxesGenerator{

	private final List<TaxGenerator> generators;

	public SimplesNacionalTaxesGenerator() {
		super();
		this.generators = Arrays.asList(
			new SimplesNacionalTaxGenerator()
		);
	}

	@Override
	public List<TaxDTO> generateFor(List<InvoiceDTO> invoices) {
		List<TaxDTO> taxes = new ArrayList<TaxDTO>();
		for(TaxGenerator generator: generators) {
			taxes.add(generator.generateFor(invoices));
		}
		return taxes;
	}
	
}
