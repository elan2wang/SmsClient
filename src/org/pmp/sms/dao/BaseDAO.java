package org.pmp.sms.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;
import org.pmp.sms.util.SpringUtil;

public class BaseDAO {
    static Logger logger = Logger.getLogger(BaseDAO.class.getName());
    
    public Session getSession(){
	SessionFactory sessionFactory = (SessionFactory)SpringUtil.getBean("sessionFactory");
	Session session = sessionFactory.openSession();
	
        return session;
    }
    
    /**
     * @Title: executeProcedure
     * @Description: execute procedure with parameter work and return void
     *
     * @param  work org.hibernate.jdbc.Work;
     * @return void
     * @throws RuntimeException
     */
    public void executeProcedure(Work work){
	Session session = getSession();
	Transaction tx = null;
	try {
            tx = session.beginTransaction();
            session.doWork(work);
	} catch (RuntimeException e){
	    tx.rollback();
	    session.close();
	    logger.debug(e.getMessage());
	    throw e;
	} finally {
	    tx.commit();
	    session.close();
	}
    }
    
    /**
     * @Title: getInstance
     * @Description: 根据hql的查询语句获得一个实体对象，或者返回null
     *
     * @param  hql  String类型，表示查询语句
     * @param  debugMsg  String类型，表示debug提示语句，用于写log
     * @return  Object  返回的实体对象，或者null
     * @throws  RuntimeException
     */
    protected Object getInstance(String hql, String debugMsg){
	logger.debug("begin to get instance: "+debugMsg);
	Session session = getSession();
	Transaction tx = null;
	Object obj = null;
	try {
	    tx = session.beginTransaction();
            Query query = session.createQuery(hql);
            List<?> list = query.list();
            if (list.size()!=0)obj = list.get(0);
            tx.commit();
	} catch (RuntimeException e){
            tx.rollback();
	    logger.error("get instance failed: "+debugMsg, e);
            session.close();
            throw e;
	}
	logger.debug("get instance successfully: "+debugMsg);
	session.close();
	return obj;
    }
    
    /**
     * @Title: loadList
     * @Description: 根据hql语句获取列表，hql语句中含有集合类型的参数ids
     *
     * @param  ids  List类型,用于给query的参数ids赋值
     * @return List<?>  返回的列表
     * @throws RuntimeException
     */
    protected List<?> loadList(String hql,List<?> ids,String debugMsg){
	logger.debug("begin to load list: "+debugMsg);
	Session session = getSession();
	Transaction tx = null;
	List<?> list2 = null;
	try {
	    tx = session.beginTransaction();
	    Query query = session.createQuery(hql);
	    query.setParameterList("ids", ids);
	    list2 = query.list();
	} catch (RuntimeException e){
	    tx.rollback();
	    logger.error("load list failed: "+debugMsg, e);
	    session.close();
	    throw e;
	}
	logger.debug("load list successfully: "+debugMsg);
	session.close();
	return list2;
    }
}
