package br.com.contabilizei.server.tax;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.com.contabilizei.server.core.api.adapter.DateAdapter;

public class TaxReferenceDateDTO {
	
	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date referenceDate;

	public Date getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(Date referenceDate) {
		this.referenceDate = referenceDate;
	}

}
