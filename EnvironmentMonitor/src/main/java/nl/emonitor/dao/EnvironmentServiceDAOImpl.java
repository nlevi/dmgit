package nl.emonitor.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import nl.emonitor.service.EnvironmentService;

@Repository
public class EnvironmentServiceDAOImpl implements EnvironmentServiceDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public EnvironmentServiceDAOImpl() {

	}

	public EnvironmentServiceDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Transactional
	public void save(EnvironmentService eService) {
		sessionFactory.getCurrentSession().saveOrUpdate(eService);
	}

	@Transactional
	public EnvironmentService getServiceById(int id) {
		System.out.println("Session object:" + sessionFactory);		
		EnvironmentService service = (EnvironmentService) sessionFactory.getCurrentSession().createCriteria(EnvironmentService.class)
				.add(Restrictions.idEq(id)).uniqueResult();
		System.out.println("Session object:" + service);
		return service;
	}

	@Transactional	
	public List<EnvironmentService> getServicesByType(String type) {		
		@SuppressWarnings("unchecked")
		List<EnvironmentService> list = sessionFactory.getCurrentSession().createCriteria(EnvironmentService.class)
				.add(Restrictions.eq("service_type", type)).list();
		return list;
	}
	
	@Transactional	
	public List<EnvironmentService> getAllServices() {
		System.out.println("Session object:" + sessionFactory);
		
		@SuppressWarnings("unchecked")
		List<EnvironmentService> list = sessionFactory.getCurrentSession().createCriteria(EnvironmentService.class).addOrder(Order.asc("id")).list();
//				.setProjection(Projections.projectionList()
//						.add(Projections.property("e.id"))
//						.add(Projections.property("e.name"))
//						.add(Projections.property("e.version"))
//						.add(Projections.property("e.status"))
//						.add(Projections.property("e.lastUpdate")))
//				.setResultTransformer(new AliasToBeanResultTransformer(EnvironmentService.class)).list();
		
//		Criteria crit = sessionFactory.getCurrentSession().createCriteria(EnvironmentService.class);
//		ProjectionList projList = Projections.projectionList();
//		projList.add(Projections.property("id"));
//		projList.add(Projections.property("name"));
//		projList.add(Projections.property("version"));
//		projList.add(Projections.property("status"));
//		projList.add(Projections.property("lastUpdate"));
//		crit.setResultTransformer(Transformers.aliasToBean(EnvironmentService.class));
//		crit.setProjection(projList);
//		
//		@SuppressWarnings("unchecked")
//		List<EnvironmentService> list = crit.list(); 

		System.out.println("Session object:" + list.size());
		return list;
	}
	
	@Transactional
	public void delete(int id) {		
		EnvironmentService service = (EnvironmentService) sessionFactory.getCurrentSession().createCriteria(EnvironmentService.class)
				.add(Restrictions.idEq(id)).uniqueResult();
		if (service != null) {
			sessionFactory.getCurrentSession().delete(service);
		}
	}
}
