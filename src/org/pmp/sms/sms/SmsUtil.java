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
    public void SmsSend(String ids){
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
		/* 创建Sms对象，从smsc对象中获取信息机上行地址 */
		sms = new Sms(smsc.getSmsUpUrl());
		/* 设置接收人数组 */
		String[] dest = item.getSmssReceiver().split(",");
	        String extCode = smsc.getExtendCode();
	        String appID = smsc.getUsername();
	        String psd = smsc.getPassword();
	        /* 执行短信发送方法 */
	        String res = sms.SendMessage(dest,item.getSmssContent(),extCode,appID,psd);
	        if(res == null)continue;
		/* 如果发送成功，则更新短信记录的状态 */
		smsSendDAO.updateState(ids, "发送成功");
		logger.info("message send successfully,smsSend ID = "+item.getSmssId());
	    } catch (AxisFault e1) {
		smsSendDAO.updateState(ids, "发送失败");
		logger.error("get com.chinamobile.openmas.client.Sms object failed");
		e1.printStackTrace();
	    } catch (RemoteException e) {
		smsSendDAO.updateState(ids, "发送失败");
		logger.error("message send failed\ndestinationAddresses:"+item.getSmssReceiver()+"\nContent:"+item.getSmssContent());
		e.printStackTrace();
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
