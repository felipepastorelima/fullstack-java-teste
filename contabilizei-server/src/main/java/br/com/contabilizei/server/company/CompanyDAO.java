package br.com.contabilizei.server.company;

import static com.google.common.base.Preconditions.checkArgument;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.common.base.Strings;

import br.com.contabilizei.server.core.BaseDAO;

public class CompanyDAO extends BaseDAO<Company> {

	public CompanyDAO() {
		super(Company.class);
	}
	
	public boolean isCnpjUnique(String cnpj) {
		checkArgument(!Strings.isNullOrEmpty(cnpj), "CNPJ não pode estar vazio!");		
		
		EntityManager entityManager = super.createEntityManager();
		
		try {
			String sql = "select count(1) from Company where cnpj = :cnpj";
			Query query = entityManager.createQuery(sql);
			query.setParameter("cnpj", cnpj);			
			Long count = (Long) query.getSingleResult();
			return count.equals(0L);
		} finally {
			entityManager.close();
		}
	}
	
	@Override
	protected void deleteRelations(EntityManager entityManager, Long id) {
		String invoicesDeletionSql = "delete from Invoice where Company_Id = :companyId";
		Query query = entityManager.createQuery(invoicesDeletionSql);
		query.setParameter("companyId", id);
		query.executeUpdate();
		
		String taxesDeletionSql = "delete from Tax where Company_Id = :companyId";
		query = entityManager.createQuery(taxesDeletionSql);
		query.setParameter("companyId", id);
		query.executeUpdate();
	}
	
}
