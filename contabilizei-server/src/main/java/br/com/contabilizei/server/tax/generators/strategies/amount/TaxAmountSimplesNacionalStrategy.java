package br.com.contabilizei.server.tax.generators.strategies.amount;

import static com.google.common.base.Preconditions.checkNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.contabilizei.server.invoice.InvoiceDTO;
import br.com.contabilizei.server.tax.TaxAnnex;

public class TaxAmountSimplesNacionalStrategy implements TaxAmountStrategy {

	private final HashMap<TaxAnnex, TaxAmountStrategy> groupedAmountStrategies;
	
	public TaxAmountSimplesNacionalStrategy() {
		super();
		this.groupedAmountStrategies = new HashMap<>();
		
		this.groupedAmountStrategies.put(
			TaxAnnex.COMERCIO,
			new TaxAmountPercentageStrategy(6)
		);
		this.groupedAmountStrategies.put(
			TaxAnnex.INDUSTRIA,
			new TaxAmountPercentageStrategy(8.5)
		);
		this.groupedAmountStrategies.put(
			TaxAnnex.PRESTACAO_DE_SERVICOS,
			new TaxAmountPercentageStrategy(11)
		);
	}

	@Override
	public BigDecimal calculateFor(List<InvoiceDTO> invoices) {
		HashMap<TaxAnnex, List<InvoiceDTO>> groupedInvoices = groupInvoicesByTaxAnnex(invoices);

		BigDecimal total = new BigDecimal(0);
		
		for(TaxAnnex taxAnnex: groupedInvoices.keySet()) {
			TaxAmountStrategy amountStrategy = groupedAmountStrategies.get(taxAnnex);
			List<InvoiceDTO> taxAnnexInvoices = groupedInvoices.get(taxAnnex);
			
			total = total.add(
				amountStrategy.calculateFor(taxAnnexInvoices)
			);
		}
		
		return total;
	}
	
	private HashMap<TaxAnnex, List<InvoiceDTO>> groupInvoicesByTaxAnnex(List<InvoiceDTO> invoices) {
		HashMap<TaxAnnex, List<InvoiceDTO>> invoicesByTaxAnnex = new HashMap<>();
		
		for(InvoiceDTO invoice: invoices) {
			checkNotNull(
				invoice.getTaxAnnex(), 
				"Não foi encontrado anexo para nota fiscal '%s' na tentativa de gerar o imposto 'Simples Nacional'!",
				invoice.getCode()
			);
			
			if (!invoicesByTaxAnnex.containsKey(invoice.getTaxAnnex())) {
				invoicesByTaxAnnex.put(invoice.getTaxAnnex(), new ArrayList<InvoiceDTO>());
			}
			
			invoicesByTaxAnnex
				.get(invoice.getTaxAnnex())
				.add(invoice);
		}
		
		return invoicesByTaxAnnex;
	}
	
}
