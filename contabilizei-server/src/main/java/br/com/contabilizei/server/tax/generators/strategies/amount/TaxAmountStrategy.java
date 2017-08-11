package br.com.contabilizei.server.tax.generators.strategies.amount;

import java.math.BigDecimal;
import java.util.List;

import br.com.contabilizei.server.invoice.InvoiceDTO;

public interface TaxAmountStrategy {

	public BigDecimal calculateFor(List<InvoiceDTO> invoices);
	
}
