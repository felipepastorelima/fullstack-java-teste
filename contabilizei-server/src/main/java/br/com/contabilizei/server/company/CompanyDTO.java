package br.com.contabilizei.server.company;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.CharMatcher;

import br.com.contabilizei.server.core.BaseDTO;
import br.com.contabilizei.server.tax.TaxAnnex;
import br.com.contabilizei.server.tax.TaxRegime;

public class CompanyDTO extends BaseDTO{
	
	private String name;
	private String cnpj;
	private TaxRegime taxRegime;
	private List<TaxAnnex> taxAnnexes;
	private String email;	
	
	public CompanyDTO() {}
	
	public CompanyDTO(Long id) {
		super(id);
	}
	
	private CompanyDTO(Company entity) {
		super(entity);
		
		if (entity == null) {
			return;
		}
		
		this.name = entity.getName();
		this.cnpj = entity.getCnpj();
		this.taxRegime = entity.getTaxRegime();
		this.taxAnnexes = entity.getTaxAnnexes();
		this.email = entity.getEmail();
	}
	
	public static CompanyDTO fromEntity(Company entity) {
		checkNotNull(entity, "Entidade da empresa é nula!");
		
		return new CompanyDTO(entity);
	}
	
	public static List<CompanyDTO> fromEntityList(List<Company> entities) {
		List<CompanyDTO> dtos = new ArrayList<>();
		
		if (entities == null) return dtos;
		
		for(Company entity: entities) {
			dtos.add(fromEntity(entity));
		}
		
		return dtos;
	}
	
	/**
	 * Remove caracteres especiais do CNPJ
	 */
	public void sanitizeCnpj() {
		if (cnpj == null) return; 
		
		this.cnpj = CharMatcher
			.javaDigit()
			.retainFrom(cnpj);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCnpj() {
		return cnpj;
	}
	
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public TaxRegime getTaxRegime() {
		return taxRegime;
	}

	public void setTaxRegime(TaxRegime taxRegime) {
		this.taxRegime = taxRegime;
	}

	public List<TaxAnnex> getTaxAnnexes() {
		if (taxAnnexes == null) {
			taxAnnexes = new ArrayList<>();
		}
		
		return taxAnnexes;
	}

	public void setTaxAnnexes(List<TaxAnnex> taxAnnexes) {
		this.taxAnnexes = taxAnnexes;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
