package br.com.contabilizei.server.core;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class DatabaseClearer {

	private EntityManagerFactory factory;

	public DatabaseClearer() {
		factory = Persistence.createEntityManagerFactory("contabilizei-server-pu");
	}
	
	public void clear() {
		EntityManager entityManager = factory.createEntityManager();
		
		try{
			Query truncateQuery = entityManager.createNativeQuery("TRUNCATE SCHEMA PUBLIC AND COMMIT");
			entityManager.getTransaction().begin();
			truncateQuery.executeUpdate();
			entityManager.getTransaction().commit();
		} finally {
			entityManager.close();
		}
	}
	
}
