package br.com.contabilizei.server.invoice;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.contabilizei.server.company.Company;
import br.com.contabilizei.server.core.BaseDAO;
import br.com.contabilizei.server.core.helpers.DateHelper;

public class InvoiceDAO extends BaseDAO<Invoice>{

	public InvoiceDAO() {
		super(Invoice.class);
	}
	
	public List<Invoice> findAllByCompanyAndMonth(
			Long companyId,
			Date referenceDate
	) {
		checkNotNull(companyId, "Id da empresa não pode estar vazio!");
		checkArgument(companyId > 0, "Id da empresa deve ser maior que zero!");
		checkNotNull(referenceDate, "Data de referência não pode estar vazia!");
		
		HashMap<String, Object> params = new HashMap<>();
		params.put("companyId", companyId);
		params.put("referenceDate", DateHelper.toYYYYMM(referenceDate));		
		
		return query(
			"from Invoice where company.id = :companyId and to_char(issueDate,'YYYYMM') = :referenceDate order by issueDate desc",
			params
		);
	}	
	
	public boolean isCodeUniqueForCompany(Long code, Long companyId) {
		checkNotNull(code, "Código não pode estar vazio!");
		checkArgument(code > 0, "Código deve ser maior que zero!");
		checkNotNull(companyId, "Id da empresa não pode estar vazio!");
		checkArgument(companyId > 0, "Id da empresa deve ser maior que zero!");
		
		EntityManager entityManager = super.createEntityManager();
		
		try {
			String sql = "select count(1) from Invoice i where company_id = :companyId and code = :code";
			Query query = entityManager.createQuery(sql);
			query.setParameter("code", code);
			query.setParameter("companyId", companyId);
			Long count = (Long) query.getSingleResult();
			return count.equals(0L);
		} finally {
			entityManager.close();
		}
	}
	
	@Override
	protected void loadRelationsReferences(EntityManager entityManager, Invoice entity) {
		entity.setCompany(
			entityManager.getReference(
				Company.class, 
				entity.getCompany().getId()
			)
		);
	}

}
