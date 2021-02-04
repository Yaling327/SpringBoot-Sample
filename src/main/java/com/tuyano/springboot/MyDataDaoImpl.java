package com.tuyano.springboot;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

@Repository
public class MyDataDaoImpl implements MyDataDao<MyData> {

	@PersistenceContext
	EntityManager entityManager;

	private static final long serialVersionUID = 1L;

	public MyDataDaoImpl() {
		super();
	}

	public MyDataDaoImpl(EntityManager manager) {
		this();
		entityManager = manager;
	}
	
	@Override
	public List<MyData> getAll() {
		List<MyData> list = null;
		CriteriaBuilder bulider = 
				entityManager.getCriteriaBuilder();
		CriteriaQuery<MyData> query = 
				bulider.createQuery(MyData.class);
		Root<MyData> root = query.from(MyData.class);
		query.select(root);
		list = (List<MyData>)entityManager
		     .createQuery(query)
		     .getResultList();
		return list;
	}

	@Override
	public MyData findById(long id) {
		return (MyData) entityManager.createQuery("from MyData where id = " + id).getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MyData> findByName(String fstr) {
		List<MyData> list = null;
		String qstr = "from MyData where id = ?1 or name like ?2 or mail like ?3";
		Long fid = 0L;
		try {
			fid = Long.parseLong(fstr);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		Query query = entityManager.createNamedQuery("findWithName").setParameter(3, fstr + "@%");
		list = query.getResultList();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MyData> find(String fstr) {
		CriteriaBuilder builder = 
				entityManager.getCriteriaBuilder();
		CriteriaQuery<MyData> query = 
				builder.createQuery(MyData.class);
		Root<MyData> root = 
				query.from(MyData.class);
		query.select(root)
		        .where(builder.equal(root.get("name"),fstr));
		List<MyData> list = null;
		list = (List<MyData>) entityManager
				.createQuery(query)
				.getResultList();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MyData> findByAge(int min, int max) {
		return (List < MyData >) entityManager
			.createNamedQuery("findByAge")
			.setParameter("min", min)
		    .setParameter("max", max)
		    .getResultList();
	}

}
