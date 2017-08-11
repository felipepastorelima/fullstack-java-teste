package br.com.contabilizei.server.invoice;

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
import br.com.contabilizei.server.tax.TaxAnnex;

public class InvoiceDTO extends BaseDTO {
	
	private Long code;
	private String description;
	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	private BigDecimal amount;
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date issueDate;
	private TaxAnnex taxAnnex;
	@JsonIgnore
	private CompanyDTO company;
	
	public InvoiceDTO() {
		super();
	}
	
	public InvoiceDTO(Long id) {
		super(id);
	}
	
	private InvoiceDTO(Invoice entity) {
		super(entity);
		
		if (entity == null) {
			return;
		}
		
		this.code = entity.getCode();
		this.description = entity.getDescription();
		this.amount = entity.getAmount();
		this.issueDate = entity.getIssueDate();
		this.taxAnnex = entity.getTaxAnnex();	
		this.company = CompanyDTO.fromEntity(entity.getCompany());
	}
	
	public static Date maxIssueDateOf(List<InvoiceDTO> invoices) {
		Date max = invoices.get(0).getIssueDate();
		
		for (InvoiceDTO invoice: invoices) {
			if (max.before(invoice.getIssueDate())) {
				max = invoice.getIssueDate();
			}
		}
		
		return max;
	}
	
	public static InvoiceDTO fromEntity(Invoice entity) {
		checkNotNull(entity, "Entidade da nota fiscal é nula!");
		
		return new InvoiceDTO(entity);
	}
	
	public static List<InvoiceDTO> fromEntityList(List<Invoice> entities) {
		List<InvoiceDTO> dtos = new ArrayList<>();

		if (entities == null) return dtos;		
		
		for(Invoice entity: entities) {
			dtos.add(fromEntity(entity));
		}
		
		return dtos;
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
	public CompanyDTO getCompany() {
		return company;
	}
	public void setCompany(CompanyDTO company) {
		this.company = company;
	}
	

}