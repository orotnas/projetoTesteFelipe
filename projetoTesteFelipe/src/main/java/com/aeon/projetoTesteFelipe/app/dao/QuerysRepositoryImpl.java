package com.aeon.projetoTesteFelipe.app.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.StringUtils;

import com.aeon.projetoTesteFelipe.app.model.LogRequest;
import com.aeon.projetoTesteFelipe.app.vo.LogRequestVO;

public class QuerysRepositoryImpl implements QuerysRepository{
	
	@PersistenceContext
	private EntityManager entity;

	@Override
	public List<LogRequest> buscarLogsByFiltro(LogRequestVO filtro) {
		
		Session session = entity.unwrap(Session.class);
		
		DetachedCriteria criteria = DetachedCriteria.forClass(LogRequest.class);
		
		if(filtro.getDtInicial() != null && filtro.getDtFinal() != null) {	
			try {
				criteria.add(Restrictions.between("data", new SimpleDateFormat("dd/MM/yyyy").parse(filtro.getDtInicial()), 
						new SimpleDateFormat("dd/MM/yyyy").parse(filtro.getDtFinal())));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(!StringUtils.isEmpty(filtro.getIp())){
			criteria.add(Restrictions.ilike("ip", filtro.getIp(), MatchMode.ANYWHERE));
		}
		
		if(!StringUtils.isEmpty(filtro.getRequest())){
			criteria.add(Restrictions.ilike("request", filtro.getRequest(), MatchMode.ANYWHERE));
		}
		
		if(!StringUtils.isEmpty(filtro.getStatus())){
			criteria.add(Restrictions.ilike("status", filtro.getStatus(), MatchMode.ANYWHERE));
		}
		
		if(!StringUtils.isEmpty(filtro.getUserAgent())){
			criteria.add(Restrictions.ilike("userAgent", filtro.getUserAgent(), MatchMode.ANYWHERE));
		}
		
		criteria.addOrder(Order.asc("seq"));
		
		List<LogRequest> lista = (List<LogRequest>) criteria.getExecutableCriteria(session).list();
		return lista;
	}

}
