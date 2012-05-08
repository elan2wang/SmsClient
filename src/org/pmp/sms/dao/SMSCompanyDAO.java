/**
 * Author            : Elan
 * Created On        : 2012-4-17 下午12:01:10
 * 
 * Copyright 2012.  All rights reserved. 
 * 
 */
package org.pmp.sms.dao;


import org.apache.log4j.Logger;
import org.pmp.sms.vo.SMSCompany;
/**
 * @author Elan
 * @version 1.0
 * @update TODO
 */
public class SMSCompanyDAO extends BaseDAO{

    //~ Static Fields ==================================================================================================
    static Logger logger = Logger.getLogger(SMSCompanyDAO.class.getName());
    
    //~ Methods ========================================================================================================
    public SMSCompany getSMSCompanyByID(Integer smscId){
	String debugMsg = "get SMSCompany instance by ID,smscId="+smscId;
	String hql = "from SMSCompany smsc where smsc.smscId="+smscId;
	SMSCompany smsc = null;
	try {
	    smsc = (SMSCompany)getInstance(hql,debugMsg);
	} catch (RuntimeException e){
	    throw e;
	}
	return smsc;
    }
}
