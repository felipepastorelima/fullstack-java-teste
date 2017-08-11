package br.com.contabilizei.server.invoice;

import static com.google.common.base.Preconditions.checkNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.contabilizei.server.company.Company;
import br.com.contabilizei.server.core.BaseEntity;
import br.com.contabilizei.server.tax.TaxAnnex;

@Entity
public class Invoice extends BaseEntity {
	
	@Column(nullable = false)
	private Long code;
	
	@Column(nullable = false, length = 255)
	private String description;
	
	@Column(nullable = false, precision = 14, scale = 2)
	private BigDecimal amount;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date issueDate;
	
	@Enumerated(EnumType.STRING)
	private TaxAnnex taxAnnex;
	
	@ManyToOne(optional=false, fetch = FetchType.EAGER)
	private Company company;
	
	public Invoice() {
		super();
	}
	
	private Invoice(InvoiceDTO dto) {
		super(dto);
		
		if (dto == null) {
			return;
		}
		
		this.code = dto.getCode();
		this.description = dto.getDescription();
		this.amount = dto.getAmount();
		this.issueDate = dto.getIssueDate();
		this.taxAnnex = dto.getTaxAnnex();
		this.company = Company.fromDTO(dto.getCompany());
	}
	
	public static Invoice fromDTO(InvoiceDTO dto) {
		checkNotNull(dto, "DTO da nota fiscal é nula!");

		return new Invoice(dto);
	}

	public static List<Invoice> fromDTOList(List<InvoiceDTO> dtos) {
		List<Invoice> entities = new ArrayList<>();
		
		if (dtos == null) return entities;

		for (InvoiceDTO dto : dtos) {
			entities.add(fromDTO(dto));
		}

		return entities;
	}	
	
	public Long getCode() {
		return code;
	}
	public void setCode(Long code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Date getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	public TaxAnnex getTaxAnnex() {
		return taxAnnex;
	}
	public void setTaxAnnex(TaxAnnex taxAnnex) {
		this.taxAnnex = taxAnnex;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	
}
