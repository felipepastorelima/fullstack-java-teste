package br.com.contabilizei.server.tax;

import static com.google.common.base.Preconditions.checkNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.contabilizei.server.company.Company;
import br.com.contabilizei.server.core.BaseEntity;

@Entity
public class Tax extends BaseEntity{
	
	@Column(nullable = false, length = 255)
	private String name;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dueDate;
	
	@Column(nullable = false, precision = 14, scale = 2)
	private BigDecimal amount;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date referenceDate;
	
	@Column(nullable = false)
	private Boolean payed;
	
	@ManyToOne(optional=false, fetch = FetchType.EAGER)
	private Company company;
	
	public Tax() {
		super();
	}
	
	private Tax(TaxDTO dto) {
		super(dto);
		
		if (dto == null) {
			return;
		}
		
		this.name = dto.getName();
		this.dueDate = dto.getDueDate();
		this.amount = dto.getAmount();
		this.referenceDate = dto.getReferenceDate();
		this.payed = dto.getPayed();
		this.company = Company.fromDTO(dto.getCompany());
	}
	
	public static Tax fromDTO(TaxDTO dto) {
		checkNotNull(dto, "DTO da empresa é nula!");

		return new Tax(dto);
	}

	public static List<Tax> fromDTOList(List<TaxDTO> dtos) {
		List<Tax> entities = new ArrayList<>();
		
		if (dtos == null) return entities;

		for (TaxDTO dto : dtos) {
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
		return payed;
	}
	public void setPayed(Boolean payed) {
		this.payed = payed;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
}
