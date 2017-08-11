package br.com.contabilizei.server.tax;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import br.com.contabilizei.server.company.Company;
import br.com.contabilizei.server.core.BaseDAO;
import br.com.contabilizei.server.core.helpers.DateHelper;

public class TaxDAO extends BaseDAO<Tax> {

	public TaxDAO() {
		super(Tax.class);
	}
	
	public Tax markAsPayed(Long id) {
		Tax tax = find(id);
		tax.setPayed(true);
		return update(tax);
	}
	
	public List<Tax> deleteAndCreateForCompanyAtMonth(
		Long companyId,
		Date referenceDate,
		List<Tax> taxes
	) {
		checkNotNull(companyId, "Id da empresa está vazio!");
		checkArgument(companyId > 0, "Id da empresa está vazio!");
		checkNotNull(referenceDate, "Data de referência está vazia!");
		
		EntityManager entityManager = super.createEntityManager();

		try {
			entityManager.getTransaction().begin();

			Query query = entityManager.createQuery("delete from Tax where Company_Id = :companyId and to_char(referenceDate,'YYYYMM') = :referenceDate order by id asc");
			query.setParameter("companyId", companyId);
			query.setParameter("referenceDate", DateHelper.toYYYYMM(referenceDate));		
			query.executeUpdate();
			
			for(Tax tax: taxes) {
				entityManager.persist(tax);				
			}
			
			entityManager.getTransaction().commit();
			
			return taxes;
		} catch (PersistenceException e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
			throw e;
		} finally {
			entityManager.close();
		}
	}
	
	public List<Tax> findAllByCompanyAndMonth(
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
			"from Tax where company.id = :companyId and to_char(referenceDate,'YYYYMM') = :referenceDate order by id asc",
			params
		);
	}	
	
	@Override
	protected void loadRelationsReferences(EntityManager entityManager, Tax entity) {
		entity.setCompany(
			entityManager.getReference(
				Company.class, 
				entity.getCompany().getId()
			)
		);
	}

}
