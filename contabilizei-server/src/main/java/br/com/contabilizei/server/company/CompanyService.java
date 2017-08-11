package br.com.contabilizei.server.company;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import br.com.contabilizei.server.tax.TaxRegime;

public class CompanyService {
	
	CompanyDAO dao;
	
	public CompanyService() {		
		this.dao = new CompanyDAO();
	}

	public List<CompanyDTO> findAll() {
		return CompanyDTO.fromEntityList(
			dao.findAll()
		);
	}	
	
	public CompanyDTO find(Long id) {
		checkNotNull(id, "Id não pode ser vazio!");
		checkArgument(id > 0, "Id deve ser maior que zero!");
		
		return CompanyDTO.fromEntity(
			dao.find(id)
		);
	}		
	
	public void delete(Long id) {
		checkNotNull(id, "Id não pode ser vazio!");
		checkArgument(id > 0, "Id deve ser maior que zero!");
		
		dao.delete(id);
	}	
	
	public CompanyDTO create(CompanyDTO dto) {
		dto.sanitizeCnpj();
		new CompanyValidator(dto, dao).validate();				
		clearTaxAnnexesIfLucroPresumido(dto);
		
		Company entity = dao.create(Company.fromDTO(dto));
		dto.setId(entity.getId());
		return dto;
	}
	
	private void clearTaxAnnexesIfLucroPresumido(CompanyDTO company) {
		if (company.getTaxRegime().equals(TaxRegime.LUCRO_PRESUMIDO)) {
			company.setTaxAnnexes(null);
		}
	}

}
