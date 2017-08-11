package br.com.contabilizei.server.tax.generators.strategies.dueDate;

import java.util.Date;

public interface TaxDueDateStrategy {

	public Date calculateFor(Date reference);
	
}
