package br.com.contabilizei.server.tax.generators.strategies.amount;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.math.BigDecimal;
import java.util.List;

import br.com.contabilizei.server.invoice.InvoiceDTO;

public class TaxAmountPercentageStrategy 
	implements TaxAmountStrategy{

	private BigDecimal percentage;
		
	public TaxAmountPercentageStrategy(Integer percentage) {
		this(BigDecimal.valueOf(percentage));
	}

	public TaxAmountPercentageStrategy(Double percentage) {
		this(BigDecimal.valueOf(percentage));
	}
	
	public TaxAmountPercentageStrategy(BigDecimal percentage) {
		super();
		checkNotNull(percentage, "Porcentagem não pode ser vazia!");
		checkArgument(
			percentage.compareTo(BigDecimal.ZERO) > 0, 
			"Porcentagem deve zer maior que zero!"
		);
		this.percentage = percentage;
	}

	@Override
	public BigDecimal calculateFor(List<InvoiceDTO> invoices) {
		checkNotNull(invoices, "Notas fiscais estão vazias!");
		checkArgument(!invoices.isEmpty(), "Notas fiscais estão vazias!");
		
		BigDecimal sum = sumAmount(invoices);
		
		if (sum.equals(BigDecimal.ZERO)) {
			return BigDecimal.ZERO;
		}
		
		return sum
			.multiply(percentage)
			.divide(BigDecimal.valueOf(100));
	}

	private BigDecimal sumAmount(List<InvoiceDTO> invoices) {
		BigDecimal total = new BigDecimal("0");
		for(InvoiceDTO invoice: invoices) {
			total = total.add(invoice.getAmount());
		}
		return total;
	}
	
}
