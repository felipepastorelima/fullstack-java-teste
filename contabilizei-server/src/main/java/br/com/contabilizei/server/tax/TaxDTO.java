package br.com.contabilizei.server.tax;

import static com.google.common.base.Preconditions.checkNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.contabilizei.server.company.CompanyDTO;
import br.com.contabilizei.server.core.BaseDTO;
import br.com.contabilizei.server.core.api.adapter.BigDecimalAdapter;
import br.com.contabilizei.server.core.api.adapter.DateAdapter;

public class TaxDTO extends BaseDTO{
	
	private String name;
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date dueDate;
	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	private BigDecimal amount;
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date referenceDate;
	private Boolean payed;
	@JsonIgnore
	private CompanyDTO company;
	
	public TaxDTO() {
		super();
	}
	
	public TaxDTO(Long id) {
		super(id);
	}

	private TaxDTO(Tax entity) {
		super(entity);
		
		if (entity == null) {
			return;
		}
		
		this.name = entity.getName();
		this.dueDate = entity.getDueDate();
		this.amount = entity.getAmount();
		this.referenceDate = entity.getReferenceDate();
		this.payed = entity.getPayed();
		this.company = CompanyDTO.fromEntity(entity.getCompany());
	}
	
	public static TaxDTO fromEntity(Tax entity) {
		checkNotNull(entity, "Entidade do imposto é nulo!");

		return new TaxDTO(entity);
	}

	public static List<TaxDTO> fromEntityList(List<Tax> entities) {
		List<TaxDTO> dtos = new ArrayList<>();
		
		if (entities == null) return dtos;

		for (Tax dto : entities) {
			dtos.add(fromEntity(dto));
		}

		return dtos;
	}	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Date getReferenceDate() {
		return referenceDate;
	}
	public void setReferenceDate(Date referenceDate) {
		this.referenceDate = referenceDate;
	}
	public Boolean getPayed() {
		if (payed == null) {
			payed = Boolean.FALSE;
		}
		
		return payed;
	}
	public void setPayed(Boolean payed) {
		if(payed == null) {
			payed = Boolean.FALSE;
		}
		
		this.payed = payed;
	}
	public CompanyDTO getCompany() {
		return company;
	}
	public void setCompany(CompanyDTO company) {
		this.company = company;
	}

}
