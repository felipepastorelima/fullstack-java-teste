package br.com.contabilizei.server.invoice;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;
import java.util.List;

import br.com.contabilizei.server.company.CompanyService;
import br.com.contabilizei.server.core.exception.ValidationException;
import br.com.contabilizei.server.tax.TaxRegime;

public class InvoiceService {
	
	InvoiceDAO dao;
	CompanyService companyService;
	
	public InvoiceService() {		
		this.dao = new InvoiceDAO();
		this.companyService = new CompanyService();
	}

	public List<InvoiceDTO> findAllByCompanyAndMonth(
		Long companyId,
		Date referenceDate
	) {
		return InvoiceDTO.fromEntityList(
			dao.findAllByCompanyAndMonth(companyId, referenceDate)
		);
	}	
	
	public InvoiceDTO find(Long id) {
		checkNotNull(id, "Id não pode ser vazio!");
		checkArgument(id > 0, "Id deve ser maior que zero!");
		
		return InvoiceDTO.fromEntity(
			dao.find(id)
		);
	}		
	
	public void delete(Long id) {
		checkNotNull(id, "Id não pode ser vazio!");
		checkArgument(id > 0, "Id deve ser maior que zero!");
		
		dao.delete(id);
	}	
	
	public InvoiceDTO create(InvoiceDTO dto) {
		loadCompany(dto);
		clearTaxAnnexIfLucroPresumido(dto);
		new InvoiceValidator(dto, dao).validate();
		
		Invoice entity = dao.create(Invoice.fromDTO(dto));
		dto.setId(entity.getId());
		return dto;
	}
	
	private void loadCompany(InvoiceDTO dto) {
		if (dto.getCompany()==null) return;
		if (dto.getCompany().getId() == null) return;
		if (dto.getCompany().getId() < 0) return;
		
		try{
			dto.setCompany(companyService.find(dto.getCompany().getId()));
		}catch(Exception e) {
			throw new ValidationException(
				String.format("Não foi encontrada empresa de id '%s'!", dto.getCompany().getId())
			);
		}			

	}
	
	private void clearTaxAnnexIfLucroPresumido(InvoiceDTO invoice) {
		boolean isLucroPresumido = invoice.getCompany().getTaxRegime().equals(TaxRegime.LUCRO_PRESUMIDO);
		if (isLucroPresumido) {
			invoice.setTaxAnnex(null);
		}
	}
	
}
