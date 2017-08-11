package br.com.contabilizei.server.tax.generators.single;

import java.util.List;

import br.com.contabilizei.server.invoice.InvoiceDTO;
import br.com.contabilizei.server.tax.TaxDTO;

public interface TaxGenerator {

	public TaxDTO generateFor(List<InvoiceDTO> invoices);
	
}
