package br.com.contabilizei.server.tax.generators;

import java.util.List;

import br.com.contabilizei.server.invoice.InvoiceDTO;
import br.com.contabilizei.server.tax.TaxDTO;

public interface TaxesGenerator {

	public List<TaxDTO> generateFor(List<InvoiceDTO> invoices);
	
}
