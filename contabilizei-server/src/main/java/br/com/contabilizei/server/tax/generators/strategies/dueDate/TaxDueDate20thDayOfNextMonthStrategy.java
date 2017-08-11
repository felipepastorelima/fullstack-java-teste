package br.com.contabilizei.server.tax.generators.strategies.dueDate;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Calendar;
import java.util.Date;

public class TaxDueDate20thDayOfNextMonthStrategy implements TaxDueDateStrategy{

	@Override
	public Date calculateFor(Date reference) {
		checkNotNull(reference, "Data de referência está vazia!");
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(reference);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 20);
		return calendar.getTime();
	}

}
