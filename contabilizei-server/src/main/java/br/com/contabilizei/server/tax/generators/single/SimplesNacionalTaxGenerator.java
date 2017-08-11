package br.com.contabilizei.server.tax.generators.single;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import br.com.contabilizei.server.invoice.InvoiceDTO;
import br.com.contabilizei.server.tax.TaxDTO;
import br.com.contabilizei.server.tax.generators.strategies.amount.TaxAmountSimplesNacionalStrategy;
import br.com.contabilizei.server.tax.generators.strategies.amount.TaxAmountStrategy;
import br.com.contabilizei.server.tax.generators.strategies.dueDate.TaxDueDate20thDayOfNextMonthStrategy;
import br.com.contabilizei.server.tax.generators.strategies.dueDate.TaxDueDateStrategy;

public class SimplesNacionalTaxGenerator implements TaxGenerator {

	private final String name;
	private final TaxAmountStrategy taxAmountStrategy;
	private final TaxDueDateStrategy dueDateStrategy;
	
	public SimplesNacionalTaxGenerator() {
		this.name = "Simples Nacional";
	
		this.taxAmountStrategy = new TaxAmountSimplesNacionalStrategy();
		this.dueDateStrategy = new TaxDueDate20thDayOfNextMonthStrategy();
	}

	@Override
	public TaxDTO generateFor(List<InvoiceDTO> invoices) {
		checkNotNull(invoices, "Notas fiscais estão vazias!");
		checkArgument(!invoices.isEmpty(), "Notas fiscais estão vazias!");
		
		TaxDTO tax = new TaxDTO();
		tax.setName(name);
		tax.setAmount(this.taxAmountStrategy.calculateFor(invoices));
		tax.setReferenceDate(InvoiceDTO.maxIssueDateOf(invoices));
		tax.setDueDate(dueDateStrategy.calculateFor(tax.getReferenceDate()));
		tax.setCompany(invoices.get(0).getCompany());
		
		return tax;
	}
	

	
}
