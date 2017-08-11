package br.com.contabilizei.server.tax.generators.single;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import br.com.contabilizei.server.invoice.InvoiceDTO;
import br.com.contabilizei.server.tax.TaxDTO;
import br.com.contabilizei.server.tax.generators.strategies.amount.TaxAmountPercentageStrategy;
import br.com.contabilizei.server.tax.generators.strategies.amount.TaxAmountStrategy;
import br.com.contabilizei.server.tax.generators.strategies.dueDate.TaxDueDate20thDayOfNextMonthStrategy;
import br.com.contabilizei.server.tax.generators.strategies.dueDate.TaxDueDateStrategy;

public class IRPJTaxGenerator implements TaxGenerator {

	private final String name;
	private final TaxAmountStrategy amountStrategy;
	private final TaxDueDateStrategy dueDateStrategy;
	
	public IRPJTaxGenerator() {
		this.name = "IRPJ";
		this.amountStrategy = new TaxAmountPercentageStrategy(
			4.8
		);
		this.dueDateStrategy = new TaxDueDate20thDayOfNextMonthStrategy();
	}


	@Override
	public TaxDTO generateFor(List<InvoiceDTO> invoices) {
		checkNotNull(invoices, "Notas fiscais estão vazias!");
		checkArgument(!invoices.isEmpty(), "Notas fiscais estão vazias!");
		
		TaxDTO tax = new TaxDTO();
		tax.setName(name);
		tax.setAmount(amountStrategy.calculateFor(invoices));
		tax.setReferenceDate(InvoiceDTO.maxIssueDateOf(invoices));
		tax.setDueDate(dueDateStrategy.calculateFor(tax.getReferenceDate()));
		tax.setCompany(invoices.get(0).getCompany());
		
		return tax;
	}

}
