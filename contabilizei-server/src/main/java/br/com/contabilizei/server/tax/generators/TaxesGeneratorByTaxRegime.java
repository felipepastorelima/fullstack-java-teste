package br.com.contabilizei.server.tax.generators;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;

import br.com.contabilizei.server.tax.TaxRegime;

public class TaxesGeneratorByTaxRegime {

	private static final HashMap<TaxRegime, TaxesGenerator> value;
	
	static {
		value = new HashMap<>();
		value.put(TaxRegime.LUCRO_PRESUMIDO, new LucroPresumidoTaxesGenerator());
		value.put(TaxRegime.SIMPLES_NACIONAL, new SimplesNacionalTaxesGenerator());
	}
	
	public static final TaxesGenerator get(TaxRegime taxRegime) {
		checkNotNull(taxRegime, "Regime tributário está vazio!");
		return value.get(taxRegime);
	}
	
}
