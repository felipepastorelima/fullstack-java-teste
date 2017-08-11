package br.com.contabilizei.server.core.api.adapter;

import java.util.Date;

/**
 * Classe utilizada para receber QueryParams nos Resources.
 * Espera uma string que é uma representação em millisegundos da data.
 */
public class DateParam extends Date {
	private static final long serialVersionUID = 1L;

	public DateParam(String s) {
		super(Long.parseLong(s));
	}
	
}
