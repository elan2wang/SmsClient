/**
 * Author            : Elan
 * Created On        : 2012-5-7 下午06:30:15
 * 
 * Copyright 2012.  All rights reserved. 
 *
 * Revision History
 * 
 *    Date       Modifier       Comments
 * ----------    -------------  --------------------------------------------
 * 
 */
package org.pmp.sms.sms;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;
import org.pmp.sms.dao.SMSCompanyDAO;
import org.pmp.sms.dao.SMSSendDAO;
import org.pmp.sms.vo.SMSCompany;
import org.pmp.sms.vo.SMSSend;

import com.chinamobile.openmas.client.Sms;

/**
 * @author Elan
 * @version 1.0
 * @update TODO
 */
public class SmsUtil {

    //~ Static Fields ==================================================================================================
    private static Logger logger = Logger.getLogger(SmsUtil.class.getName());
    //~ Instance Fields ================================================================================================
    private SMSCompanyDAO smsCompanyDAO;
    private SMSSendDAO smsSendDAO;
    
    //~ Methods ========================================================================================================
    public void SmsSend(String ids) throws Exception{
	List<Integer> list = new ArrayList<Integer>();
	String [] ids_tmp = ids.split(",");
	for (int i=0;i<ids_tmp.length;i++){
	    list.add(Integer.parseInt(ids_tmp[i]));
	}
	
	List<SMSSend> smsList = smsSendDAO.loadSmsList_ByIds(list);
	//logger.debug("smsList.size="+smsList.size());
	Iterator<SMSSend> ite = smsList.iterator();
	while(ite.hasNext()){
	    SMSSend item = ite.next();
	    SMSCompany smsc = item.getSMSCompany();
            Sms sms = null;
	    try {
		/* set the Sms properties */
		sms = new Sms(smsc.getSmsUpUrl());
		String[] dest = item.getSmssReceiver().split(",");
	        String extendCode = smsc.getExtendCode();
	        String applicationId = smsc.getUsername();
	        String password = smsc.getPassword();
	        //logger.debug("dest.size="+dest.length);
	        String res = sms.SendMessage(dest, item.getSmssContent(), extendCode, applicationId, password);
	        //logger.debug("return message"+res);
		/* update SMS state after sending message successfully */
	        smsSendDAO.updateState(ids, "SUCCESS");
	        logger.debug("message send successfully");
	    } catch (AxisFault e1) {
		smsSendDAO.updateState(ids, "FAILED");
		logger.error("get com.chinamobile.openmas.client.Sms object failed");
		e1.printStackTrace();
		throw e1;
	    } catch (RemoteException e) {
		smsSendDAO.updateState(ids, "FAILED");
		logger.error("message send failed\ndestinationAddresses:"+item.getSmssReceiver()+"\nContent:"+item.getSmssContent());
		e.printStackTrace();
		throw e;
	    }
	}
    }
    
    //~ Getters and Setters ============================================================================================
    public SMSCompanyDAO getSmsCompanyDAO() {
        return smsCompanyDAO;
    }
    public void setSmsCompanyDAO(SMSCompanyDAO smsCompanyDAO) {
        this.smsCompanyDAO = smsCompanyDAO;
    }
    public SMSSendDAO getSmsSendDAO() {
        return smsSendDAO;
    }
    public void setSmsSendDAO(SMSSendDAO smsSendDAO) {
        this.smsSendDAO = smsSendDAO;
    }
}
