package br.com.contabilizei.server.invoice;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.math.BigDecimal;

import com.google.common.base.Strings;

import br.com.contabilizei.server.core.exception.ValidationException;
import br.com.contabilizei.server.tax.TaxRegime;

public class InvoiceValidator {

	private final InvoiceDTO dto;
	private final InvoiceDAO dao;
	
	public InvoiceValidator(InvoiceDTO dto, InvoiceDAO dao) {
		super();
		this.dto = dto;
		this.dao = dao;
	}
	
	public void validate() {
		try {
			checkNotNull(dto, "Nota fiscal não pode estar vazia!");			
			validateCompany();
			validateCode();
			validateIssueDate();
			validateDescription();
			validateAmount();
			validateTaxAnnex();			
		} catch (Exception e) {
			throw new ValidationException(e.getMessage());
		}
	}
	
	private void validateCompany() {	
		checkNotNull(dto.getCompany(), "Empresa não pode ser estar vazia!");			
		checkNotNull(dto.getCompany().getId(), "Empresa relacionada deve ter um id!");			
	}
	
	private void validateCode() {
		checkNotNull(dto.getCode(), "Código não pode ser vazio!");		
		checkArgument(dto.getCode() > 0, "Código deve ser maior que zero!");
		checkArgument(
			dao.isCodeUniqueForCompany(dto.getCode(), dto.getCompany().getId()), 
			"Empresa já possui uma nota de código '%s'!",
			dto.getCode()
		);
	}
	
	private void validateIssueDate() {
		checkNotNull(dto.getIssueDate(), "Data de emissão não pode estar vazia!");
	}
	
	private void validateDescription() {
		checkArgument(
			!Strings.isNullOrEmpty(dto.getDescription()),
			"Descrição deve ser preenchida!"
		);
		
		checkArgument(
			dto.getDescription().length() <= 255,
			"Descrição deve ter no máximo 255 caracteres!"
		);
	}
	
	private void validateAmount() {
		checkNotNull(dto.getAmount(), "Valor não pode ser vazio!");
		
		checkArgument(
			dto.getAmount().compareTo(new BigDecimal("0")) > 0,
			"Valor deve ser maior que zero!"
		);	
	}
	
	private void validateTaxAnnex() {
		boolean isSimplesNacional = dto.getCompany().getTaxRegime().equals(TaxRegime.SIMPLES_NACIONAL);
		if(isSimplesNacional) {
			checkNotNull(dto.getTaxAnnex(), "Anexo não pode estar vazio!");

			checkArgument(
				dto.getCompany().getTaxAnnexes().contains(dto.getTaxAnnex()),
				"Empresa não possui anexo '%s'!",
				dto.getTaxAnnex().getLabel()
			);
		}
	}
	
}
