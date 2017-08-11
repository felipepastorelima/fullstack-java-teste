package br.com.contabilizei.server.core;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.google.common.base.Strings;

public class BaseDAO<T extends BaseEntity> {
	
	private EntityManagerFactory factory;
	
	private Class<T> entityClass;

	public BaseDAO(Class<T> entityClass) {
		checkNotNull(entityClass, "Classe da entidade não pode ser nula!");
		
		this.entityClass = entityClass;
		
		factory = Persistence.createEntityManagerFactory("contabilizei-server-pu");
	}

	public List<T> findAll() {
		return query("from " + entityClass.getName());
	}
	
	public List<T> query(String hql){
		return query(hql, null);
	}
	
	public List<T> query(
		String hql, 
		HashMap<String, Object> params
	) {
		checkArgument(!Strings.isNullOrEmpty(hql), "HQL não pode ser vazia!");		
		
		EntityManager entityManager = factory.createEntityManager();
		
		try {
			TypedQuery<T> query = entityManager.createQuery(hql, entityClass);
			
			if (params != null) {
				for(String key: params.keySet()) {
					query.setParameter(key, params.get(key));
				}								
			}
			
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			entityManager.close();
		}
	}

	public T find(Long id) {
		checkNotNull(id, "Id não pode ser nulo!");
		checkArgument(id > 0, "Id não pode ser zero!");

		EntityManager entityManager = factory.createEntityManager();
		
		try{
			return entityManager.find(entityClass, id);			
		} finally {
			entityManager.close();
		}
	}

	public T create(T entity) {
		EntityManager entityManager = factory.createEntityManager();

		try {
			entityManager.getTransaction().begin();
			loadRelationsReferences(entityManager, entity);
			entityManager.persist(entity);
			entityManager.getTransaction().commit();
			return entity;
		} catch (PersistenceException e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
			throw e;
		} finally {
			entityManager.close();
		}
	}

	public List<T> create(List<T> entities) {
		EntityManager entityManager = factory.createEntityManager();

		try {
			entityManager.getTransaction().begin();
			for (T entity : entities) {
				loadRelationsReferences(entityManager, entity);
				entityManager.persist(entity);
			}
			entityManager.getTransaction().commit();
			return entities;
		} catch (PersistenceException e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
			throw e;
		} finally {
			entityManager.close();
		}
	}

	public T update(T entity) {
		EntityManager entityManager = factory.createEntityManager();

		try {
			entityManager.getTransaction().begin();
			loadRelationsReferences(entityManager, entity);
			entityManager.merge(entity);
			entityManager.getTransaction().commit();
			return entity;
		} catch (PersistenceException e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
			throw e;
		} finally {
			entityManager.close();
		}
	}

	public List<T> update(List<T> entities) {
		EntityManager entityManager = factory.createEntityManager();

		try {
			entityManager.getTransaction().begin();
			for (T entity : entities) {
				loadRelationsReferences(entityManager, entity);
				entityManager.merge(entity);
			}
			entityManager.getTransaction().commit();
			return entities;
		} catch (PersistenceException e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
			throw e;
		} finally {
			entityManager.close();
		}
	}

	public void delete(Long id) {
		EntityManager entityManager = factory.createEntityManager();

		try {
			T entity = entityManager.getReference(entityClass, id);
			entityManager.getTransaction().begin();
			deleteRelations(entityManager, id);
			entityManager.remove(entity);
			entityManager.getTransaction().commit();
		} catch (PersistenceException e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
			throw e;
		} finally {
			entityManager.close();
		}
	}

	public void delete(List<Long> ids) {
		EntityManager entityManager = factory.createEntityManager();

		try {
			entityManager.getTransaction().begin();
			for (Long id : ids) {				
				T entity = entityManager.getReference(entityClass, id);
				deleteRelations(entityManager, id);
				entityManager.remove(entity);
			}
			entityManager.getTransaction().commit();
		} catch (PersistenceException e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
			throw e;
		} finally {
			entityManager.close();
		}
	}

	protected EntityManager createEntityManager() {
		return factory.createEntityManager();
	}
	
	/**
	 * Método deve ser sobrescrevido quando a entidade possuir relações, 
	 * para carregá-las no entity manager
	 */
	protected void loadRelationsReferences(EntityManager entityManager, T entity) {}
	
	/**
	 * Método deve ser sobrescrevido quando a entidade possuir relações que devem
	 * ser exluídas juntas com ela
	 */
	protected void deleteRelations(EntityManager entityManager, Long id) {}
	
}
