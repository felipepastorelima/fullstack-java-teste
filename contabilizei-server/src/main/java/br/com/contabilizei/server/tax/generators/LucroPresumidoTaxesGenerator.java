package br.com.contabilizei.server.tax.generators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.contabilizei.server.invoice.InvoiceDTO;
import br.com.contabilizei.server.tax.TaxDTO;
import br.com.contabilizei.server.tax.generators.single.CofinsTaxGenerator;
import br.com.contabilizei.server.tax.generators.single.IRPJTaxGenerator;
import br.com.contabilizei.server.tax.generators.single.ISSTaxGenerator;
import br.com.contabilizei.server.tax.generators.single.TaxGenerator;

public class LucroPresumidoTaxesGenerator implements TaxesGenerator{

	private final List<TaxGenerator> generators;

	public LucroPresumidoTaxesGenerator() {
		super();
		this.generators = Arrays.asList(
			new IRPJTaxGenerator(),
			new ISSTaxGenerator(),
			new CofinsTaxGenerator()
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
