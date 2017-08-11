package br.com.contabilizei.server.company;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Strings;

import br.com.contabilizei.server.core.exception.ValidationException;
import br.com.contabilizei.server.core.validator.CNPJValidator;
import br.com.contabilizei.server.tax.TaxRegime;

public class CompanyValidator {

	private final CompanyDTO dto;
	private final CompanyDAO dao;
	
	public CompanyValidator(CompanyDTO dto, CompanyDAO dao) {
		super();
		this.dto = dto;
		this.dao = dao;
	}
	
	public void validate() {
		try{
			checkNotNull(dto, "Empresa não pode estar vazia!");
			validateName();
			validateCnpj();
			validateTaxRegime();
			validateTaxAnnexes();
			validateEmail();		
		} catch (Exception e) {
			throw new ValidationException(e.getMessage());
		}
	}
	
	private void validateName() {
		checkArgument(
			!Strings.isNullOrEmpty(dto.getName()), 
			"Nome da empresa está em branco!"
		);
		
		checkArgument(
			dto.getName().length() <= 255,
			"Nome da empresa deve ter no máximo 255 caracteres!"
		);
	}
	
	private void validateCnpj() {
		checkArgument(
			!Strings.isNullOrEmpty(dto.getCnpj()),
			"CNPJ da empresa está em branco!"
		);
		
		checkArgument(
			CNPJValidator.isValid(dto.getCnpj()),
			"CNPJ da empresa é invalido!"
		);
		
		checkArgument(
			dao.isCnpjUnique(dto.getCnpj()),
			"O CNPJ %s já está cadastrado!",
			dto.getCnpj()
		);
	}
	
	private void validateTaxRegime() {
		checkNotNull(
			dto.getTaxRegime(),
			"Regime tributário não pode estar vazio!"
		);
	}
	
	private void validateTaxAnnexes() {
		boolean isSimplesNacional = dto.getTaxRegime().equals(TaxRegime.SIMPLES_NACIONAL);
		if (isSimplesNacional) {
			checkNotNull(
				dto.getTaxAnnexes(),
				"Anexo não pode estar vazio!"
			);
			
			checkArgument(
				!dto.getTaxAnnexes().isEmpty(),
				"Anexo não pode estar vazio!"
			);
		}
	}
	
	private void validateEmail() {
		checkArgument(
			!Strings.isNullOrEmpty(dto.getEmail()),
			"Email da empresa está em branco!"
		);
		
		checkArgument(
			dto.getEmail().length() <= 255,
			"Email da empresa deve ter no máximo 255 caracteres!"
		);	
	}
	
}
