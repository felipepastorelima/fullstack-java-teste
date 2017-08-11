package br.com.contabilizei.server.company;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;

import br.com.contabilizei.server.core.BaseEntity;
import br.com.contabilizei.server.tax.TaxAnnex;
import br.com.contabilizei.server.tax.TaxRegime;

@Entity
public class Company extends BaseEntity {

	@Column(nullable = false, length = 255)
	private String name;

	@Column(nullable = false, length = 14, unique = true)
	private String cnpj;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TaxRegime taxRegime;

	@ElementCollection(
		targetClass = TaxAnnex.class, 
		fetch = FetchType.EAGER
	) 
	@CollectionTable
	private List<TaxAnnex> taxAnnexes;

	@Column(nullable = false, length = 255)
	private String email;
	
	public Company() {
		super();
	}

	private Company(CompanyDTO dto) {
		super(dto);
		
		if (dto == null) {
			return;
		}
		
		this.name = dto.getName();
		this.cnpj = dto.getCnpj();
		this.taxAnnexes = dto.getTaxAnnexes();
		this.taxRegime = dto.getTaxRegime();
		this.email = dto.getEmail();
	}

	public static Company fromDTO(CompanyDTO dto) {
		checkNotNull(dto, "DTO da empresa é nula!");

		return new Company(dto);
	}

	public static List<Company> fromDTOList(List<CompanyDTO> dtos) {
		List<Company> entities = new ArrayList<>();
		
		if (dtos == null) return entities;

		for (CompanyDTO dto : dtos) {
			entities.add(fromDTO(dto));
		}

		return entities;
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
