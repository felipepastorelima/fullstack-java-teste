package br.com.contabilizei.server.tax;

public enum TaxAnnex {
	COMERCIO("Comércio"),
	INDUSTRIA("Indústria"),
	PRESTACAO_DE_SERVICOS("Prestação de Serviços");	
	
	final String label;

	private TaxAnnex(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
	
}
